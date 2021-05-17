package com.mycompany.sshtunneling.scp;

import com.mycompany.sshtunneling.SSHClientGui;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.mycompany.sshtunneling.sftp.SFTPUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;



public class SCPModerator {
    
    private SCPUtil scpUtil;
    private String remoteHomeDir;
    
    private Session session;
    private List<String> mainFileList = new ArrayList<String>();;

    public SCPModerator(Session session) {
        this.session = session;
        this.scpUtil = new SCPUtil(session);
        
        SFTPUtil sftpUtil = new SFTPUtil();
        
        this.remoteHomeDir = sftpUtil.getPWD(SSHClientGui.session, null);
        //this.remoteHomeDir = String.format("/home/%s/Desktop", session.getUserName());
        
    }
    
    public void executeCommand(String command, SCPCommandLine terminal) throws JSchException, IOException, InterruptedException {
        switch(command) {
            case "sendfile":
                sendFile(terminal);
                break;
            case "retrievefile":
                retrieveFile(terminal);
                break;
            case "remotelist":
                remoteList(terminal);
                break;
        }
    }
    
    private void retrieveFile(SCPCommandLine terminal) {
        if(!terminal.getIsSCPRemoteFileOn())
            SCPRemoteFilePrompt.displaySCPRemoteFilePrompt(this.session, terminal,this.scpUtil);
        
    }
    
    private void sendFile(SCPCommandLine terminal) throws JSchException, IOException, InterruptedException{
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        int result = fileChooser.showOpenDialog(terminal);

        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            
            String message = String.format("Send %s to remote server?",selectedFile.getName());
            int reply = JOptionPane.showConfirmDialog(null, message, "Send File to Remote Server", JOptionPane.YES_NO_OPTION);
            
            if(reply == JOptionPane.YES_OPTION){
                
               if(selectedFile.isDirectory())
                   scpUtil.uploadDirectory(selectedFile, remoteHomeDir);
               
               else
                   scpUtil.uploadFile(selectedFile, remoteHomeDir);
               
               terminal.getTerminal().append(String.format("[SCP_INFO] %s has been successfully copied to remote server\n", selectedFile.getName()));
               terminal.getSSHClientGui().writeToGuiConsole(String.format("[SCP_INFO] %s has been successfully copied to remote server.", selectedFile.getName()), 
                       SSHClientGui.LEVEL_INFO);
            }
        }
    }
    
    private void remoteList(SCPCommandLine terminal) throws JSchException, IOException{
        this.scpUtil.lsCommand(mainFileList);
        
        if(!mainFileList.isEmpty()){
            for(String file : mainFileList){
                terminal.getTerminal().append(file);
                terminal.getTerminal().append("\n");
            }
        }
         System.out.println(mainFileList.size());
    }
}
