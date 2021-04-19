package com.mycompany.sshtunneling.jtreedisplay;


import java.io.File;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;


public class MyTreeModel implements TreeModel {
    private DefaultTreeModel model;
    
    public MyTreeModel(DefaultTreeModel model) {
        this.model = model;
    }
    
    @Override
    public Object getRoot() {
        return model.getRoot();
    }

    @Override
    public Object getChild(Object arg0, int arg1) {
        return model.getChild(arg0, arg1);
    }

    @Override
    public int getChildCount(Object arg0) {
        return model.getChildCount(arg0);
    }

    @Override
    public boolean isLeaf(Object arg0) {
        File file = new File(String.valueOf(arg0));
        return file.isFile();
    }

    @Override
    public void valueForPathChanged(TreePath arg0, Object arg1) {
        model.valueForPathChanged(arg0, arg1);
    }

    @Override
    public int getIndexOfChild(Object arg0, Object arg1) {
        return model.getIndexOfChild(arg0,arg1);
    }

    @Override
    public void addTreeModelListener(TreeModelListener arg0) {
        model.addTreeModelListener(arg0);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener arg0) {
        model.removeTreeModelListener(arg0);
    }
    
    
}
