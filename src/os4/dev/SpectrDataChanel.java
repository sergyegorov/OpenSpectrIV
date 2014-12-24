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
import os4.serv.AbstractChanelEditor;
import os4.serv.StreamTools;
import os4.serv.Vector;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class SpectrDataChanel {
    public float Ly;
    public Vector Data[][];
    public float[] ProfileLeft;
    public float[] ProfileRight;
    public SpectrDataChanel(){
        
    }
    
    public AbstractChanelEditor getEditor(){
        return null;
    }
    
    public void load(DataInputStream dis) throws IOException{
        StreamTools.versionBlockBegin(dis, 1, 1);
        Ly = dis.readFloat();
        int depth = dis.readInt();
        Data = new Vector[depth][];
        for(int i = 0;i<depth;i++)
            Data[i] = StreamTools.readVectorArray(dis);
        ProfileLeft = StreamTools.readFloatArray(dis);
        ProfileRight = StreamTools.readFloatArray(dis);
        StreamTools.versionBlockEnd(dis);
    }
    
    public void save(DataOutputStream dos) throws IOException{
        StreamTools.versionBlockBegin(dos, 1);
        dos.writeFloat(Ly);
        dos.writeInt(Data.length);
        for(Vector[] line : Data)
            StreamTools.writeVectorArray(line, dos);
        StreamTools.writeFloatArray(ProfileLeft, dos);
        StreamTools.writeFloatArray(ProfileRight, dos);
        StreamTools.versionBlockEnd(dos);
    }
}
