
package SSHTunnelingFYP;

import com.jcraft.jsch.JSchException;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class SCPCommandLine extends javax.swing.JFrame {
    private static final String SCP_PROMPT = "scp>> ";
    private static final String [] avaliableCommands = {"sendfile", "retrievefile", "remotelist"};
    private static SSHClientGui sshClientGui;
    
    /**
     * Creates new form SCPCommandLine
     */
    public SCPCommandLine() {
        initComponents();
        terminal.setLineWrap(true);
        terminal.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret)terminal.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        displayWelcomeBanner();
    }
    
    public JTextArea getTerminal() {
        return terminal;
    }
    
    public String getPrompt() {
        return SCP_PROMPT;
    }
    
    public SSHClientGui getSSHClientGui() {
        return sshClientGui;
    }
    
    
    private void displayWelcomeBanner() {
        terminal.append("Welcome to SCP CLI.\n");
        terminal.append("Avaliable commands description can be found on the right.\n");
        terminal.append(SCP_PROMPT);
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
        terminal = new javax.swing.JTextArea();
        enterCommandTextArea = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        commandList = new javax.swing.JTextArea();
        exitSCPButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        terminal.setEditable(false);
        terminal.setColumns(20);
        terminal.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        terminal.setRows(5);
        terminal.setToolTipText("");
        terminal.setWrapStyleWord(true);
        jScrollPane1.setViewportView(terminal);

        enterCommandTextArea.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        enterCommandTextArea.setToolTipText("Enter Command here");
        enterCommandTextArea.setActionCommand("<Not Set>");
        enterCommandTextArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                enterCommandTextAreaKeyPressed(evt);
            }
        });

        commandList.setEditable(false);
        commandList.setColumns(20);
        commandList.setFont(new java.awt.Font("Monospaced", 2, 12)); // NOI18N
        commandList.setRows(5);
        commandList.setText("SCP customized commands\n=======================\nsendfile -> send file from local to remote\nretrievefile -> retrieve file from remote to local\nremotelist -> list all files in remote Desktop");
        jScrollPane2.setViewportView(commandList);

        exitSCPButton.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        exitSCPButton.setText("Exit");
        exitSCPButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitSCPButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("SCP Command Line Interface");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Enter command here:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(22, 22, 22)
                                .addComponent(enterCommandTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 778, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(exitSCPButton, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)))))
                        .addGap(37, 37, 37))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(enterCommandTextArea, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(exitSCPButton)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void enterCommandTextAreaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_enterCommandTextAreaKeyPressed
        String commandEntered;
        
        if (evt.getKeyCode()==KeyEvent.VK_ENTER){
            commandEntered = enterCommandTextArea.getText().toLowerCase().replaceAll("\\s","");
            if(isCommandValid(commandEntered)){
                terminal.append(commandEntered + "\n");
                
                //scp moderator takes over from here
                //where it will execute whatever command user enter
                SCPModerator scp = new SCPModerator(SSHClientGui.session);
                try {
                    scp.executeCommand(commandEntered,this);

                } catch (JSchException | IOException | InterruptedException ex) {
                    terminal.append("[SCP_ERROR] " + ex.getMessage() + "\n");
                    this.sshClientGui.writeToGuiConsole("[SCP_ERROR] " + ex.getMessage(), SSHClientGui.LEVEL_ERROR);
                }
            }
            else {
                terminal.append("[SCP_ERROR] " + commandEntered + " is not a valid command. See right for avaliable commands\n");
                this.sshClientGui.writeToGuiConsole("[SCP_ERROR] " + commandEntered + " is not a valid command. See right for avaliable commands", SSHClientGui.LEVEL_ERROR);
            }
            terminal.append(SCP_PROMPT);
            enterCommandTextArea.setText("");
        }
    }//GEN-LAST:event_enterCommandTextAreaKeyPressed

    private void exitSCPButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitSCPButtonActionPerformed
        setVisible(false);
        this.sshClientGui.writeToGuiConsole("SCP channel is closed", SSHClientGui.LEVEL_INFO);
        dispose();
    }//GEN-LAST:event_exitSCPButtonActionPerformed
    
   
    private boolean isCommandValid(String command) {
        return Arrays.asList(avaliableCommands).contains(command);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void displaySCPCommandLine(SSHClientGui gui) {
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
            java.util.logging.Logger.getLogger(SCPCommandLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SCPCommandLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SCPCommandLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SCPCommandLine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SCPCommandLine.sshClientGui = gui;
                
                SCPCommandLine scp = new SCPCommandLine();
                scp.setVisible(true);
                scp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                scp.addWindowListener(new WindowAdapter(){
                    @Override
                    public void windowClosing(WindowEvent e){
                        
                        SCPCommandLine.sshClientGui.writeToGuiConsole("SCP channel is closed", SSHClientGui.LEVEL_INFO);
                    }
                });
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea commandList;
    private javax.swing.JTextField enterCommandTextArea;
    private javax.swing.JButton exitSCPButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea terminal;
    // End of variables declaration//GEN-END:variables
}