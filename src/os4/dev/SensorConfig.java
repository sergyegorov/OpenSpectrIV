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
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class SensorConfig{
    public int Width;
    public int Height;
    public int ActiveFrom;
    public int ActiveTo;
    public SensorConfig(int w,int h,int act_from,int act_to){
        Width = w;
        Height = h;
        ActiveFrom = act_from;
        ActiveTo = act_to;
    }

    public void save(DataOutputStream dos) throws IOException{
        StreamTools.versionBlockBegin(dos, 1);
        dos.writeInt(Width);
        dos.writeInt(Height);
        dos.writeInt(ActiveFrom);
        dos.writeInt(ActiveTo);
        StreamTools.versionBlockEnd(dos);
    }

    public void load(DataInputStream dis) throws IOException{
        StreamTools.versionBlockBegin(dis, 1, 1);
        Width = dis.readInt();//dos.writeInt(Width);
        Height = dis.readInt();//dos.writeInt(Height);
        ActiveFrom = dis.readInt();
        ActiveTo = dis.readInt();
        StreamTools.versionBlockEnd(dis);
    }
    
    public boolean isEquals(SensorConfig conf){
        return Width == conf.Width && Height == conf.Height;
    }
}
