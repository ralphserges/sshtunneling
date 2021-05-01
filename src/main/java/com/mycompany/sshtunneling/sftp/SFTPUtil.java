package com.mycompany.sshtunneling.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.mycompany.sshtunneling.SSHClientGui;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;


// this class is still not in use yet. 
public class SFTPUtil {
    
    public SFTPUtil() {
        
    }
    
    
    //create sftp channel and connect to ssh server
    public ChannelSftp getSFTPChannel(Session session,SSHClientGui gui) {
        ChannelSftp sftpChannel = null;
        
        try {
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect(5000); // connect sftp to ssh server. time out 5 sec
        }catch(JSchException e) {
           gui.writeToGuiConsole(e.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
           return null;
        }
        
        if(gui != null) {
            String sftpMsg = "SFTP channel is created.";
            gui.writeToGuiConsole(sftpMsg, SSHClientGui.LEVEL_INFO);
        }
        
        return sftpChannel;
    }
    
    
    //transfer files from local to remote 
    public void transferFile(ChannelSftp sftp, String fileName ,String localDir, String remoteDir,SSHClientGui gui) {
         try {
            File fileInput = new File(localDir);
            
            //transfer a folder
            if (fileInput.isDirectory()) {
                System.out.println("test folder:" + localDir);
               
                //folder does not exist, creating folder at destination (remote directories)
                sftp.cd(remoteDir.substring(0, remoteDir.lastIndexOf('/')));
                sftp.mkdir(fileName);
                
		String newFileName;
                //recursively transfer all files in folder
                for(File file: fileInput.listFiles()){
                    newFileName = file.getName();
                    transferFile(sftp, newFileName,localDir + File.separatorChar + newFileName,remoteDir + "/" + newFileName, gui);
                }
                sftp.cd(remoteDir.substring(0, remoteDir.lastIndexOf('/')));
            }
            
            //transfer a file
            else {
                System.out.println("test file: " + localDir);
                sftp.put(localDir, remoteDir,ChannelSftp.OVERWRITE); 
            }
        } catch (SftpException ex) {
                gui.writeToGuiConsole("[SFTP_ERROR] " + ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);

        }

        String successTransfer = String.format("[SFTP_INFO] %s is transferred from %s to %s ", fileName,localDir,remoteDir);
        gui.writeToGuiConsole(successTransfer, SSHClientGui.LEVEL_INFO);
    }
    
    
    //retrieve files from remote to local
    public void retrieveFile(ChannelSftp sftp, String fileName,String localDir, String remoteDir,SSHClientGui gui) {
        try {
            Vector<ChannelSftp.LsEntry> entryVector = sftp.ls(remoteDir);
            File fileInput = new File(localDir);
            
            int size = entryVector.size();
            
            if (size == 1) { //retrieve a file
                System.out.println("test file: " + remoteDir);
                sftp.get(remoteDir, localDir);
            } 
            
            else if(size == 2) { //retrieve empty folder
                //create new directory
                System.out.println("test empty folder: " + remoteDir);
                fileInput.mkdir();
            }
           
            else {         
               
                System.out.println("test folder: " + remoteDir);
                //System.out.println("test dest folder: " + localDir.substring(0, localDir.lastIndexOf('\\')));
                sftp.lcd(fileInput.getParent()); //cd to local host directory
                //create new directory
                fileInput.mkdir();
                
                String fName;
                //recursively retrieve all files in selected folder
                for (ChannelSftp.LsEntry entry : entryVector) {
                    fName = entry.getFilename();
                    if (!".".equals(fName) && !"..".equals(fName)) {
                        retrieveFile(sftp, fName,localDir + File.separatorChar + fName,remoteDir 
                                + "/" + fName, gui);
                    }
                }
                
                sftp.lcd(fileInput.getParent());
            }
            
        } catch (SftpException ex) {
            gui.writeToGuiConsole("[SFTP_ERROR] " + ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
        }
        
        String successRetrieval = String.format("[SFTP_INFO] %s is retrieved from %s to %s ", fileName,remoteDir, localDir);
        gui.writeToGuiConsole(successRetrieval, SSHClientGui.LEVEL_INFO);
    }
    
    //remove files from remote
    public void removeFile(ChannelSftp sftp, String fileName,String remoteDir,SSHClientGui gui) {
        try {
            Vector<ChannelSftp.LsEntry> entryVector = sftp.ls(remoteDir);
            
            if (null == entryVector) {
                return;
            }
            int size = entryVector.size();
            
            if (size == 1) {
                // delete a file
                sftp.rm(remoteDir);
            } else if (size == 2) {
                // delete empty folder 
                sftp.rmdir(remoteDir);
            } else {
                // delete folder and its file
                String fName;
                //recursively delete all files in folder
                for (ChannelSftp.LsEntry entry : entryVector) {
                    fName = entry.getFilename();
                    if (!".".equals(fName) && !"..".equals(fName)) {
                        removeFile(sftp, fName, remoteDir + "/" + fName, gui);
                    }
                }
                sftp.rmdir(remoteDir);
            }
        
        } catch (SftpException ex) {
            gui.writeToGuiConsole("[SFTP_ERROR] " + ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
        }
        
        String successRemoval = String.format("[SFTP_INFO] %s is removed from %s", fileName,remoteDir );
        gui.writeToGuiConsole(successRemoval, SSHClientGui.LEVEL_INFO);
    }
        
    //create new folder from remote
    public void createFolder(ChannelSftp sftp, String newFolderName,String remoteDir,SSHClientGui gui) {
        String successCreate = "";
        
        try {
            Vector<ChannelSftp.LsEntry> entryVector = sftp.ls(remoteDir);
            int size = entryVector.size();
                   
            if (null == entryVector) {
                return;
            }
            
            //selected file is not a folder, new folder will be created at parent
            if (size == 1) {
                String parentRemoteDir = remoteDir.substring(0, remoteDir.lastIndexOf('/'));
                //list selected remoteDir parent
                Vector<ChannelSftp.LsEntry> parentEntryVect = sftp.ls(parentRemoteDir);
                boolean folderExist = false;
                
                //validate
                for (ChannelSftp.LsEntry entry : parentEntryVect) {
                    String fileName = entry.getFilename();
                    //check if folder already exist
                    if (newFolderName.equalsIgnoreCase(fileName)) {
                        folderExist = true;
                        
                        JOptionPane.showMessageDialog(null, 
                            "Folder name already exist, please input another name",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        String failCreate = String.format("[SFTP_ERROR] %s already exist at %s, duplicate folder name"
                                , newFolderName, parentRemoteDir);
                        gui.writeToGuiConsole(failCreate, SSHClientGui.LEVEL_ERROR);
                        
                    }                
                }
                
                //new folder does not exist in the directory
                if (folderExist == false) {
                    //cd to selected destination directories parent (remote directories)
                    sftp.cd(parentRemoteDir);
                    //create new directory at parent folder
                    sftp.mkdir(newFolderName);
                
                    successCreate = String.format("[SFTP_INFO] New folder %s is created on %s", newFolderName,parentRemoteDir );
                    gui.writeToGuiConsole(successCreate, SSHClientGui.LEVEL_INFO);
                }               
            }
            
            //selected file is a folder, new folder will be created inside
            else {
                boolean folderExist = false;
                
                //validate
                for (ChannelSftp.LsEntry entry : entryVector) {
                    String fileName = entry.getFilename();
                    //check if folder already exist
                    if (newFolderName.equalsIgnoreCase(fileName)) {
                        folderExist = true;
                        
                        JOptionPane.showMessageDialog(null, 
                            "Folder name already exist, please input another name",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        String failCreate = String.format("[SFTP_ERROR] %s already exist at %s, duplicate folder name"
                                , newFolderName, remoteDir);
                        gui.writeToGuiConsole(failCreate, SSHClientGui.LEVEL_ERROR);
                    }                
                }
                
                //new folder does not exist in the directory
                if (folderExist == false) {
                    //cd to selected destination directories (remote directories)
                    sftp.cd(remoteDir);
                    //create directory
                    sftp.mkdir(newFolderName);
                    successCreate = String.format("[SFTP_INFO] New folder %s is created on %s", newFolderName,remoteDir);
                    gui.writeToGuiConsole(successCreate, SSHClientGui.LEVEL_INFO);
                } 
            }
            
        } catch (SftpException ex) {
            gui.writeToGuiConsole("[SFTP_ERROR] " + ex.getCause().getMessage(), SSHClientGui.LEVEL_ERROR);
        }
        
    }
  
    public boolean isDirSFTP(ChannelSftp sftp, String path, String fileName) throws SftpException {
        Vector<ChannelSftp.LsEntry> vector = sftp.ls(path);
        List<String> fileList = new ArrayList<String>(); 
        
        for (ChannelSftp.LsEntry entry : vector) {
            fileList.add(entry.getFilename());
            //System.out.println("testing123 " + entry);
        }
        
        int selectedFileIndex = fileList.indexOf(fileName);
        if(selectedFileIndex != -1) {
            ChannelSftp.LsEntry selectedFile = vector.get(selectedFileIndex);
            return selectedFile.getAttrs().isDir();
        }
        return false; 
        
    }

    
    public void endSFTPChannel(ChannelSftp sftp,SSHClientGui gui){
        //if sftp channel is created
        if(sftp != null){
            if(sftp.isConnected()) {
                sftp.exit();
                if(gui != null){
                    String endSFTP = "SFTP channel is closed.";
                    gui.writeToGuiConsole(endSFTP, SSHClientGui.LEVEL_INFO);
                }
            }
        }
    }
}
