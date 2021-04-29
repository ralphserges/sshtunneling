
package com.mycompany.sshtunneling.jtreedisplay;

import com.jcraft.jsch.ChannelSftp;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


public class RemoteFileTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value instanceof DefaultMutableTreeNode) {
            value = ((DefaultMutableTreeNode)value).getUserObject();
            if (value instanceof ChannelSftp.LsEntry) {
                
                if(((ChannelSftp.LsEntry) value).getAttrs().isDir())
                    leaf = false;
                value = ((ChannelSftp.LsEntry) value).getFilename(); //for display only
            }
        }
        return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    }
    
     
    
}
