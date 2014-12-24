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
import java.util.ArrayList;
import os4.OS4UnExpectedError;
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 * 
 * Tested by os4.dev.SpectrRawDataTest
 */
public class SpectrRawData {
    ArrayList<SpectrRawDataSensorFrame> Sensors[];
    String DeviceId;
    public SpectrRawData(int sensor_count,String device_id){
        initSensorStorage(sensor_count);
        DeviceId = device_id;
    }
    
    private void initSensorStorage(int sensor_count){
        Sensors = new ArrayList[sensor_count];
        for(int i = 0;i<sensor_count;i++)
            Sensors[i] = new ArrayList<>();
    }
    
    public SpectrRawData(){
        
    }
    
    public float[] getEverModule(int for_sensor){
        int base = 0;
        int fc = Sensors[for_sensor].size();
        SpectrRawDataSensorFrame data = Sensors[for_sensor].get(0);
        float[] ever_active = new float[data.Data.length];
        int ever_active_count = 0;
        float[] ever_nul = new float[data.Data.length];
        int ever_nul_count = 0;
        for(int f = 0;f<fc;f++){
            data = Sensors[for_sensor].get(f);
            if(data.IsNull == false){
                for(int i = 0;i<ever_active.length;i++)
                    ever_active[i] += data.Data[i].asFloat();
                ever_active_count ++;
            } else {
                for(int i = 0;i<ever_nul.length;i++)
                    ever_nul[i] += data.Data[i].asFloat();
                ever_nul_count ++;
            }
        }
        if(ever_nul_count == 0)
            ever_nul_count ++;
        if(ever_active_count == 0)
            throw new OS4UnExpectedError("No signal for sensor "+for_sensor+" in source data");
        int ever_nul_level = 0;
        for(int i = 0;i<ever_active.length;i++){
            ever_active[i] /= ever_active_count;
            ever_nul[i] /= ever_nul_count;
            ever_active[i] -= ever_nul[i];
        }
        for(int i = 0;i<ever_active.length;i++)
            ever_nul_level += ever_nul[i];
        ever_nul_level /= ever_active.length;
        for(int i = 0;i<ever_active.length;i++)
            ever_active[i] += ever_nul_level;
        //int segment;
        //float base_level = 0;
        /*for(float val:ever_nul)
            base_level += val;
        base_level /= ever_nul.length;
        base += ever_active.length;*/
        return ever_active;
        /*long[] sum = new long[Sensors[for_sensor].get(0).Width];
        for(SpectrRawDataSensorFrame frame : Sensors[for_sensor]){
            for(int i = 0;i<sum.length;i++){
                Vector v = frame.Data[i];
                sum[i] += (long)v.getModule();
            }
        }
        int n = Sensors[for_sensor].size();
        int[] ret = new int[sum.length];
        for(int i = 0;i<sum.length;i++)
            ret[i] = (int)(sum[i]/n);
        return ret;*/
    }
    
    public int getSensorCount(){
        return Sensors.length;
    }
    
    public int getFrameCount(int sensor){
        return Sensors[sensor].size();
    }
    
    public void addFrame(int sensor,SpectrRawDataSensorFrame fr){
        Sensors[sensor].add(fr);
    }
    
    public SpectrRawDataSensorFrame getFrame(int sn,int fn){
        return Sensors[sn].get(fn);
    }
    
    final public void save(DataOutputStream os) throws IOException{
        StreamTools.versionBlockBegin(os, 1);
        os.writeInt(Sensors.length);
        StreamTools.writeString(DeviceId, os);
        for (ArrayList<SpectrRawDataSensorFrame> Sensor : Sensors) {
            os.writeInt(Sensor.size());
            for (int f = 0; f < Sensor.size(); f++)
                Sensor.get(f).save(os);
        }
        StreamTools.versionBlockEnd(os);
    }
    
    final public void load(DataInputStream is) throws IOException{
        int ver = StreamTools.versionBlockBegin(is, 1, 1);
        int sensor_count = is.readInt();
        DeviceId = StreamTools.readString(is);
        initSensorStorage(sensor_count);
        for(int s = 0;s<sensor_count;s++){
            int frame_count = is.readInt();
            for(int f = 0;f<frame_count;f++){
                SpectrRawDataSensorFrame sdf = new SpectrRawDataSensorFrame();
                sdf.load(is);
                Sensors[s].add(sdf);
            }
        }
        StreamTools.versionBlockEnd(is);        
    }
    
    public boolean isEquals(SpectrRawData data){
        if(data.Sensors.length != Sensors.length)
            return false;
        for(int s = 0;s<Sensors.length;s++){
            if(data.Sensors[s].size() != Sensors[s].size())
                return false;
        }
        return true;
    }
}
