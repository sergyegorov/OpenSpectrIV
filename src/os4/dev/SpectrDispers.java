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
import java.util.ArrayList;
import os4.analit.Function;
import os4.analit.OS4AnalitException;
import os4.serv.StreamTools;
import os4.task.TaskLinkingComiler;
import os4.task.TaskLinkingComiler.LyToPixel;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class SpectrDispers {
    SensorConfig[] Config;
    Function Fk[];
    boolean FkInited[];
    int StartPositions[];
    public SpectrDispers(SensorConfig[] config) throws OS4AnalitException{
        Config = config;
        resetToDefault();
    }
    
    public void setFunction(int sn,Function fk){
        Fk[sn] = new Function(fk);
    }
    
    public Function getFunction(int sn){
        return new Function(Fk[sn]);
    }
    
    final public void resetToDefault() throws OS4AnalitException{
        Fk = new Function[Config.length];
        FkInited = new boolean[Fk.length];
        StartPositions = new int[Fk.length];
        int start_position = 0;
        for(int s = 0;s<Config.length;s++){
            StartPositions[s] = start_position;
            double[] p = {0,Config[s].Width};
            double[] ly = {start_position,start_position+p[1]};
            boolean[] en = {true,true};
            Fk[s] = new Function(Function.FType.Poly1,en,p,ly);
            start_position += Config[s].Width;
        }
    }
    
    public void init(int sn,int order,ArrayList<TaskLinkingComiler.LyToPixel> data) 
            throws OS4AnalitException{
        if(order < 1 || data.isEmpty())
            return;
        
        boolean en[] = new boolean[data.size()];
        double x[] = new double[en.length];
        double y[] = new double[en.length];
        for(int i = 0;i<en.length;i++){
            en[i] = true;
            LyToPixel tmp = data.get(i);
            x[i] = tmp.Pixel - StartPositions[sn];
            y[i] = tmp.Ly;
        }
        
        FkInited[sn] = Fk[sn].Init(Function.getTypeFromIndex(order-1), en, x, y);
    }
    
    int[] findNearestInited(int for_sensor){
        int left = Integer.MAX_VALUE;
        int right = Integer.MAX_VALUE;
        int count = 0;
        
        for(int i = for_sensor-1;i>=0;i--)
            if(FkInited[i] == true){
                left = i;
                count ++;
                break;
            }
        
        for(int i = for_sensor+1;i<Fk.length;i++)
            if(FkInited[i] == true){
                right = i;
                count ++;
                break;
            }
        
        if(count == 0)
            return null;
        
        int[] ret = new int[count];
        count = 0;
        
        if(left != Integer.MAX_VALUE)
            ret[count++]=left;
        if(right != Integer.MAX_VALUE)
            ret[count++]=right;
        
        return ret;
    }
    
    public void initEnd(){
        if(Fk.length < 2)
            return;
        for(int s = 0;s<Fk.length;s++){
            if(FkInited[s])
                continue;
            int ret[] = findNearestInited(s);
            if(ret == null)
                return;
            if(ret.length == 1){
                if(ret[0] < s){
                    int sn = s-1;
                    int last_pixel = Config[sn].Width;
                    double y = Fk[sn].calcDirect(last_pixel);
                    double k1 = (y-Fk[sn].calcDirect(last_pixel-10))/10;
                    double k0 = y;
                    Fk[s].setLine(k0, k1);
                } else {
                    int sn = ret[0];
                    double y = Fk[sn].calcDirect(0);
                    double k1 = (Fk[sn].calcDirect(10)-y)/10;
                    double k0 = y-(StartPositions[sn]-StartPositions[s])*k1;
                    Fk[s].setLine(k0, k1);
                }
            } else {
                double x_beg = StartPositions[ret[0]+1];
                double y_beg = Fk[ret[0]].calcDirect(x_beg);
                double x_end = StartPositions[ret[1]];
                double y_end = Fk[ret[1]].calcDirect(0);
                double k1 = (y_end-y_beg)/(x_end-x_beg);
                double x = StartPositions[s];
                double y = y_beg+(x-x_beg)*k1;
                Fk[s].setLine(y, k1);
            }
        }
    }
    
    public SpectrDispers(SpectrDispers from){
        Config = from.Config;
        Fk = new Function[from.Fk.length];
        FkInited = new boolean[Fk.length];
        StartPositions = new int[Fk.length];
        int pos = 0;
        for(int i = 0;i<Fk.length;i++){
            Fk[i] = new Function(from.Fk[i]);
            StartPositions[i] = pos;
            pos += Config[i].Width;
        }
    }
    
    public SpectrDispers(){
        
    }
    
    public void save(DataOutputStream dos) throws IOException{
        StreamTools.versionBlockBegin(dos, 1);
        dos.writeInt(Config.length);
        for(int i = 0;i<Config.length;i++){
            Config[i].save(dos);
            Fk[i].save(dos);
            dos.writeBoolean(FkInited[i]);
            dos.writeInt(StartPositions[i]);
        }
        StreamTools.versionBlockEnd(dos);
    }
    
    public void load(DataInputStream dis) throws IOException{
        StreamTools.versionBlockBegin(dis, 1, 1);
        int n = dis.readInt();
        Config = new SensorConfig[n];
        Fk = new Function[n];
        StartPositions = new int[n];
        FkInited = new boolean[n];
        for(int i = 0;i<n;i++){
            Config[i] = new SensorConfig(0, 0, 0, 0);
            Config[i].load(dis);
            Fk[i] = new Function();
            Fk[i].load(dis);
            FkInited[i] = dis.readBoolean();
            StartPositions[i] = dis.readInt();
        }
        StreamTools.versionBlockEnd(dis);
    }
    
    public int findSensorByGlobalPixel(double pixel){
        for(int i = 1;i<StartPositions.length;i++)
            if(pixel < StartPositions[i])
                return i-1;
        return StartPositions.length-1;
    }
    
    public int findSensorByLy(double ly,int[] sensors_ret){
        int found = 0;
        for(int i = 0;i<StartPositions.length;i++){
            double from = getLyByLocalPixel(i, 0);
            double to = getLyByLocalPixel(i, Config[i].Width);
            if(ly >= from && ly <= to){
                sensors_ret[found] = i;
                found ++;
            }
        }
        sensors_ret[found] = -1;
        return found;
    }
    
    public double getLyByLocalPixel(int sn,double pixel){
        return Fk[sn].calcDirect(pixel);
    }
    
    public double getLyByGlobalPixel(int sn,double pixel){
        return Fk[sn].calcDirect(pixel-StartPositions[sn]);
    }
    
    public double getLocalPixelByLy(int sn,double ly){
        return Fk[sn].calcRev(ly);
    }
    
    public double getGlobalPixelByLy(int sn,double ly){
        return Fk[sn].calcRev(ly)+StartPositions[sn];
    }
    
    public boolean isCompatible(SpectrDispers disp){
        if(Config.length != disp.Config.length)
            return false;
        for(int i = 0;i<Config.length;i++)
            if(Config[i].isEquals(disp.Config[i]) == false)
                return false;
        return true;
    }
}
