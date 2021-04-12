package SSHTunnelingFYP;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class SCPModerator {
    private Session session;
    private String hostName;
    
    private List<String> fileList = new ArrayList<String>();

    public SCPModerator(Session session) {
        this.session = session;
        this.hostName = session.getUserName() + "@" + session.getHost() + ":"; //hostName format is as such remote_username@10.10.0.2:
        System.out.println(this.hostName);
    }
    
    public void executeCommand(String command) throws JSchException, IOException {
        switch(command) {
            case "sendfile":
                System.out.println("sendfile");
                break;
            case "retrievefile":
                //remoteList();
                System.out.println("retrievefile");
                break;
            case "remotelist":
                remoteList();
                break;
        }
    }
    
    public List<String> getExistingFiles() {
        return fileList;
    }
    
    private Channel getExecChannel() throws JSchException {
        if(session.isConnected()){
            return session.openChannel("exec");
        }      
        return null;
    }
    
    private boolean isFileExist(String fileName) {
        if(!fileList.isEmpty())
            return fileList.contains(fileName);
        return false;
    }
        
    // this will execute ls command, display to scp cmd and then return existing files in remote desktop
    private void remoteList() throws JSchException, IOException {
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
}
