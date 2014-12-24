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
package os4.dev;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JPanel;
import os4.dev.debug.DevDebugMeasuringCondition;

/**
 *
 * @author root
 */
public interface AbstractMeasuringCondition {
    final static public int TypeDebug = 0;
    
    JPanel getGUIEditor();
    void save(DataOutputStream os) throws IOException;
    void load(DataInputStream is) throws IOException;
    boolean isCompatibleWith(String id);
    
    public static AbstractMeasuringCondition loadFrom(DataInputStream is) throws IOException{
        is.mark(5);
        int type = is.readInt();
        is.reset();
        AbstractMeasuringCondition ret;
        switch(type){
            case TypeDebug:
                ret = new DevDebugMeasuringCondition();
                break;
            default:
                throw new IOException("Wrong type of the condition:"+type);
        }
        ret.load(is);
        return ret;
        
    }
}
