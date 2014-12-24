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

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import os4.serv.StreamTools;
import os4.serv.Vector;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class SpectrRawDataSensorFrame {
    public int ExpositionCRC;
    public boolean IsNull;
    public long StartMeasuringTime,EndMeasuringTime;
    public int Width,Height;
    public Vector[] Data;
    
    /**
     * This default constructor for single measuring from single sensor
     * @param is_nul - if true this is null calibration measuring
     * @param exposition_id - exposition id
     * @param time_from - time got from System.nanoTime() at the beginning of 
     * the measuring;
     * @param time_to - time got from System.nanoTime() at the end of 
     * the measuring;
     * @param width
     * @param height
     * @param data - short[] result values. For line sensor this is 
     * short[size]. For matrix sensor short[width x height]
     * @throws java.lang.Exception - if data is incorrect
     */
    public SpectrRawDataSensorFrame(boolean is_nul,int exposition_id,
            long time_from,long time_to,int width,int height,
            short[] data) throws Exception{
        IsNull = is_nul;
        ExpositionCRC = exposition_id;
        StartMeasuringTime = time_from;
        EndMeasuringTime = time_to;
        Width = width;
        Height = height;
        if(width*height != data.length)
            throw new Exception("Data is incorect");
        Data = new Vector[data.length];//(short[])data.clone();
        for(int i = 0;i<Data.length;i++)
            Data[i] = new Vector(data[i]);
    }
    
    public SpectrRawDataSensorFrame(){
        
    }
    
    final public void save(DataOutputStream os) throws IOException{
        StreamTools.versionBlockBegin(os, 1);
        os.writeBoolean(IsNull);
        os.writeInt(ExpositionCRC);
        os.writeLong(StartMeasuringTime);
        os.writeLong(EndMeasuringTime);
        os.writeInt(Width);
        os.writeInt(Height);
        StreamTools.writeVectorArray(Data, os);
        StreamTools.versionBlockEnd(os);
    }
    
    final public void load(DataInputStream is) throws IOException{
        StreamTools.versionBlockBegin(is, 1, 1);
        IsNull = is.readBoolean();
        ExpositionCRC = is.readInt();
        StartMeasuringTime = is.readLong();
        EndMeasuringTime = is.readLong();
        Width = is.readInt();
        Height = is.readInt();
        Data = StreamTools.readVectorArray(is);
        StreamTools.versionBlockEnd(is);
    }
    
    public boolean isEquals(SpectrRawDataSensorFrame data){
        if(IsNull != data.IsNull)
            return false;
        if(ExpositionCRC != data.ExpositionCRC)
            return false;
        if(StartMeasuringTime != data.StartMeasuringTime)
            return false;
        if(EndMeasuringTime != data.EndMeasuringTime)
            return false;
        if(Width != data.Width)
            return false;
        if(Height != data.Height)
            return false;
        if(Data.length != data.Data.length)
            return false;
        for(int i = 0;i<Data.length;i++)
            if(Data[i] != data.Data[i])
                return false;
        return true;
    }
}
