/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sshtunneling.jtreedisplay;

import com.jcraft.jsch.SftpException;
import com.mycompany.sshtunneling.scp.SCPRemoteFilePrompt;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/**
 *
 * @author LIU ZI AN
 */
public class SCPRemoteJTreeLoader implements TreeWillExpandListener{

    @Override
    public void treeWillExpand(TreeExpansionEvent path) throws ExpandVetoException {
        TreePath selectedNode = path.getPath();
        String selectedPath = convertTreePathToPath(selectedNode.toString());
        DefaultMutableTreeNode selectedFileName =(DefaultMutableTreeNode) path.getPath().getLastPathComponent();
 
        JTreeLoader loader = new JTreeLoader();
         
        try {
            loader.addNodesRemoteSCP(selectedPath, selectedFileName, SCPRemoteFilePrompt.getChannelSftp());
            
        } catch (SftpException ex) {
            Logger.getLogger(RemoteJtreeLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void treeWillCollapse(TreeExpansionEvent arg0) throws ExpandVetoException {
        
    }
    
    private String convertTreePathToPath(String uncleanPath) {
        StringBuilder sb = new StringBuilder(uncleanPath);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        String [] pathItems = sb.toString().split(", ");
        return String.join("/",pathItems);
    }
}
