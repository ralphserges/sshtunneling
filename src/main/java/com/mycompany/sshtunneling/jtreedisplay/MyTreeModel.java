package com.mycompany.sshtunneling.jtreedisplay;

import java.io.File;
import javax.swing.tree.DefaultTreeModel;

public class MyTreeModel extends DefaultTreeModel {
    
    
    public MyTreeModel() {
        super(null);
    }
    
    
    @Override
    public boolean isLeaf(Object arg0) {
        File file = new File(String.valueOf(arg0));
        return file.isFile();
    }
}
