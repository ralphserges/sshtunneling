package SSHTunnelingFYP;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


public class SSHClient {
    public static final int SSH_CLIENT_PORT = 1111; // ssh client listens on port 1111 as default
    
    public SSHClient(){
    
    }
   
    // username, password, ipaddress, serverport and sshserverport are to be provided by user
    // this function is for local port forwarding
    public static Session getSSHSessionLPF(String username, String password, String ipAddress, int serverPort, int sshServerPort, SSHClientGui gui) {
        Session session = null;
        String [] messages = null;
        try {
            session = new JSch().getSession(username, ipAddress, sshServerPort);
            session.setPassword(password);
            
            // check if known host list contain host key
            // if not, add host key to known host list
            session.setConfig("StrictHostKeyChecking", "no");
            
            gui.writeToGuiConsole("Connecting to SSH Server on port " + sshServerPort, SSHClientGui.LEVEL_INFO);
            session.connect(30000); // connect to ssh server, 30 sec timeout
            
            gui.writeToGuiConsole("SSH session is successfully created and connected to SSH server", SSHClientGui.LEVEL_INFO); // session created, print relevant message to console
            
            
            if(serverPort != 0) {
                session.setPortForwardingL(SSH_CLIENT_PORT, ipAddress, serverPort); // set ssh local port forwording
                String connectingMsg = String.format("Creating local port forwarding tunneling to server running on port %d...",serverPort);
                gui.writeToGuiConsole(connectingMsg, SSHClientGui.LEVEL_INFO);
            }
            
        } catch (JSchException ex) { 

           // session creation fail print error to console write error cause to gui
           gui.writeToGuiConsole(ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
           return null;
        }
        
        
        if(serverPort != 0) {
            //local port forwarding success
            String lpfSuccessMsg = String.format("Local port forwarding set up successfully. SSHClient listening on port %d", SSH_CLIENT_PORT);
            gui.writeToGuiConsole(lpfSuccessMsg, SSHClientGui.LEVEL_INFO);
        }else {
            
            // no local port forwarding feature
            String noLPFMsg = "No local port forwarding tunneling initiated";
            gui.writeToGuiConsole(noLPFMsg, SSHClientGui.LEVEL_INFO);
        }
        
        
        //change connect button to disconnect button
        return session;
    }
    
    // pass in textarea object for printing messages
    // when user select disconnect or exit button
    public static void endSSHSession(Session session,SSHClientGui gui) {
        //display disconnect msg
        String endTunnelMsg = String.format("Local port forwarding tunnel closed. SSH client no longer listens on %d",SSH_CLIENT_PORT);
        gui.writeToGuiConsole(endTunnelMsg, SSHClientGui.LEVEL_INFO);
        
        String endSessionMsg = "SSH client is disconnected from SSH server";
        gui.writeToGuiConsole(endSessionMsg, SSHClientGui.LEVEL_INFO);
        
        session.disconnect();
    }
}