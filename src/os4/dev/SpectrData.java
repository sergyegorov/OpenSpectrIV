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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class SpectrData {
    File Src;
    public SpectrData(File from) throws IOException{
        Src = from;
        checkLevel(LevelPreview);
    }
    
    public SpectrData(File from,int level) throws IOException{
        Src = from;
        checkLevel(level);
    }
    
    public SpectrDispers Dispers;
    public static final int LevelPreview = 1;
    public static final int LevelChanels = 2;
    public static final int LevelRawData = 3;
    public SpectrData(SpectrRawData src_data,SpectrDispers disp){
        RawData = src_data;
        LoadedLevel = LevelRawData;
        Dispers = new SpectrDispers(disp);
    }
    
    RandomAccessFile RAF;
    final void checkLevel(int level) throws IOException{
        if(LoadedLevel < level){
            RAF = new RandomAccessFile(Src,"r");//new FileInputStream(Src);
            FileInputStream fis = new FileInputStream(RAF.getFD());
            DataInputStream dis = new DataInputStream(fis);
            load(dis, level);
            dis.close();
        }
    }
    
    public class ExtraReturnContainer{
        public int Ret;
    }
    
    public double[] getProfile(int sn,double ly,int width,ExtraReturnContainer midle) throws IOException{
        SpectrRawData data = getRawData();
        double[] values = null;
        int value_count = 0;
        double[] nul = null;
        int nul_count = 0;
        for(int frame_i = 0;frame_i < data.getFrameCount(sn);frame_i ++){
            SpectrRawDataSensorFrame frame = data.getFrame(sn, frame_i);
            if(values == null){
                values = new double[frame.Data.length];
                nul = new double[frame.Data.length];
            }
            double[] cur_buffer;
            if(frame.IsNull == false)
                cur_buffer = values;
            else
                cur_buffer = nul;
            for(int i = 0;i<values.length;i++)
                cur_buffer[i] += frame.Data[i].getModule();
            if(frame.IsNull == false)
                value_count ++;
            else
                nul_count ++;
        }
        if(nul_count == 0)
            nul_count = 1;
        for(int i = 0;i<values.length;i++){
            values[i] = values[i]/value_count - nul[i]/nul_count;
        }
        int center = (int)Dispers.getLocalPixelByLy(sn, ly);
        int from = center - width / 2;
        int to = center + width / 2;
        if(from < 0){
            center -= from;
            to -= from;
            from = 0;
        }
        if(to >= values.length){
            int dlt = to - values.length;
            center += dlt;
            to -= dlt;
            from -= dlt;
        }
        
        double[] ret = new double[to-from+1];
        for(int i = 0;i<ret.length;i++)
            ret[i] = values[from+i];
        
        midle.Ret = center-from;
        
        return ret;
    }
    
    public void save(File to) throws IOException{
        checkLevel(LevelRawData);
        if(to.exists() == false)
            to.createNewFile();
        RAF = new RandomAccessFile(to,"rw");
        FileOutputStream fos = new FileOutputStream(RAF.getFD());
        DataOutputStream dos = new DataOutputStream(fos);
        save(dos);
        dos.flush();
        dos.close();
        Src = to;
    }
    
    public void save() throws IOException{
        save(Src);
    }
    
    SpectrRawData RawData;
    ArrayList<SpectrDataChanel> Chanel = new ArrayList<>();
    CustomDrawing Drawing;
    public SpectrRawData getRawData() throws IOException{
        checkLevel(LevelRawData);
        return RawData;
    }
    
    public CustomDrawing getFullView() throws IOException{
        return Drawing;
    }
    
    public void setFullView(CustomDrawing dr){
        Drawing = dr;
    }
    
    public SpectrDataChanel getChanerView(double ly) throws IOException{
        checkLevel(LevelChanels);
        return null;
    }
    
    public SpectrDataChanel addChanelView(double ly) throws IOException{
        checkLevel(LevelChanels);
        return null;
    }
    
    public void removeChanelView(double ly) throws IOException{
        checkLevel(LevelChanels);
        for(SpectrDataChanel ch : Chanel)
            if(ch.Ly == ly){
                Chanel.remove(ch);
                return;
            }
    }
    
    public void clearAllChangelViews() throws IOException{
        checkLevel(LevelChanels);
        Chanel.clear();
    }
    
    int LoadedLevel = 0;
    long LoadedPosition = 0;
    int LoadedVersion = 0;
    private void load(DataInputStream dis,int level) throws IOException{
        if(LoadedLevel >= level)
            return;
        
        dis.skip(LoadedPosition);
        
        if(LoadedLevel < LevelPreview){
            LoadedVersion = StreamTools.versionBlockBegin(dis, 1, 1);
        
            Dispers = new SpectrDispers();
            Dispers.load(dis);
            
            Drawing = new CustomDrawing();
            Drawing.load(dis);
            
            LoadedLevel = LevelPreview;
            LoadedPosition = RAF.getFilePointer();
        }
        
        if(level >= LevelChanels){
            if(LoadedLevel < LevelChanels){
                int count = dis.readInt();
                for(int i = 0;i<count;i++){
                    SpectrDataChanel ch = new SpectrDataChanel();
                    ch.load(dis);
                    Chanel.add(ch);
                }
                LoadedLevel = LevelPreview;
                LoadedPosition = RAF.getFilePointer();
                
                if(level == LevelRawData){
                    RawData = new SpectrRawData();
                    RawData.load(dis);

                    StreamTools.versionBlockEnd(dis);

                    LoadedLevel = LevelRawData;
                }
            }            
        }
    }
    
    private void save(DataOutputStream dos) throws IOException{
        if(LoadedLevel < LevelRawData)
            throw new IOException("Loaded data is not full. Can't write.");
        StreamTools.versionBlockBegin(dos, 1);
        Dispers.save(dos);
        Drawing.save(dos);
        dos.writeInt(Chanel.size());
        for(SpectrDataChanel ch : Chanel)
            ch.save(dos);
        RawData.save(dos);
        StreamTools.versionBlockEnd(dos);
    }
}
