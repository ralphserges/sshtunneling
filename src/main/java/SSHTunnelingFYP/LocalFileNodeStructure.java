
package SSHTunnelingFYP;

import java.io.File;
import java.util.Collections;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;

public class LocalFileNodeStructure {
    
    public static DefaultMutableTreeNode addNodes(DefaultMutableTreeNode currentNode, File dir) {
        String currentPath = dir.getPath(); 
        DefaultMutableTreeNode currentDir = new DefaultMutableTreeNode(dir.getName());
        
        // if param node is null means node is root
        if(currentNode != null) {
            currentNode.add(currentDir);
        }
        
        Vector ol = new Vector();
        String [] tmp = dir.list();
        
 
        for(int i = 0; i < tmp.length; i++) {
            ol.addElement(tmp[i]);
        }
        
        Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
        
        File f; 
        Vector files = new Vector();
        for(int i = 0; i< ol.size(); i++){
            String thisObject = (String) ol.elementAt(i);
            String newPath;
            
            if(currentPath.equals(System.getProperty("user.home") + "/Desktop")) {
                newPath = thisObject;
            }
            else {
                newPath = currentPath + File.separator + thisObject;
            }
            
            if((f = new File(newPath)).isDirectory()) {
                addNodes(currentDir,f);
            }
            else {
                
                files.addElement(thisObject);
            }
            
        }
        for(int i = 0; i < files.size(); i++) {
            currentDir.add(new DefaultMutableTreeNode(files.elementAt(i)));
        }
        return currentDir;
    }
}
