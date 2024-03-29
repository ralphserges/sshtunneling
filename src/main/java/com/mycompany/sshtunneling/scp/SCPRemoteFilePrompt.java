
package com.mycompany.sshtunneling.scp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.mycompany.sshtunneling.jtreedisplay.JTreeLoader;
import com.mycompany.sshtunneling.SSHClientGui;
import com.mycompany.sshtunneling.jtreedisplay.SCPRemoteJTreeLoader;
import com.mycompany.sshtunneling.sftp.SFTPUtil;
import java.util.Arrays;




public class SCPRemoteFilePrompt extends javax.swing.JFrame {
    
    private Session session;
    private SCPCommandLine terminal;
    private String selectedNodePath;
    private String selectedNodeName;
    private SCPUtil scpUtil;
    private boolean isFile;
    
    private static ChannelSftp channelSftp;

    public SCPRemoteFilePrompt(Session session, SCPCommandLine terminal,SCPUtil scpUtil) {
        initComponents();
        this.session = session;
        this.terminal = terminal;
        this.scpUtil = scpUtil;
        this.channelSftp = setChannelSftp();
        
        displayRemoteFileStruct();
        jTree1.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        SCPRemoteJTreeLoader scpremoteloader = new SCPRemoteJTreeLoader();
        jTree1.addTreeWillExpandListener(scpremoteloader);
        
        jTree1.addTreeSelectionListener(new TreeSelectionListener(){
            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) jTree1.getLastSelectedPathComponent();

                selectedNodeName = selectedNode.toString();
                
                TreePath tp = jTree1.getSelectionPath();
                if (tp != null) {
                    Object [] filePathToAdd = tp.getPath();
                    String fullPath = "";
                    
                    for(int i = 0; i < filePathToAdd.length; i++) {
                        fullPath = fullPath + "/" + String.valueOf(filePathToAdd[i]);             
                    }
                    selectedNodePath = fullPath.substring(1, fullPath.length());
                    
                    // if node is leaf AND isDirectory return false, selected node is a file 
                    if(selectedNode.isLeaf() && !isDirectory(filePathToAdd, selectedNodeName)) 
                        isFile = true;
                    else
                        isFile = false;
                }
            }
        });
    }
    
    public static ChannelSftp getChannelSftp() {
        return channelSftp;
    }
    
    private boolean isDirectory(Object [] filePathToAdd, String fileName){
        Object [] fileItems = Arrays.copyOf(filePathToAdd, filePathToAdd.length-1);
        String fullPath = "";
        for(int i = 0; i < fileItems.length; i++) {
            fullPath = fullPath + "/" + String.valueOf(fileItems[i]);             
        }
        
        if(this.channelSftp.isConnected()){
            SFTPUtil sftpUtil = new SFTPUtil();
            try {
                return sftpUtil.isDirSFTP(this.channelSftp, fullPath, fileName);
            } catch (SftpException ex) {
                terminal.getTerminal().append("[SCP_ERROR] " + ex.getMessage() + "\n");
                terminal.getSSHClientGui().writeToGuiConsole("[SCP_ERROR] " + ex.getMessage(), SSHClientGui.LEVEL_ERROR);
            }
        }
        return false;
    }
    
    private void displayRemoteFileStruct(){  
        String hostName = this.session.getUserName();

        //assuming that the remote host runs on unix or linux
        //here
        SFTPUtil sftpUtil = new SFTPUtil();
        String mainPath = sftpUtil.getPWD(SSHClientGui.session, null);
        //String mainPath = String.format("/home/%s/Desktop",hostName);

        DefaultMutableTreeNode nroot = new DefaultMutableTreeNode(mainPath);
        if(this.channelSftp.isConnected()){
            JTreeLoader remoteTreeLoader = new JTreeLoader();
            try {  
                remoteTreeLoader.addNodesRemoteSCP(mainPath, nroot, this.channelSftp);
            } catch (SftpException ex) {
                terminal.getTerminal().append("[SCP_ERROR] " + ex.getMessage() + "\n");
                terminal.getSSHClientGui().writeToGuiConsole("[SCP_ERROR] " + ex.getMessage(), SSHClientGui.LEVEL_ERROR);
            }
        }

        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.setAsksAllowsChildren(true);
        model.setRoot(nroot);
        model.reload();

    }
    
    private ChannelSftp setChannelSftp(){
        ChannelSftp sftpChannel = null;
        
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect(5000); // connect sftp to ssh server. time out 5 sec
        }catch(JSchException e) {
           terminal.getTerminal().append("[SCP_ERROR] " + e.getMessage() + "\n");
           terminal.getSSHClientGui().writeToGuiConsole("[SCP_ERROR] " + e.getMessage(), SSHClientGui.LEVEL_ERROR);
           return null;
        }
        return sftpChannel;
    }
    
    private void endSFTPChannel(){
        SFTPUtil sftpUtil = new SFTPUtil();
        sftpUtil.endSFTPChannel(this.channelSftp, terminal.getSSHClientGui());
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
        jTree1 = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        selectButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTree1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jScrollPane1.setViewportView(jTree1);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Remote Server Desktop");

        selectButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        exitButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectButton)
                    .addComponent(exitButton))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        setVisible(false);
        terminal.getTerminal().append("[SCP_INFO] " + "Remote Server Desktop display closed." + "\n");
        terminal.getTerminal().append(terminal.getPrompt());
        terminal.getSSHClientGui().writeToGuiConsole("[SCP_INFO] " + "Remote Server Desktop display closed.", SSHClientGui.LEVEL_INFO);
        dispose();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        if(!selectedNodePath.isEmpty()) {
            String message = String.format("Retrieve %s from remote server?",selectedNodeName);
            int reply = JOptionPane.showConfirmDialog(null, message, "Retrieve File from Remote Server", JOptionPane.YES_NO_OPTION);
            
            if(reply == JOptionPane.YES_OPTION) {
                try {
                    if(this.isFile)
                        scpUtil.downloadFile(selectedNodePath, new File(scpUtil.getLocalHomeDir()));
                    else
                        scpUtil.downloadDirectory(selectedNodePath, new File(scpUtil.getLocalHomeDir()));
                    
                    terminal.getTerminal().append(String.format("[SCP_INFO] " + "Retrieved %s from remote server to local desktop.\n",selectedNodeName));
                    terminal.getTerminal().append(terminal.getPrompt());
                    terminal.getSSHClientGui().writeToGuiConsole(String.format("[SCP_INFO] " + "Retrieved %s from remote server to local desktop.",selectedNodeName), 
                            SSHClientGui.LEVEL_INFO);
                    
                } catch (IOException | JSchException ex) {
                    terminal.getTerminal().append("[SCP_ERROR] " + ex.getMessage() + "\n");
                    terminal.getTerminal().append(terminal.getPrompt());
                    terminal.getSSHClientGui().writeToGuiConsole("[SCP_ERROR] " + ex.getMessage(), SSHClientGui.LEVEL_ERROR);
                }
            }
        }
    }//GEN-LAST:event_selectButtonActionPerformed

    
    public static void displaySCPRemoteFilePrompt(Session session, SCPCommandLine terminal, SCPUtil scpUtil) {
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
            java.util.logging.Logger.getLogger(SCPRemoteFilePrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SCPRemoteFilePrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SCPRemoteFilePrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SCPRemoteFilePrompt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SCPRemoteFilePrompt filePrompt = new SCPRemoteFilePrompt(session,terminal,scpUtil);
                
                terminal.setScpRemoteFileDisplay(filePrompt);
                terminal.setIsSCPRemoteFileON(true);
                
                
                filePrompt.setVisible(true);
                filePrompt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                filePrompt.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e){
                        
                        terminal.getTerminal().append("[SCP_INFO] " + "Remote Server Desktop display closed." + "\n");
                        terminal.getTerminal().append(terminal.getPrompt());
                        terminal.getSSHClientGui().writeToGuiConsole("[SCP_INFO] " + "Remote Server Desktop display closed.", SSHClientGui.LEVEL_INFO);
                        terminal.setIsSCPRemoteFileON(false);
                        filePrompt.endSFTPChannel();
                    }
                    
                    @Override
                    public void windowClosed(WindowEvent e){
                       
                        terminal.setIsSCPRemoteFileON(false);
                        filePrompt.endSFTPChannel();
                    }
                });
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton exitButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTree jTree1;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
}
