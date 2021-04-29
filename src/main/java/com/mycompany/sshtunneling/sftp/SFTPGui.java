
package com.mycompany.sshtunneling.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import com.mycompany.sshtunneling.SSHClientGui;
import com.mycompany.sshtunneling.jtreedisplay.FileTreeCellRenderer;
import com.mycompany.sshtunneling.jtreedisplay.JTreeLoader;
import com.mycompany.sshtunneling.jtreedisplay.MyTreeModel;
import com.mycompany.sshtunneling.jtreedisplay.RemoteFileTreeCellRenderer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class SFTPGui extends javax.swing.JFrame {
    
    private String destDir, localDir, remoteDir, localFileName, remoteFileName;
    private static SSHClientGui sshClientG = new SSHClientGui();
    
    
    /**
     * Creates new form SFTPGui
     */
    public SFTPGui() {
        initComponents();
        displayLocalFileStructure();
        displayRemoteFileStruct();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        remoteJTree = new javax.swing.JTree();
        jScrollPane2 = new javax.swing.JScrollPane();
        localJTree = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        deleteButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(remoteJTree);

        jScrollPane2.setViewportView(localJTree);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Remote File Structure");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Local File Structure");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("SFTP File Transfer (Drag and Drop)");

        deleteButton.setText("Delete (Remote)");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        createButton.setText("Create Folder (Remote)");
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 336, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(createButton)
                        .addGap(54, 54, 54)
                        .addComponent(deleteButton)
                        .addGap(21, 21, 21)))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(createButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        String selectedFile = remoteFileName;
        
        if (selectedFile != null ) {
             //sftp delete files in remote 
            if (SSHClientGui.sftpChannel != null){
                int option = JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to permanently delete " + selectedFile + " ?",
                        "Delete File/Folder",
                        JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
                
                //user confirm selection YES option
                if (option == JOptionPane.YES_OPTION) {
                    SFTPUtil sftpUtil = new SFTPUtil();
                    
                    sftpUtil.removeFile(SSHClientGui.sftpChannel, remoteFileName, remoteDir, sshClientG);
                
                    String hostName = SSHClientGui.session.getUserName();

                    //get remote root path 
                    String mainPath = String.format("/home/%s/Desktop",hostName);

                    DefaultMutableTreeNode nroot = new DefaultMutableTreeNode(mainPath);
                    try {
                        JTreeLoader remoteTreeLoader = new JTreeLoader();
                        remoteTreeLoader.addNodesRemoteV2(mainPath, nroot, SSHClientGui.sftpChannel);


                    } catch (SftpException ex) {
                        Logger.getLogger(SFTPGui.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 
                    //reload the jtree everytime a node is deleted
                    DefaultTreeModel model = (DefaultTreeModel) remoteJTree.getModel();
                    model.setRoot(nroot);
                    model.reload();
                
                //user confirm selection NO option
                } else {
                        System.out.println("File/Folder: " + selectedFile + " not deleted.");
                }
            }
        } else { //validation for when file is deleted and delete button is pressed again
            JOptionPane.showMessageDialog(this, 
                    "Please select a file/folder from the remote file structure to delete",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        //reset to null for validation
        remoteFileName = null;
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        // TODO add your handling code here:
        String selectedFile = remoteFileName;
        
        if (selectedFile != null ) {
             //sftp delete files in remote 
            if (SSHClientGui.sftpChannel != null){
                String folderNameIn = JOptionPane.showInputDialog(null, "Please input folder name", "Create new folder", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Your new folder name is: " + folderNameIn);
                
                SFTPUtil sftpUtil = new SFTPUtil();
                //create new folder
                sftpUtil.createFolder(SSHClientGui.sftpChannel, folderNameIn, remoteDir, sshClientG);

                String hostName = SSHClientGui.session.getUserName();

                //get remote root path 
                String mainPath = String.format("/home/%s/Desktop",hostName);

                DefaultMutableTreeNode nroot = new DefaultMutableTreeNode(mainPath);
                try {
                    JTreeLoader remoteTreeLoader = new JTreeLoader();
                    remoteTreeLoader.addNodesRemoteV2(mainPath, nroot, SSHClientGui.sftpChannel);


                } catch (SftpException ex) {
                    Logger.getLogger(SFTPGui.class.getName()).log(Level.SEVERE, null, ex);
                }

                //reload the jtree everytime a new folder is created
                DefaultTreeModel model = (DefaultTreeModel) remoteJTree.getModel();
                model.setRoot(nroot);
                model.reload();
                
            }
        } else { //validation for when folder is not created and delete button is pressed again
            JOptionPane.showMessageDialog(this, 
                    "Please select where will the folder be created from the remote file structure",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        //reset to null for validation
        remoteFileName = null;
        
    }//GEN-LAST:event_createButtonActionPerformed

    public void displayLocalFileStructure() {
        
        
        String root = System.getProperty("user.home");

        JTreeLoader loader = new JTreeLoader();
        DefaultMutableTreeNode rootNode = loader.createNodes(root);

        //DefaultTreeModel model = new DefaultTreeModel(rootNode, true);
        //DefaultTreeModel model = (DefaultTreeModel) localJTree.getModel(); //
        
        
        MyTreeModel myTreeModel = new MyTreeModel(); //*
        myTreeModel.setRoot(rootNode); //
        myTreeModel.setAsksAllowsChildren(true);//
        
        localJTree.setModel(myTreeModel); //*
        localJTree.addTreeWillExpandListener(loader);
        localJTree.setCellRenderer(new FileTreeCellRenderer());
      
        
        //set drag and drop 
        localJTree.setDragEnabled(true);
        localJTree.setDropMode(DropMode.ON_OR_INSERT);
        
        //transfer data from JTree
        JTreeTransfer transfer = new JTreeTransfer();
        localJTree.setTransferHandler(transfer);
        localJTree.getSelectionModel().setSelectionMode(
               TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        
        //when node is selected from local JTree
        localJTree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath tp = localJTree.getSelectionPath();

                if (tp != null){
                    JTreeLoader localTreeLoader = new JTreeLoader();               
                    localDir = localTreeLoader.convertTreePathToFilePath(tp); // get then set selected node absolute filepath to localDir
                    System.out.println("test-localDir(selected-node): " + localDir);
                    
                    DefaultMutableTreeNode selectedNode = ((DefaultMutableTreeNode)tp.getLastPathComponent());
                    File selectedFile = (File)selectedNode.getUserObject();
                    
                    localFileName = selectedFile.getName(); // get then set selected node filename to localFileName
                    System.out.println("test-localFileName(selected-node): " + localFileName);
                }          
            }
        });
      
        //get destination path when file is drop 
        localJTree.getModel().addTreeModelListener(new TreeModelListener() {
            @Override 
            public void treeNodesChanged(TreeModelEvent e) {}
            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                System.out.println("A node is inserted");
                TreePath dest = e.getTreePath();
                //get parent node of the treenode that inserted
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
                System.out.println("dest parent (L): " + parent);
                DefaultMutableTreeNode insertedNode = null;
                
                //get children nodes from the parent node
                for (int i=0; i<parent.getChildCount(); i++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
                    //get inserted node 
                    if (child.toString().equals(remoteFileName)) {
                        insertedNode = child;
                    }                
                }
                
                // get parent file path of node dropped
                String tempdpath = dest.toString();
                tempdpath = tempdpath.substring(1,tempdpath.length()-1);
                
                StringBuilder sb = new StringBuilder();
                String[] tempNodes = tempdpath.split(", "); // split by , and a space

                for(int i=0; i<tempNodes.length;  i++) {
                    //this if-else is done to prevent adding '\' to the front of the path
                    if(i == 0){
                        sb.append(tempNodes[i]);
                    }else{
                        //exclude repeated root to be appended again
                        String tempStr = tempNodes[i].substring(tempNodes[i].lastIndexOf('\\'));
                        System.out.println("index after first test: " + tempStr);
                        tempStr = tempStr.substring(1); //remove '\'
                        sb.append(File.separatorChar).append(tempStr);
                    }
                    
                    //get fullpath after node is inserted
                    if(i == tempNodes.length-1 && insertedNode != null) {
                        //get dropped node name and append to string builder
                        sb.append(File.separatorChar).append(insertedNode.toString());
                    }
                } 
                
                //set destination path for file download (remote to local)
                //destDir = System.getProperty("user.home") + "/" + sb.toString();
                destDir = sb.toString() + File.separatorChar + remoteFileName;
                System.out.println("destDIR (L):" + destDir);
                //sftp retrieve file 
               if (SSHClientGui.sftpChannel != null){
                   SFTPUtil sftpUtil = new SFTPUtil();
                   sftpUtil.retrieveFile(SSHClientGui.sftpChannel, remoteFileName, destDir, remoteDir, sshClientG);
               }
            }
            @Override
            public void treeNodesRemoved(TreeModelEvent e) {}
            @Override
            public void treeStructureChanged(TreeModelEvent e) {
            }
        });
    }
    
    public void displayRemoteFileStruct() {
        String hostName = SSHClientGui.session.getUserName();
        
        //assuming that the remote host runs on unix or linux 
        String mainPath = String.format("/home/%s/Desktop",hostName);
        
        DefaultMutableTreeNode nroot = new DefaultMutableTreeNode(mainPath);
        if(SSHClientGui.sftpChannel != null){
            try {
                JTreeLoader remoteTreeLoader = new JTreeLoader();
                remoteTreeLoader.addNodesRemoteV2(mainPath, nroot, SSHClientGui.sftpChannel);
                
                
                //remoteTreeLoader.addNodesRemoteV3(mainPath, nroot, SSHClientGui.sftpChannel); <-- fix here
                
                
                
            } catch (SftpException ex) {
                Logger.getLogger(SFTPGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        DefaultTreeModel model = (DefaultTreeModel) remoteJTree.getModel();
        model.setRoot(nroot);
        model.reload();
        
        //set drag and drop 
        remoteJTree.setDragEnabled(true);
        remoteJTree.setDropMode(DropMode.ON_OR_INSERT);
        //remoteJTree.setCellRenderer(new RemoteFileTreeCellRenderer()); <-- fix here
        
        //transfer data from JTree
        JTreeTransfer transfer = new JTreeTransfer();
        remoteJTree.setTransferHandler(transfer);
        remoteJTree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        
        remoteJTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath tp = remoteJTree.getSelectionPath();
                System.out.println("from displayremote tp: " + tp);
                if (tp != null) {
                    Object [] filePathToAdd = tp.getPath();
                    String fullPath = "";
                    for(Object path : filePathToAdd) {
                        
                        /*
                        <-- fix here-->
                        if (path instanceof DefaultMutableTreeNode) {
                            path = ((DefaultMutableTreeNode)path).getUserObject();
                            if (path instanceof ChannelSftp.LsEntry) {
                                path = ((ChannelSftp.LsEntry) path).getFilename();
                            }
                        }
                        */
                        
                        fullPath = fullPath + "/" + String.valueOf(path);
                        
                    }
                    remoteDir = fullPath.substring(1, fullPath.length());
                    
                     //get name of selected file 
                    if(fullPath != null && !fullPath.isEmpty()) {
                        
                        String[] directories = fullPath.split("/");
                        remoteFileName = directories[directories.length-1];

                        System.out.println("file dir (R): " + remoteDir);
                        System.out.println("file name (R): " + remoteFileName);
                    }
                   
                }
            }
        });
        
        //get destination path when file is drop 
        remoteJTree.getModel().addTreeModelListener(new TreeModelListener() {
            @Override 
            public void treeNodesChanged(TreeModelEvent e) {}
            @Override
            public void treeNodesInserted(TreeModelEvent e) {
                TreePath dest = e.getTreePath();
                //get parent node of the treenode that inserted
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) dest.getLastPathComponent();
                DefaultMutableTreeNode insertedNode = null;
                
                //get children nodes from the parent node
                for (int i=0; i<parent.getChildCount(); i++) {
                    DefaultMutableTreeNode child = (DefaultMutableTreeNode) parent.getChildAt(i);
                    //get inserted node 
                    if (child.toString().equals(localFileName)) {
                        insertedNode = child;
                    }                
                }
                
                //get parent file path of node dropped
                String tempdpath = dest.toString().replaceAll("\\]| |\\[|", "");
                StringBuilder sb = new StringBuilder();
                

                //linux environment
                if (tempdpath.charAt(0) == '/') {
                    tempdpath = tempdpath.substring(1); //remove first '/'
                    tempdpath = tempdpath.replaceAll(" ", "");
                    tempdpath = tempdpath.replaceAll("/", ",");
                    String[] tempNodes = tempdpath.split(",");

                    for(int i=0; i<tempNodes.length;  i++) {
                        sb.append("/").append(tempNodes[i]);

                        //get fullpath after node is inserted
                        if(i == tempNodes.length-1 && insertedNode != null) {
                            //get dropped node name and append to string builder
                            sb.append("/").append(insertedNode.toString());
                        }
                    } 
                }
                
                //set destination path for file upload (local to remote)
                destDir = sb.toString();
                
                //sftp transfer file 
                if (SSHClientGui.sftpChannel != null){
                    SFTPUtil sftpUtil = new SFTPUtil();
                    sftpUtil.transferFile(SSHClientGui.sftpChannel, localFileName, localDir, destDir, sshClientG);
                }
                
            }
            @Override
            public void treeNodesRemoved(TreeModelEvent e) {}
            @Override
            public void treeStructureChanged(TreeModelEvent e) {}
            
        });
    }
    
    
    public static void displaySFTPGui(SSHClientGui sshClient) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SFTPGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SFTPGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SFTPGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SFTPGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                sshClientG = sshClient;
                
                SFTPGui sftpGui = new SFTPGui();
                sshClient.setSFTPGui(sftpGui);
                sshClient.setIsSFTPOn(true);
                
                sftpGui.setVisible(true);
                sftpGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                //after user click close button on the top right, sftp will be disconnected.
                sftpGui.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e){
                        if(SSHClientGui.sftpChannel.isConnected()){
                            SFTPUtil sftpUtil = new SFTPUtil();
                            sftpUtil.endSFTPChannel(SSHClientGui.sftpChannel, sshClientG);
                            sshClient.setIsSFTPOn(false);
                            System.out.println("SFTP Closed");
                        }
                    }
                    
                    @Override
                    public void windowClosed(WindowEvent e){
                        if(SSHClientGui.sftpChannel.isConnected()){
                            SFTPUtil sftpUtil = new SFTPUtil();
                            sftpUtil.endSFTPChannel(SSHClientGui.sftpChannel, sshClientG);
                            sshClient.setIsSFTPOn(false);
                            
                        }
                    }
                });
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton createButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree localJTree;
    private javax.swing.JTree remoteJTree;
    // End of variables declaration//GEN-END:variables
}