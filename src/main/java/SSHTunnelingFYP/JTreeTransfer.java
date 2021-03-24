package SSHTunnelingFYP;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import static javax.swing.TransferHandler.COPY;

public class JTreeTransfer extends TransferHandler{
    DataFlavor nodeFlavor;
    DataFlavor[] flavors = new DataFlavor[1];
    String destPath;
    
    public JTreeTransfer() {
        try {
            String mimeType = DataFlavor.javaJVMLocalObjectMimeType +
                              ";class=\"" +
                javax.swing.tree.DefaultMutableTreeNode[].class.getName() +
                              "\"";
            //System.out.print("Mimetype: " + mimeType);
            nodeFlavor = new DataFlavor(mimeType);
            flavors[0] = nodeFlavor;
        } catch(ClassNotFoundException e) {
            System.out.println("ClassNotFound: " + e.getMessage());
        }
    }
  
    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        if(!support.isDrop()) {
            return false;
        }
        support.setShowDropLocation(true);
        if(!support.isDataFlavorSupported(nodeFlavor)) {
            return false;
        }
        // Do not allow a drop on the drag source selections.
        JTree.DropLocation dl =
                (JTree.DropLocation)support.getDropLocation();
        JTree tree = (JTree)support.getComponent();
        int dropRow = tree.getRowForPath(dl.getPath());
        int[] selRows = tree.getSelectionRows();
        for(int i = 0; i < selRows.length; i++) {
            if(selRows[i] == dropRow) {
                return false;
            }
        }
        // Do not allow MOVE-action drops if a non-leaf node is
        // selected unless all of its children are also selected
        int action = support.getDropAction();
        if(action == COPY) {
            return haveCompleteNode(tree);
        }
        // Do not allow a non-leaf node to be copied to a level
        // which is less than its source level
        TreePath dest = dl.getPath();
        DefaultMutableTreeNode target = (DefaultMutableTreeNode)dest.getLastPathComponent();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode)path.getLastPathComponent();
        if(firstNode.getChildCount() > 0 &&
               target.getLevel() < firstNode.getLevel()) {
            return false;
        }
        
//        //Test
//        //Do not allow a leaf node level to be copied into a leaf node level
//        else if(firstNode.isLeaf() && target.isLeaf() && action == MOVE) {
//            return false;
//        }
        
        return true;
    }
  
    private boolean haveCompleteNode(JTree tree) {
        int[] selRows = tree.getSelectionRows();
        TreePath path = tree.getPathForRow(selRows[0]);
        DefaultMutableTreeNode first =
            (DefaultMutableTreeNode)path.getLastPathComponent();
        int childCount = first.getChildCount();
        // first has children and no children are selected
        if(childCount > 0 && selRows.length == 1)
            return false;
        // first may have children
        for(int i = 1; i < selRows.length; i++) {
            path = tree.getPathForRow(selRows[i]);
            DefaultMutableTreeNode next =
                (DefaultMutableTreeNode)path.getLastPathComponent();
            if(first.isNodeChild(next)) {
                // Found a child of first
                if(childCount > selRows.length-1) {
                    // Not all children of first are selected
                    return false;
                }
            }
        }
        return true;
    }
  
    @Override
    protected Transferable createTransferable(JComponent c) {
        JTree tree = (JTree)c;
        TreePath[] paths = tree.getSelectionPaths();
        if(paths != null) {
            // Make up a node array of copies for transfer and
            // another for/of the nodes incase they are needed to be removed
            // later on after a successful drop
            List<DefaultMutableTreeNode> copies = new ArrayList<DefaultMutableTreeNode>();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)paths[0].getLastPathComponent();
            DefaultMutableTreeNode copy = copy(node);
            copies.add(copy);
            
            for(int i = 1; i < paths.length; i++) {
                DefaultMutableTreeNode next =
                    (DefaultMutableTreeNode)paths[i].getLastPathComponent();
                // Do not allow higher level nodes to be added to list.
                if(next.getLevel() < node.getLevel()) {
                    break;
                } else if(next.getLevel() > node.getLevel()) {  // child node
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
        if(!canImport(support)) {
            return false;
        }
        //Extract transfer data
        DefaultMutableTreeNode[] nodes = null;
        try {
            Transferable t = support.getTransferable();
            nodes = (DefaultMutableTreeNode[])t.getTransferData(nodeFlavor);
        } catch(UnsupportedFlavorException ufe) {
            System.out.println("Unsupported Flavor error: " + ufe.getMessage());
        } catch(java.io.IOException ioe) {
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
        if(childIndex == -1) {     // DropMode ON
            index = parent.getChildCount();
        }
        // Add data to model
        for(int i = 0; i < nodes.length; i++) {
            model.insertNodeInto(nodes[i], parent, index++);
        }
        
        // get parent file path of node dropped
        String tempdpath = dest.toString().replaceAll("\\]| |\\[|", ""); 
        //System.out.println("dest: " + dest);
        
        //linux environment
        if (tempdpath.charAt(0) == '/') {
            tempdpath = tempdpath.substring(1); //remove first '/'
            tempdpath = tempdpath.replaceAll(" ", "");
            tempdpath = tempdpath.replaceAll("/", ",");
            //System.out.println("temppath: " + tempdpath);
            
            StringBuilder sb = new StringBuilder();
            String[] tempNodes = tempdpath.split(",");

            for(int i=0; i<tempNodes.length;  i++) {
                sb.append("/").append(tempNodes[i]);

                //last item
                if(i == tempNodes.length-1) {
                    //get dropped node name and append to string builder
                    sb.append("/").append(nodes[nodes.length-1]);
                }
            } 
            destPath = sb.toString();
            System.out.println("dpath: " + destPath);
        }
        
        //windows environment
        else {
            //System.out.println("temppath: " + tempdpath);
            StringBuilder sb = new StringBuilder();
            String[] tempNodes = tempdpath.split(",");
                
            for(int i=0; i<tempNodes.length;  i++) {
                sb.append(File.separatorChar).append(tempNodes[i]);

                //last item
                if(i == tempNodes.length-1) {
                    //get dropped node name and append to string builder
                    sb.append(File.separatorChar).append(nodes[nodes.length-1]);
                }
            } 
            destPath = sb.toString();
            System.out.println("dpath: " + destPath);
        }
        
        return true;
    }
    
    //get parent file path of node dropped
    public String getDestPath() {
        return destPath;
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
            if(!isDataFlavorSupported(flavor))
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