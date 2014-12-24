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
import java.io.IOException;
import os4.dev.SpectrDispers;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class Point3D {
    public float X,Y,Z;
    public Point3D(){
        
    }
    
    public Point3D(float x,float y,float z){
        X = x;
        Y = y;
        Z = z;
    }
    
    public void save_(DataOutputStream dos) throws IOException{
        dos.write(0);
        dos.writeFloat(X);
        dos.writeFloat(Y);
        dos.writeFloat(Z);
    }
    
    public void load(DataInputStream dis) throws IOException{
        int ver = dis.read();
        X = dis.readFloat();
        Y = dis.readFloat();
        Z = dis.readFloat();
    }
    
    public double getX(SpectrDispers disp,int sn){
        return disp.getLyByGlobalPixel(sn, X);
    }
    
    static final byte DataCasesSet = -1;
    static final byte DataCasesInc = 0;
    static final byte DataCasesByte = 1;
    static final byte DataCasesShort = 2;
    static final byte DataCasesZerro = 3;
    
    byte checkValueType(short value,short prev) throws IOException{
        int val = (int)(prev - value);
        if(val == 0)
            return DataCasesZerro;
        
        if(Math.abs(val) < 128){
            if(val == -1)
                return DataCasesInc;
            else
                return DataCasesByte;
        }
        
        if(Math.abs(val) < Short.MAX_VALUE)
            return DataCasesShort;
        
        throw new IOException("Unsupported float value "+value);
    }
    
    //double SavedValue;
    short saveValue(DataOutputStream dos,byte type, short val,short prev) throws IOException{
        short dlt = (short)(val - prev);
        switch(type){
            case DataCasesByte:
                dos.write((byte)(dlt));
                return (short)(prev+(byte)(dlt));
            case DataCasesShort:
                dos.writeShort((short)dlt);
                return (short)(prev+dlt);
            case DataCasesZerro:
                return prev;
            case DataCasesInc:
                return (short)(prev+1);
        }
        throw new IOException("Wrong type...");
    }
    
    static short PrevX,PrevY,PrevZ;
    public void saveARC(DataOutputStream dos,
            double x0,double y0,double z0,
            double kx,double ky,double kz,
            int index) throws IOException{
        double dx = (X-x0)*kx;
        double dy = (Y-y0)*ky;
        double dz = (Z-z0)*kz;
        if(dx > Short.MAX_VALUE || dx < -Short.MAX_VALUE)
            throw new IOException("Wrong x value diapazone!");
        if(dy > Short.MAX_VALUE || dy < -Short.MAX_VALUE)
            throw new IOException("Wrong y value diapazone!");
        if(dz > Short.MAX_VALUE || dz < -Short.MAX_VALUE)
            throw new IOException("Wrong z value diapazone!");
        short x = (short)Math.round(dx);
        short y = (short)Math.round(dy);
        short z = (short)Math.round(dz);
        if(index == 0){
            dos.write(DataCasesSet);
            dos.writeShort(x);
            dos.writeShort(y);
            dos.writeShort(z);
            PrevX = x;
            PrevY = y;
            PrevZ = z;
        } else {
            byte verx = checkValueType(x,PrevX);
            byte very = checkValueType(y,PrevY);
            byte verz = checkValueType(z,PrevZ);
            
            byte ver = (byte)(verx | (very << 2) | (verz << 4));
            
            dos.write(ver);
            PrevX = saveValue(dos,verx,x,PrevX);
            PrevY = saveValue(dos,very,y,PrevY);
            PrevZ = saveValue(dos,verz,z,PrevZ);
        }
    }
    
    short readValue(DataInputStream dis, int type,short prev_val) throws IOException{
        switch(type){
            case DataCasesByte:
                return (short)(prev_val + (byte)dis.read());
            case DataCasesShort:
                return (short)(prev_val + dis.readShort());
            case DataCasesZerro:
                return prev_val;
            case DataCasesInc:
                return (short)(prev_val + 1);
        }
        throw new IOException("Wrong value type...");
    }
    
    public void loadARC(DataInputStream dis,
            double x0,double y0,double z0,
            double kx,double ky,double kz) throws IOException{
        byte conf = (byte)dis.read();
        if(conf == DataCasesSet){
            PrevX = dis.readShort();
            PrevY = dis.readShort();
            PrevZ = dis.readShort();
            X = (float)(PrevX/kx+x0);
            Y = (float)(PrevY/ky+y0);
            Z = (float)(PrevZ/kz+z0);
        } else {
            PrevX = readValue(dis, conf&0x3, PrevX);
            X = (float)(PrevX/kx+x0);
            
            PrevY = readValue(dis, (conf>>2)&0x3, PrevY);
            Y = (float)(PrevY/kx+y0);
            
            PrevZ = readValue(dis, (conf>>4)&0x3, PrevZ);
            Z = (float)(PrevZ/kz+z0);
        }
    }
    
    public boolean isEquals(Point3D p,double dlt){
        return Math.abs(X-p.X) < dlt &&
                Math.abs(Y-p.Y) < dlt &&
                Math.abs(Z-p.Z) < dlt;
    }
}
