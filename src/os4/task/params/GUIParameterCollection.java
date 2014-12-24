/*
 * The MIT License
 *
 * Copyright 2014 root.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package os4.task.params;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import os4.OS4ExceptionInternalError;
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class GUIParameterCollection{
    ArrayList<AbstractGUIParameter> Parameters;
    
    public int size(){
        return Parameters.size();
    }
    
    public AbstractGUIParameter get(int index){
        return Parameters.get(index);
    }
    
    String Name;
    public GUIParameterCollection(String name){
        Parameters = new ArrayList<>();
        Name = name;
    }
    
    public AbstractGUIParameter has(String id){
        for(int i = 0;i<Parameters.size();i++){
            AbstractGUIParameter candidate = Parameters.get(i);
            if(candidate.is(id))
                return candidate;
        }
        return null;
    }
    
    public void add(AbstractGUIParameter params) throws OS4ExceptionInternalError{
        String id = params.getId();
        if(has(id) != null)
            throw new OS4ExceptionInternalError("Parameter "+params.getId()+" already exsits...");
        Parameters.add(params);
        params.initMaster(this);
    }
    
    public AbstractGUIParameter get(String id) throws OS4ExceptionInternalError{
        AbstractGUIParameter ret = has(id);
        if(ret == null)
            throw new OS4ExceptionInternalError("Can't find parameter: "+id);
        return ret;
    }
    
    File SrcFile;
    public void load(File file) throws IOException, OS4ExceptionInternalError{
        SrcFile = file;
        FileInputStream fis = new FileInputStream(SrcFile);
        try{
            DataInputStream dis = new DataInputStream(fis);
            load(dis);
        } finally{
            fis.close();            
        }
    }
    
    public void commit() throws OS4ExceptionInternalError,IOException{
        if(SrcFile == null)
            throw new OS4ExceptionInternalError("This data was not loaded from file. Can't commit data.");
        FileOutputStream fos = new FileOutputStream(SrcFile);
        try{
            DataOutputStream dos = new DataOutputStream(fos);
            save(dos);
        } finally {
            fos.close();
        }
    }
    
    public DefaultMutableTreeNode getTree(){
        HashMap<String, DefaultMutableTreeNode> folders = new HashMap<>();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        for(int i = 0;i<Parameters.size();i++){
            AbstractGUIParameter candidate = Parameters.get(i);
            String id = candidate.getId();
            int index = id.indexOf('.');
            DefaultMutableTreeNode group_root;
            if(index > 0){
                String group_name = id.substring(0,index);
                group_root = folders.get(group_name);
                if(group_root == null){
                    group_root = new DefaultMutableTreeNode(group_name);
                    folders.put(group_name, group_root);
                    root.add(group_root);
                }
            }
            else
                group_root = root;
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(candidate);
            group_root.add(node);
        }
        
        return root;
    }
    
    public void load(DataInputStream str) throws IOException, OS4ExceptionInternalError{
        if(str.available() == 0)
            return;
        StreamTools.versionBlockBegin(str, 1, 1);
        Name = StreamTools.readString(str);
        int count = str.readInt();//str.writeInt(Parameters.size());
        for(int i = 0;i<count;i++){
            int type = str.readInt();
            AbstractGUIParameter param = AbstractGUIParameter.createInstance(type);//Parameters.get(i).save(str);
            param.load(str);
            boolean found = false;
            String id = param.getId();
            param.initMaster(this);
            for(int j = 0;j<Parameters.size();j++){
                AbstractGUIParameter candidate = Parameters.get(j);
                if(candidate.is(id)){
                    found = true;
                    Parameters.remove(j);
                    Parameters.add(j, param);
                    break;
                }
            }
            if(found == false)
                Parameters.add(param);
        }
        StreamTools.versionBlockEnd(str);
    }
    
    public void save(DataOutputStream str) throws IOException, OS4ExceptionInternalError{
        StreamTools.versionBlockBegin(str, 1);
        StreamTools.writeString(Name,str);
        str.writeInt(Parameters.size());
        for(int i = 0;i<Parameters.size();i++){
            AbstractGUIParameter param = Parameters.get(i);
            str.writeInt(param.getType());
            param.save(str);
        }
        StreamTools.versionBlockEnd(str);
    }
}
