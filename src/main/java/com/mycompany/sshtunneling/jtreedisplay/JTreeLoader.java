
package com.mycompany.sshtunneling.jtreedisplay;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;


public class JTreeLoader implements TreeWillExpandListener {
    
    private static Comparator comparator;
    private static Comparator remoteTreeComparator;
   
    
    
    public JTreeLoader(){
        this.comparator = new Comparator() {
            @Override
            public int compare(Object arg0, Object arg1) {
                File f1 = (File) arg0;
                File f2 = (File) arg1;
                
                if(f1.isDirectory() && !f2.isDirectory())
                    return -1;
                else if (!f1.isDirectory() && f2.isDirectory())
                    return 1;
                else
                    return f1.compareTo(f2);
            }
        };
        
        this.remoteTreeComparator = new Comparator(){
            @Override
            public int compare(Object arg0, Object arg1) {
                ChannelSftp.LsEntry entry1 = (ChannelSftp.LsEntry)arg0;
                ChannelSftp.LsEntry entry2 = (ChannelSftp.LsEntry)arg1;
                
                if(entry1.getAttrs().isDir() && !entry2.getAttrs().isDir())
                    return -1;
                else if(!entry1.getAttrs().isDir() && entry2.getAttrs().isDir())
                    return 1;
                else 
                    return entry1.compareTo(entry2);
            }
            
        };
    }
    
    public void loadJTree(File filePath,DefaultMutableTreeNode root) {
        if(filePath.isDirectory()) {
            File [] subFilesList = filePath.listFiles();
            Arrays.sort(subFilesList,comparator);
           
            for(File file : subFilesList) {
                if(!file.getName().startsWith(".")){
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
                    //DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.getName());
                    root.add(child);                
                }
            }
        }
    }
    
    public DefaultMutableTreeNode createNodes(String rootFileName) {
        File rootFile = new File(rootFileName);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile) ;
        //DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootFile.getName()) ;
          
        if(rootFile.isDirectory()) {
            File [] subFilesList = rootFile.listFiles();
            Arrays.sort(subFilesList,comparator);
           
            for(File file : subFilesList) {
                if(!file.getName().startsWith(".")){
                    
                    DefaultMutableTreeNode child = new DefaultMutableTreeNode(file);
                    //DefaultMutableTreeNode child = new DefaultMutableTreeNode(file.getName());
                    root.add(child);
                }
            }
        }
        return root;
    }

    @Override
    public void treeWillExpand(TreeExpansionEvent path) throws ExpandVetoException {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getPath().getLastPathComponent();
        //System.out.println("SelectedNode treewillexpand: " + selectedNode.toString());
        File selectedFile = new File(convertTreePathToFilePath(path.getPath()));
        //System.out.println("SelectedFile treewillexpand: " + selectedFile);
        
        loadJTree(selectedFile, selectedNode);
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent arg0) throws ExpandVetoException {}
    
    
    
    public String convertTreePathToFilePath(TreePath path) {
        String absolutePath = Arrays.toString(path.getPath());
        StringBuilder sb = new StringBuilder(absolutePath);
        
        //delete open and close bracket 
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        
        String [] fileObjsPath = sb.toString().split(", ");
        
        return fileObjsPath[fileObjsPath.length-1];
    }
    
    public void addNodesRemoteV2(String remotePath, DefaultMutableTreeNode parent, ChannelSftp sftpChannel) throws SftpException{
        Pattern regex = Pattern.compile("[.]");
        
        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(remotePath); // List source directory structure.
        Collections.sort(list,remoteTreeComparator);
        
        for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the list to get file/folder names.       
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(oListItem.getFilename(), true);
            if (!oListItem.getAttrs().isDir()) { // If it is a file (not a directory).
                node.setAllowsChildren(false);
                parent.add(node); // add as a child node
            } else{
                if (!".".equals(oListItem.getFilename()) && 
                        !"..".equals(oListItem.getFilename()) &&
                        !regex.matcher(oListItem.getFilename()).find()) {
                    parent.add(node); // add as a child node
                    //addNodesRemoteV2(remotePath + "/" + oListItem.getFilename(), node,sftpChannel); // call again for the subdirectory
                }
            }
        }
    }
    
    public void addNodesRemoteSCP(String remotePath, DefaultMutableTreeNode parent, ChannelSftp sftpChannel) throws SftpException{
        
        Pattern regex = Pattern.compile("[$&+,:;=\\\\?@#|/'<>.^*()%!~`\\s]");
        
        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(remotePath); // List source directory structure.
        Collections.sort(list,remoteTreeComparator);
        
        for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the list to get file/folder names.       
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(oListItem.getFilename(), true);
            if (!oListItem.getAttrs().isDir()) { // If it is a file (not a directory).
                node.setAllowsChildren(false);
                parent.add(node); // add as a child node
            } else{
                if (!".".equals(oListItem.getFilename()) && 
                        !"..".equals(oListItem.getFilename()) && 
                        !regex.matcher(oListItem.getFilename()).find()) {
                    parent.add(node); // add as a child node
                    //addNodesRemoteV2(remotePath + "/" + oListItem.getFilename(), node,sftpChannel); // call again for the subdirectory
                }
            }
        }
    }
}
