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

import java.io.File;
import os4.dev.method.AbstractMethod;
import java.util.logging.Level;
import java.util.logging.Logger;
import os4.OS4Exception;
import os4.serv.Point3D;
import os4.Common;


/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public abstract class AbstractDevice {
    public abstract AbstractMeasuringCondition getDefaultCondition();
    public abstract AbstractMethod loadMethod(File from_file) throws Exception;
    public abstract boolean isConnected();
    public abstract void Open();
    public abstract void Close();
    public abstract String getId();
    public abstract SpectrDispers getDispers() throws Exception;
    public abstract void setDisper(SpectrDispers disp) throws Exception;
    protected abstract SpectrData updatePreview(SpectrRawData src);
    public abstract String getMethodExtension();
    public String getDefaultLinkingText(){
        String ret = getDefaultLinkingTextInternal();
        if(ret == null)
            ret = "#Liking---------------------------------\r\n#s1:2 - sensor 1 polinome 2\r\n#pixel-ly\r\n\r\n\r\n#end";
        return ret;
    }
    protected abstract String getDefaultLinkingTextInternal();
        
    abstract public int getMaxLineValue();

    SpectrData LatestResult;
    public SpectrData measuring(AbstractMeasuringCondition cond) throws OS4Exception{
        if(cond.isCompatibleWith(getId()) == false)
            throw new OS4Exception("Device "+getId()+" can't work with current program...");
        MeasuringLogWindow log = new MeasuringLogWindow();
        Thread th = new Thread(new Runnable(){
            Logger Log = Common.getLogger( AbstractDevice.class );
            @Override
            public void run() {
                try{
                    SpectrRawData data = measuringInternal(cond);
                    LatestResult = updatePreview(data);
                    MeasuringLogWindow.end();
                } catch(Exception ex) {
                    Log.log(Level.SEVERE,"Measuring error: ",ex);
                }
            }
        }
        );
        th.start();
        log.setVisible(true);
        return LatestResult;
    }
    
    protected abstract SpectrRawData measuringInternal(AbstractMeasuringCondition cond);
    
    public abstract SensorConfig[] getSensorConfiguration();

    protected SpectrRawData CurrentMeasuring;
    protected long[] SensorMeasuringTimes;
    protected void internalStartMeasuring(int sensor_count){
        CurrentMeasuring = new SpectrRawData(sensor_count,getId());
        SensorMeasuringTimes = new long[sensor_count];
        long time = System.currentTimeMillis();
        for(int i = 0;i<sensor_count;i++)
            SensorMeasuringTimes[i] = time;
    }
    
    protected void internalAddSensorData(int sensor,boolean is_null,int exp_id,short[] data) throws Exception{
        long time = System.currentTimeMillis();
        SpectrRawDataSensorFrame fr = new SpectrRawDataSensorFrame(is_null, exp_id, 
                SensorMeasuringTimes[sensor], time, data.length, 1, data);
        SensorMeasuringTimes[sensor] = time;
        CurrentMeasuring.addFrame(sensor, fr);
    }
    
    public static final CustomDrawing generateMultiLineDrawing(SpectrRawData src){
        int sc = src.getSensorCount();
        CustomDrawing ret = new CustomDrawing();
        int base = 0;
        for(int s = 0;s<sc;s++){
            float[] ever_active = src.getEverModule(s);
            int segment;
            float base_level = 0;

            if((s&1) == 0)
                segment = ret.addLine(CustomDrawing.DrawColorSpectrDark,base_level);
            else
                segment = ret.addLine(CustomDrawing.DrawColorSpectrLight,base_level);
            for(int i = 0;i<ever_active.length;i++)
                ret.addPoint(segment, new Point3D(i+base,ever_active[i],0));
            base += ever_active.length;
        }        
        return ret;
    }
}
