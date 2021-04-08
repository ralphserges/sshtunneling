
package SSHTunnelingFYP;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileNodeStructure {
    
    public static void addNodesLocalV2(File fileRoot, DefaultMutableTreeNode parent) {
        File [] files = fileRoot.listFiles();
        if(files == null)
            return;
        
        for(File file : files) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(file.getName());
            
            //ignore all files that starts with '.'
            if(!file.getName().startsWith(".")) {
                parent.add(childNode);
                if(file.isDirectory()) {
                    addNodesLocalV2(file, childNode);
                }
            }
        }
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
