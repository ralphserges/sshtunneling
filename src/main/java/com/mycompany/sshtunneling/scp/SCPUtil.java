
package com.mycompany.sshtunneling.scp;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.tools.ant.taskdefs.optional.ssh.ScpToMessage;
import org.apache.tools.ant.taskdefs.optional.ssh.ScpFromMessage;

public class SCPUtil {
    private Session session;
    private String localHomeDir;
    //private String hostName;
    
    
    private static final int SHORT_WAIT_MSEC = 100;
    
    public SCPUtil(Session session) {
        this.session = session;
        this.localHomeDir = System.getProperty("user.home") + "/Desktop";
        //this.hostName = session.getUserName() + "@" + session.getHost() + ":"; //hostName format is as such remote_username@10.10.0.2:
        
    }
    
    public String getLocalHomeDir(){
        return localHomeDir;
    }
    
     private Channel getExecChannel() throws JSchException {
        if(session.isConnected()){
            return session.openChannel("exec");
        }      
        return null;
    }
    
    //retrieve one file from remote to local 
    public void downloadFile(String remotePath, File localFile) throws IOException, JSchException {
        // "false" = not recursive
        ScpFromMessage message = new ScpFromMessage(this.session, remotePath, localFile, false);
        message.execute();
    }
    
    //retrieve a folder from remote to local
    public void downloadDirectory(String remotePath, File localDir) throws IOException, JSchException {
        // "true" = recursive
        ScpFromMessage message = new ScpFromMessage(this.session, remotePath, localDir, true);
        message.execute();
    }
     
    // this will execute ls command, display to scp cmd and then return existing files in remote desktop
    public void lsCommand(List<String> fileList) throws JSchException, IOException {
        Channel channel = getExecChannel();
        String command = "ls Desktop";
        ((ChannelExec)channel).setCommand(command);
        channel.setInputStream(null); 
        
        ((ChannelExec)channel).setErrStream(System.err); 
        
        InputStream in=channel.getInputStream(); 
        channel.connect();

        byte[] tmp=new byte[1024];
        while(true){
          while(in.available()>0){
            int i=in.read(tmp, 0, 1024);
            if(i<0)break;
            //System.out.print(new String(tmp, 0, i));
            fileList.add(new String(tmp,0,i));
          }
          
          if(channel.isClosed()){
            if(in.available()>0) continue; 
            System.out.println("exit-status: "+channel.getExitStatus());
            break;
          }
          try{Thread.sleep(1000);}catch(Exception ee){}
        }
        channel.disconnect();
    }
    
    
    //send a file from local to remote
    public void uploadFile(File fileToSend, String remotePath) throws IOException, JSchException{
        ScpToMessage msg = new ScpToMessage(this.session,fileToSend,remotePath);
        msg.execute();
    }
    
    //send a directory from local to remote 
    public void uploadDirectory(File directory, String remotePath) throws JSchException, InterruptedException, IOException{
       remotePath = remotePath + "/" + directory.getName();
       Channel channel = getExecChannel();
       ((ChannelExec)channel).setCommand("mkdir -p " + remotePath);
       channel.connect();
       
       
        while (!channel.isClosed()) {
            // dir creation is usually fast, so only wait for a short time
            Thread.sleep(SHORT_WAIT_MSEC);
        }
        
        channel.disconnect();
        
        if (channel.getExitStatus() != 0) {
            throw new IOException("Creating directory failed: "  + remotePath);
        }
        
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                uploadDirectory(file, remotePath);
            } else {
                uploadFile(file, remotePath);
            }
        }
    }
}
