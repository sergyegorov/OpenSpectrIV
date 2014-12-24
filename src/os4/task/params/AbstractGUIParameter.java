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
import java.io.IOException;
import javax.swing.JPanel;
import os4.OS4ExceptionInternalError;
import os4.OS4UnExpectedError;

/**
 *
 * @author root
 */
public interface AbstractGUIParameter {
    default public boolean is(String id){
        return id.equals(getId());
    }
    JPanel getGui();
    String getId();
    void load(DataInputStream str) throws IOException, OS4ExceptionInternalError;
    void save(DataOutputStream str) throws IOException, OS4ExceptionInternalError;
    void reset();
    
    public static final int TypeSelector = 0;
    public static final int TypeDouble = 1;
    public static final int TypeString = 2;
    int getType();
    
    public static AbstractGUIParameter createInstance(int type) throws OS4ExceptionInternalError{
        switch(type){
            case TypeSelector:
                return new GUIParameterSelector();
            case TypeDouble:
                return new GUIParameterDouble();
            case TypeString:
                return new GUIParameterString();                
        }
        throw new OS4ExceptionInternalError("Unknown type of GUI parameter:"+type+" Fix It!");
    }
    
    public static String getShortDescription(String full_description){
        int point = full_description.indexOf('.');
        return full_description.substring(0,point);
    }
    GUIParameterCollection getMaster();
    void initMaster(GUIParameterCollection master);
    double getAsDouble();
    String getAsString();
    default public long getAsLong(){
        return Math.round(getAsDouble());
    }
    default public int getAsInt(){
        long ret = Math.round(getAsDouble());
        if(ret < -Integer.MAX_VALUE || 
                ret > Integer.MAX_VALUE)
            throw new OS4UnExpectedError("Value bigger then int "+ret);
        return (int)ret;
    }
}
