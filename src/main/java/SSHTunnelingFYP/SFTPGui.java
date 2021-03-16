
package SSHTunnelingFYP;

import com.jcraft.jsch.SftpException;
import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DropMode;
import javax.swing.JFrame;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class SFTPGui extends javax.swing.JFrame {
    
    
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(remoteJTree);

        jScrollPane2.setViewportView(localJTree);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Remote File Structure");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel2.setText("Local File Structure");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel3.setText("SFTP File Transfer (Drag and Drop)");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
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
                        .addGap(50, 50, 50)))
                .addGap(14, 14, 14))
            .addGroup(layout.createSequentialGroup()
                .addGap(184, 184, 184)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(68, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void displayLocalFileStructure() {
        
        DefaultTreeModel model = (DefaultTreeModel) localJTree.getModel();
        
        // display all files at client desktop 
        DefaultMutableTreeNode root = FileNodeStructure.addNodesLocal(null, new File(System.getProperty("user.home")+ "/Desktop"));
        
        model.setRoot(root);
        model.reload();
        
        localJTree.setDragEnabled(true);
        localJTree.setDropMode(DropMode.ON_OR_INSERT);
        
        
        localJTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath tp = localJTree.getSelectionPath();
                if (tp != null) {
                    Object [] filePathToAdd = tp.getPath();
                    String fullPath = "";
                    for(Object path : filePathToAdd) {
                        fullPath = fullPath + "/" + String.valueOf(path);
                    }
                    fullPath = fullPath.substring(1, fullPath.length());
                    
                    
                    //assuming that the client host runs on windows
                    // have not tested on linux environment
                    fullPath = System.getProperty("user.home") + "/" + fullPath;
                    fullPath = fullPath.replaceAll("C:", "");
                    fullPath = Paths.get(fullPath).toString();
                    System.out.println(fullPath);
                }
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
                
                FileNodeStructure.addNodesRemote(mainPath,nroot,SSHClientGui.sftpChannel);
                
            } catch (SftpException ex) {
                Logger.getLogger(SFTPGui.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        DefaultTreeModel model = (DefaultTreeModel) remoteJTree.getModel();
        model.setRoot(nroot);
        model.reload();
        
        remoteJTree.setDragEnabled(true);
        remoteJTree.setDropMode(DropMode.ON_OR_INSERT);
        
        remoteJTree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath tp = remoteJTree.getSelectionPath();
                if (tp != null) {
                    Object [] filePathToAdd = tp.getPath();
                    String fullPath = "";
                    for(Object path : filePathToAdd) {
                        fullPath = fullPath + "/" + String.valueOf(path);
                    }
                    System.out.println(fullPath.substring(1, fullPath.length()));
                }
            }
        });
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void displaySFTPGui() {
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
                SFTPGui sftpGui = new SFTPGui();
                sftpGui.setVisible(true);
                sftpGui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTree localJTree;
    private javax.swing.JTree remoteJTree;
    // End of variables declaration//GEN-END:variables
}
