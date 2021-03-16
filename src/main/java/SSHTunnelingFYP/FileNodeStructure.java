
package SSHTunnelingFYP;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileNodeStructure {
    
    public static DefaultMutableTreeNode addNodesLocal(DefaultMutableTreeNode currentNode, File dir) {
        String currentPath = dir.getPath(); 
        
        //create a node that contains the file name
        DefaultMutableTreeNode currentDir = new DefaultMutableTreeNode(dir.getName());
        
        
        if(currentNode != null) {

            currentNode.add(currentDir);
        }
        
        Vector ol = new Vector();
        
        //store the list of file and dir names in the current path
        String [] tmp = dir.list();
        
        //add all files and dir names to Vector
        for(int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        
        // sort file and dir names 
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        
        
        File f; 
        Vector files = new Vector();
        for(int i = 0; i< ol.size(); i++){
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            
            // thisObject is at Desktop
            if(currentPath.equals(System.getProperty("user.home") + "/Desktop")) {
                newPath = thisObject;
            }
            
            // thisObject is not at Desktop
            else {
                newPath = currentPath + File.separator + thisObject;
            }
            
            
            // if thisobject is a directory
            if((f = new File(newPath)).isDirectory()) {
                
                addNodesLocal(currentDir,f); //explore the directory, add directory to parent node
            }
            else {
                //add thisobject to files
                files.addElement(thisObject);
            }
            
        }
       
        //under a parent node, add all child node of file type to it 
        for(int i = 0; i < files.size(); i++) {
            currentDir.add(new DefaultMutableTreeNode(files.elementAt(i)));
        }
        return currentDir;
    }
    
    
    public static void addNodesRemote(String remotePath, DefaultMutableTreeNode parent, ChannelSftp sftpChannel) throws SftpException{
        Vector<LsEntry> list = sftpChannel.ls(remotePath); // List source directory structure.
        for (LsEntry oListItem : list) { // Iterate objects in the list to get file/folder names.       
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(oListItem.getFilename());
            if (!oListItem.getAttrs().isDir()) { // If it is a file (not a directory).
                parent.add(node); // add as a child node
            } else{
                if (!".".equals(oListItem.getFilename()) && !"..".equals(oListItem.getFilename())) {
                    parent.add(node); // add as a child node
                    addNodesRemote(remotePath + "/" + oListItem.getFilename(), node,sftpChannel); // call again for the subdirectory
                }
            }
        }
    }
}
