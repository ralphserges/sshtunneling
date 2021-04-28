package com.mycompany.sshtunneling.sftp;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;

public class JTreeTransfer extends TransferHandler{
    DataFlavor nodeFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    
    public JTreeTransfer() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                              ";class=\"" +
                javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                              "\"";
            //System.out.print("Mimetype: " + mimeType);
            nodeFlavor = new DataFlavor(mimeType);
            flavors[0] = nodeFlavor;
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
  
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if (!support.isDrop()) {
            //System.out.println("Support isDrop: False"); //
            return false;
        }
        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(nodeFlavor)) {
            //System.out.println("Support isDataFlavorSupported: False"); //
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for (int i = 0; i < selRows.length; i++) {
            if (selRows[i] == dropRow) {
                return false;
            }
        }
        //System.out.println("Support canImport: true"); //
        return true;
    }
  
    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first =
            (DefaultMutableTreeNode)path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected
        if (childCount > 0 && selRows.length == 1)
            return false;
        // first may have children
        for (int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next =
                (DefaultMutableTreeNode)path.getLastPathComponent();
            if (first.isNodeChild(next)) {
                // Found a child of first
                if (childCount > selRows.length-1) {
                    // Not all children of first are selected
                    return false;
                }
            }
        }
        System.out.println("Support haveCompletedNode: true"); //
        return true;
    }
  
    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        System.out.println("createTransferable length: " + paths.length); //
        System.out.println("createTransferable: " + paths[0].getLastPathComponent()); //
        if (paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes incase they are needed to be removed
            // later on after a successful drop
            List<DefaultMutableTreeNode> copies = new ArrayList<>();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            
            for (int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next =
                    (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if (next.getLevel() > node.getLevel()) {  // child node
                    copy.add(copy(next));
                    // node already contains child/sibling
                } else {                                        
                    copies.add(copy(next));
                }
            }
            DefaultMutableTreeNode[] nodes =
                copies.toArray(new DefaultMutableTreeNode[copies.size()]);
   
            return new NodesTransferable(nodes);
        }
        return null;
    }
  
    // Defensive copy used in createTransferable. 
    private DefaultMutableTreeNode copy(TreeNode node) {
        DefaultMutableTreeNode n =(DefaultMutableTreeNode)node;
        return (DefaultMutableTreeNode) n.clone();
    }
 
    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }
  
    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (!canImport(support)) {
            //System.out.println("Support canImport data: False"); //
            return false;
        }
        //Extract transfer data
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[])t.getTransferData(nodeFlavor);
        } catch (UnsupportedFlavorException ufe) {
            System.out.println("Unsupported Flavor error: " + ufe.getMessage());
        } catch (java.io.IOException ioe) {
            System.out.println("I/O error: " + ioe.getMessage());
        }
        //Get drop location info
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        int childIndex = dl.getChildIndex();
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode parent =
            (DefaultMutableTreeNode)dest.getLastPathComponent();
        JTree tree = (JTree)support.getComponent();
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        // Configure drop mode
        int index = childIndex;    // DropMode INSERT
        if (childIndex == -1) {     // DropMode ON
            index = parent.getChildCount();
        }
     
        //list to store all child nodes of drop target location
        List<DefaultMutableTreeNode> childNodes = new ArrayList<>();
        boolean gotDuplicate = false;
        
        //add all existing child nodes to list 
        for (int i=0; i<parent.getChildCount(); i++) {
            childNodes.add((DefaultMutableTreeNode)parent.getChildAt(i));
            //testNode = (DefaultMutableTreeNode) parent.getChildAt(i);                
        } 
        
        //check for import data is null
        if (nodes == null){
            //System.out.println("Import data is null: true"); //
            return false;
        }
        
        //if-else to convert imported data from File type to String and display inserted node
        //as only the file name (Linux env)
        //String to File type (Windows env) 
        String localRoot = System.getProperty("user.home");
        if (model.getRoot().toString().equals(localRoot)) { // local-windows env
            System.out.println("transfer mode is: Retrieval-Remote-to-Local");
            //convert into File type node
            for (int i=0; i<nodes.length; i++) {
                File testLocalTransfer = new File(dest.getLastPathComponent().toString() + "\\" + nodes[i].toString());
                nodes[i].setUserObject(testLocalTransfer);
            }
            
        }
        else {  //remote-linux env 
            System.out.println("transfer mode is: Transfer-Local-to-Remote");
            for (int i=0; i<nodes.length; i++) {
                String test = nodes[i].toString().substring(nodes[i].toString().lastIndexOf('\\'));
                test = test.substring(1);  //remove first \ char
                System.out.println("this is a test: " + test);
                nodes[i].setUserObject(test); //change the name
                System.out.println("transfer child count: "+ nodes[i].getChildCount());
                //transfer node contains child 
                if (nodes[i].getChildCount() >= 1) {
                    for(int j=0; j<nodes[i].getChildCount(); j++) {   //rename each child to get only the filename
                        DefaultMutableTreeNode temp = (DefaultMutableTreeNode) model.getChild(nodes[i], j);
                        String test2 = temp.toString().substring(temp.toString().lastIndexOf('\\'));
                        test2 = test2.substring(1);  //remove first \ char
                        System.out.println("this is a test child: " + test2);
                        temp.setUserObject(test2); //change the name
                    }
                }
            
            }
        }
           
        //check if drop nodes is of duplicate from the list
        for (DefaultMutableTreeNode n : childNodes) {          
            if (n.toString().equalsIgnoreCase(nodes[nodes.length-1].toString())) {
                gotDuplicate = true;
                System.out.println("found duplicate node: " + gotDuplicate + ", " + nodes[nodes.length-1]);
                
                //remove existing node and replace with new imported node
                model.removeNodeFromParent(n);
                for (DefaultMutableTreeNode node : nodes)
                    //add imported data to model
                    model.insertNodeInto(node, parent, index++);
                
            }
        }
        
        //no duplicate, insert nodes into jtree
        if (gotDuplicate == false) {
            for (DefaultMutableTreeNode node : nodes)
                //add imported data to model
                model.insertNodeInto(node, parent, index++);
            
        }
        
        //System.out.println("Support canImportData: true"); //
        
        return true;
    }
    
    @Override
    public String toString() {
        return getClass().getName();
    }
  
    public class NodesTransferable implements Transferable {
        DefaultMutableTreeNode[] nodes;
  
        public NodesTransferable(DefaultMutableTreeNode[] nodes) {
            this.nodes = nodes;
         }
  
        @Override
        public Object getTransferData(DataFlavor flavor)
                                 throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor))
                throw new UnsupportedFlavorException(flavor);
            return nodes;
        }
  
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return flavors;
        }
  
        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return nodeFlavor.equals(flavor);
        }
    }//end NodeTransferable 
}//end JTreeTransfer class