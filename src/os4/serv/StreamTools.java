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

package os4.serv;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import os4.Common;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class StreamTools {
    public static final void writeText(File fl,String txt){
        if(txt == null)
            txt = "";
        try {
            if(fl.exists())
                Files.write(fl.toPath(), txt.getBytes(Charset.forName("UTF-8")));
            else
                Files.write(fl.toPath(), txt.getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE_NEW);
        } catch (IOException ex) {
            Common.getLogger(StreamTools.class).log(Level.SEVERE, "Can't write file "+fl.getAbsolutePath(), ex);
        }
    }
    
    public static final void writeText(File fl,ArrayList<String> txt){
        try {
            if(fl.exists())
                fl.delete();
            if(txt == null)
                return;
            FileOutputStream os = new FileOutputStream(fl);
            for(String line : txt)
                os.write(line.getBytes(Charset.forName("UTF-8")));
                //Files.write(fl.toPath(), line.getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE_NEW);
            os.flush();
            os.close();
        } catch (IOException ex) {
            Common.getLogger(StreamTools.class).log(Level.SEVERE, "Can't write file "+fl.getAbsolutePath(), ex);
        }
    }
    
    public static final String readText(File fl){
        if(fl.exists() == false)
            return "";
        try {
            byte[] buf = Files.readAllBytes(fl.toPath());
            String ret = new String(buf, Charset.forName("UTF-8"));
            return ret;
        } catch (IOException ex) {
            Common.getLogger(StreamTools.class).log(Level.SEVERE, "Can't load "+fl.toString(), ex);
        }
        return "";
    }
    
    // Tested by GUIParameterCollectionTest
    public static final void writeString(String val,DataOutputStream os)throws IOException{
        os.write(12);
        if(val == null){
            os.writeInt(-1);
            return;
        }
        os.writeInt(val.length());
        for(int i = 0;i<val.length();i++)
            os.writeChar(val.charAt(i));
    }

    // Tested by GUIParameterCollectionTest
    public static final String readString(DataInputStream is) throws IOException{
        int prefix = is.read();
        if(prefix != 12)
            throw new IOException("Wrong string data block prefix");
        int len = is.readInt();
        if(len < 0)
            return null;
        char[] data = new char[len];
        for(int i = 0;i<len;i++)
            data[i] = is.readChar();
        return new String(data);
    }

    public static final void writeVectorArray(Vector[] data,DataOutputStream os) throws IOException{
        os.write(38);
        os.writeInt(data.length);
        for(int i = 0;i<data.length;i++)
            data[i].save(os);//os.writeShort(data[i]);
        os.write(84);
    }

    public static final Vector[] readVectorArray(DataInputStream is) throws IOException{
        int prefix = is.read();
        if(prefix != 38) throw new IOException("Wrong Short Array prefix");
        int len = is.readInt();
        Vector[] ret = new Vector[len];
        for(int i = 0;i<len;i++)
            ret[i] = new Vector(is);// is.readShort();
        int sufix = is.read();
        if(sufix != 84) throw new IOException("Wrong Short Array sufix");
        return ret;
    }

    public static final void writeDoubleArray(double[] data,DataOutputStream os) throws IOException{
        os.write(21);
        os.writeInt(data.length);
        for(int i = 0;i<data.length;i++)
            os.writeDouble(data[i]);
        os.write(32);
    }
    
    public static final double[] readDoubleArray(DataInputStream is) throws IOException{
        int prefix = is.read();
        if(prefix != 21) throw new IOException("Wrong Double Array prefix");
        int len = is.readInt();
        double[] ret = new double[len];
        for(int i = 0;i<len;i++)
            ret[i] = is.readDouble();
        int sufix = is.read();
        if(sufix != 32) throw new IOException("Wrong Double Array sufix");
        return ret;
    }
    
    public static final void writeFloatArray(float[] data,DataOutputStream os) throws IOException{
        os.write(22);
        os.writeInt(data.length);
        for(int i = 0;i<data.length;i++)
            os.writeFloat(data[i]);
        os.write(33);
    }
    
    public static final float[] readFloatArray(DataInputStream is) throws IOException{
        int prefix = is.read();
        if(prefix != 22) throw new IOException("Wrong Float Array prefix");
        int len = is.readInt();
        float[] ret = new float[len];
        for(int i = 0;i<len;i++)
            ret[i] = is.readFloat();
        int sufix = is.read();
        if(sufix != 33) throw new IOException("Wrong Float Array sufix");
        return ret;
    }
    
    // Tested by os4.dev.SpectrRawDataTest
    public static final void writeShortArray(short[] data,DataOutputStream os) throws IOException{
        os.write(37);
        os.writeInt(data.length);
        for(int i = 0;i<data.length;i++)
            os.writeShort(data[i]);
        os.write(83);
    }
    
    // Tested by os4.dev.SpectrRawDataTest
    public static final short[] readShortArray(DataInputStream is) throws IOException{
        int prefix = is.read();
        if(prefix != 37) throw new IOException("Wrong Short Array prefix");
        int len = is.readInt();
        short[] ret = new short[len];
        for(int i = 0;i<len;i++)
            ret[i] = is.readShort();
        int sufix = is.read();
        if(sufix != 83) throw new IOException("Wrong Short Array sufix");
        return ret;
    }
    
    // Tested by os4.dev.SpectrRawDataTest
    public static final int versionBlockBegin(DataInputStream dis,int ver_from,int ver_to) throws IOException{
        int mark = dis.readInt();
        if(mark != 478456324)
            throw new IOException("Version procted block has not start mark");
        int ver = dis.readInt();
        if(ver < ver_from || ver > ver_to)
            throw new IOException("Unsurported version: "+ver+" ["+ver_from+"..."+ver_to+"]");
        return ver;
    }
    
    // Tested by os4.dev.SpectrRawDataTest
    public static final void versionBlockBegin(DataOutputStream dos,int ver) throws IOException{
        dos.writeInt(478456324);
        dos.writeInt(ver);
    }
    
    // Tested by os4.dev.SpectrRawDataTest
    public static final void versionBlockEnd(DataOutputStream dos) throws IOException{
        dos.writeInt(98123467);
    }
    
    // Tested by os4.dev.SpectrRawDataTest
    public static final void versionBlockEnd(DataInputStream dis) throws IOException{
        int mark = dis.readInt();
        if(mark != 98123467)
            throw new IOException("Invalid end of version block...");
    }
}
