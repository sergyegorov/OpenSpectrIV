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

package os4.dev.debug;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import os4.Common;
import os4.MainConfig;
import os4.dev.AbstractDevice;
import os4.dev.AbstractMeasuringCondition;
import os4.dev.method.AbstractMethod;
import os4.dev.MeasuringLogWindow;
import os4.dev.SensorConfig;
import os4.dev.SpectrData;
import os4.dev.SpectrDispers;
import os4.dev.SpectrRawData;
import os4.dev.method.MethodCalibratedChanels;
import os4.serv.UParser;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class DeviceDebug extends AbstractDevice{
    private static final Logger Log = Common.getLogger( DeviceDebug.class);
    final static String Sufix = "спектр для отображения.csv";

    public static String[] Names;
    File RootDir;
    public DeviceDebug(File dir){
        RootDir = dir;
        File[] list = dir.listFiles((File pathname) -> pathname.getName().endsWith(Sufix));
        Names = new String[list.length];
        for(int i = 0;i<Names.length;i++){
            String name = list[i].getName();
            name = name.substring(0,name.length() - Sufix.length());
            Names[i] = name;
        }
    }

    @Override
    public AbstractMeasuringCondition getDefaultCondition() {
        return new DevDebugMeasuringCondition();
    }

    @Override
    public AbstractMethod loadMethod(File from_file) throws Exception{
        MethodCalibratedChanels ret = new MethodCalibratedChanels(from_file);
        return ret;
    }

    DeviceDebugMeasuringParameter MParam;
    @Override
    protected SpectrRawData measuringInternal(AbstractMeasuringCondition cond) {
        if(MParam == null)
            MParam = new DeviceDebugMeasuringParameter();
        MParam.setVisible(true);
        if(MParam.DialogResult < 0)
            return null;
        try{
            String selected_name = Names[MParam.DialogResult];
            MeasuringLogWindow.setProgress(0, "Loading file");
            SpectrRawData ret = load(selected_name,((DevDebugMeasuringCondition)cond).ExpCount);
            MeasuringLogWindow.setProgress(100, "Done");
            return ret;
        } catch(Exception ex){
            Log.log(Level.SEVERE,"Measuring: Loading debug data error...",ex);
        }
        return null;
    }

    float[] parseCSVLine(String line) throws ParseException{
        String values[] = line.split(";");
        float ret[] = new float[values.length-1];
        for(int i = 0;i<ret.length;i++)
            ret[i] = (float)UParser.parseDouble(values[i]);
        return ret;
    }
    
    int BaseLevel = 7100;
    SpectrRawData load(String name,int frame_count) throws IOException, Exception{
        short[][] val_null = new short[8][];
        for(int s = 0;s<8;s++)
            val_null[s] = new short[4096];
        internalStartMeasuring(8);
        long started = System.currentTimeMillis();
        for(int frame = 0;frame < frame_count;frame++){
            String fname = RootDir.getAbsoluteFile()+"\\"+name+"экспозиция "+(frame+1)+".csv";
            File f = new File(fname);
            List<String> flines = Files.readAllLines(f.toPath());
            int line = 1;
            for(int sn = 0;sn<8;sn++){
                MeasuringLogWindow.setProgress(100*(frame*8+sn)/(frame_count*8), "Reading spectr frame "+frame);
                short[] values = new short[4096];
                for(int pixel = 0;pixel < 4096;pixel++,line ++){
                    float[] line_values = parseCSVLine(flines.get(line));
                    if(line_values[1] != (sn+1))
                        throw new IOException("Wrong file format... Unexpected sn "+sn);
                    if(line_values[2] != pixel)
                        throw new IOException("Wrong file format... Unexpected pixel "+pixel);
                    values[pixel] = (short)(line_values[4] - BaseLevel);
                    val_null[sn][pixel] = (short)(line_values[5] - BaseLevel);
                }
                internalAddSensorData(sn, false, 0, values);
            }
            while(System.currentTimeMillis()-started < 2000)
                Thread.sleep(1);
        }
        for(int s = 0;s<8;s++)
            internalAddSensorData(s, true, 0, val_null[s]);
        return CurrentMeasuring;
    }
    
    boolean IsConnected;
    @Override
    public boolean isConnected() {
        return IsConnected;
    }

    @Override
    public void Open() {
        IsConnected = true;
    }

    @Override
    public void Close() {
        IsConnected = false;
    }

    static final public String ID = "DebugDeviceV1";
    @Override
    public String getId() {
        return ID;
    }
    
    @Override
    protected SpectrData updatePreview(SpectrRawData src) {
        SpectrData ret;
        try {
            ret = new SpectrData(src,getDispers());
            ret.setFullView(generateMultiLineDrawing(src));
            return ret;
        } catch (Exception ex) {
            Log.log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public SensorConfig[] getSensorConfiguration() {
        SensorConfig[] ret = new SensorConfig[8];
        for(int i = 0;i<ret.length;i++){
            ret[i] = new SensorConfig(4096, 1, 
                    MainConfig.getBlankBeg(i),
                    4096-MainConfig.getBlankEnd(i));
        }
        return ret;
    }

    File getDispFile(){
        return new File(Common.DirData+"\\dispers.bin");
    }
    
    SpectrDispers Disp;
    @Override
    public SpectrDispers getDispers() throws Exception {
        if(Disp == null){
            Disp = new SpectrDispers(getSensorConfiguration());
            try{
                File f = getDispFile();
                if(f.exists()){
                    SpectrDispers disp_cand = new SpectrDispers(getSensorConfiguration());
                    FileInputStream fis = new FileInputStream(f);
                    DataInputStream dis = new DataInputStream(fis);
                    disp_cand.load(dis);
                    dis.close();
                    if(disp_cand.isCompatible(Disp))
                        Disp = disp_cand;
                }
            }catch(Exception ex){
            }
        }
        return Disp;
    }

    @Override
    public void setDisper(SpectrDispers disp) throws Exception {
        File f = getDispFile();
        if(f.exists() == false)
            f.createNewFile();
        try{
            Disp = new SpectrDispers(getSensorConfiguration());
            FileOutputStream fos = new FileOutputStream(f);
            DataOutputStream dos = new DataOutputStream(fos);
            disp.save(dos);
            dos.flush();
            dos.close();
            Disp = disp;
        } catch(Exception ex) {
            Log.log(Level.SEVERE,"Dispers saving error...",ex);
        }
    }

    @Override
    protected String getDefaultLinkingTextInternal() {
        return null;
    }

    @Override
    public int getMaxLineValue() {
        return 25000;
    }

    @Override
    public String getMethodExtension() {
        return "ddm";
    }
}
