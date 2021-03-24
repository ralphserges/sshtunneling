package SSHTunnelingFYP;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SSHClient {
    public static final int SSH_CLIENT_PORT = 1111; // ssh client listens on port 1111 as default
    
    
    public SSHClient(){
    
    }
   
    // username, password, ipaddress, serverport and sshserverport are to be provided by user
    // this function is for local port forwarding
    public static Session getSSHSessionLPF(String username, String password, String ipAddress, int serverPort, int sshServerPort, SSHClientGui gui) {
        Session session = null;
        
        try {
            session = new JSch().getSession(username, ipAddress, sshServerPort);
            session.setPassword(password);
            
            // check if known host list contain host key
            // if not, add host key to known host list
            session.setConfig("StrictHostKeyChecking", "no");
           
            gui.writeToGuiConsole("Connecting to SSH Server on port " + sshServerPort, SSHClientGui.LEVEL_INFO);
            session.connect(10000); // connect to ssh server, 10 sec timeout
            
            
            String successConnectMsg = String.format("SSH session is successfully created and connected to SSH server of remote host name %s", session.getHost());
            gui.writeToGuiConsole(successConnectMsg, SSHClientGui.LEVEL_INFO); // session created, print relevant message to console
            
            
            //display cipher information
            displayCipherInfo(session, gui);
            
            
            if(serverPort != 0) {
         
                session.setPortForwardingL(SSH_CLIENT_PORT, ipAddress, serverPort); // set ssh local port forwording
                String connectingMsg = String.format("Creating local port forwarding tunneling to server running on port %d...",serverPort);
                gui.writeToGuiConsole(connectingMsg, SSHClientGui.LEVEL_INFO);

                String lpfSuccessMsg = String.format("Local port forwarding set up successfully. SSHClient listening on port %d", SSH_CLIENT_PORT);
                gui.writeToGuiConsole(lpfSuccessMsg, SSHClientGui.LEVEL_INFO);
                
                
            }else {
            
                // no local port forwarding feature
                String noLPFMsg = "No local port forwarding tunneling initiated";
                gui.writeToGuiConsole(noLPFMsg, SSHClientGui.LEVEL_INFO);
            }
            
        } catch (JSchException ex) { 

           // session creation fail print error to console write error cause to gui
           gui.writeToGuiConsole("Unable to connect to SSH server. Please Check input fields.", SSHClientGui.LEVEL_ERROR);
           return null;
        }
        
        
<<<<<<< HEAD
=======
        if(serverPort != 0) {
            //local port forwarding success
            String lpfSuccessMsg = String.format("Local port forwarding set up successfully. SSHClient listening on port %d", serverPort);
            gui.writeToGuiConsole(lpfSuccessMsg, SSHClientGui.LEVEL_INFO);
        }else {
            
            // no local port forwarding feature
            String noLPFMsg = "No local port forwarding tunneling initiated";
            gui.writeToGuiConsole(noLPFMsg, SSHClientGui.LEVEL_INFO);
        }
        
        
>>>>>>> e774a93758d7ee6a1782acdcba22b5395de85def
        //change connect button to disconnect button
        return session;
    }
    
    //display all cipher related info to console
    private static void displayCipherInfo(Session session, SSHClientGui gui) {
        String keyExchangeUsed = String.format("Key exchange algorithm used: %s",session.getConfig("kex"));
        String s2cCipherUsed  = String.format("encryption algorithms used for server-to-client transport: %s",session.getConfig("cipher.s2c"));
        String c2sCipherUsed  = String.format("encryption algorithms used for client-to-server transport: %s", session.getConfig("cipher.c2s"));
        
        gui.writeToGuiConsole(keyExchangeUsed, SSHClientGui.LEVEL_INFO);
        gui.writeToGuiConsole(s2cCipherUsed, SSHClientGui.LEVEL_INFO);
        gui.writeToGuiConsole(c2sCipherUsed, SSHClientGui.LEVEL_INFO);
    }
    
    //create sftp channel and connect to ssh server
    public static ChannelSftp getSFTPChannel(Session session,SSHClientGui gui) {
        ChannelSftp sftpChannel = null;
        
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect(5000); // connect sftp to ssh server. time out 5 sec
        }catch(JSchException e) {
           gui.writeToGuiConsole(e.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
           return null;
        }
        
        String sftpMsg = "SFTP channel is created.";
        gui.writeToGuiConsole(sftpMsg, SSHClientGui.LEVEL_INFO);
        
        return sftpChannel;
    }
    
    
    //transfer files from local to remote 
    public static void transferFile(ChannelSftp sftp, String fileName ,String localDir, String remoteDir,SSHClientGui gui) {
        try {
            sftp.put(localDir, remoteDir);
        } catch (SftpException ex) {
            gui.writeToGuiConsole(ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
        }
        
        String successTransfer = String.format("%s is transferred from %s to %s ", fileName,localDir,remoteDir);
        gui.writeToGuiConsole(successTransfer, SSHClientGui.LEVEL_INFO);
    }
    
    
    //retrieve files from remote to local
    public static void retrieveFile(ChannelSftp sftp, String fileName,String localDir, String remoteDir,SSHClientGui gui) {
        try {
            sftp.get(localDir, remoteDir);
        } catch (SftpException ex) {
            gui.writeToGuiConsole(ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
        }
        
        String successRetrieval = String.format("%s is retrieved from %s to %s ", fileName,remoteDir, localDir);
        gui.writeToGuiConsole(successRetrieval, SSHClientGui.LEVEL_INFO);
    }
    
    // pass in textarea object for printing messages
    // when user select disconnect or exit button
    public static void endSSHSession(Session session,ChannelSftp sftp,SSHClientGui gui) {
        //display disconnect msg
        String endTunnelMsg = String.format("Local port forwarding tunnel is closed. SSH client no longer listens on %d",SSH_CLIENT_PORT);
        gui.writeToGuiConsole(endTunnelMsg, SSHClientGui.LEVEL_INFO);
        
        sftp.exit();
        String endSFTP = "SFTP channel is closed.";
        gui.writeToGuiConsole(endSFTP, SSHClientGui.LEVEL_INFO);
        
        String endSessionMsg = "SSH client is disconnected from SSH server";
        gui.writeToGuiConsole(endSessionMsg, SSHClientGui.LEVEL_INFO);
        
        session.disconnect();
    }
}