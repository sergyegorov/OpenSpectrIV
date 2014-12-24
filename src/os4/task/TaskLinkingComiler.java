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

package os4.task;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import os4.Common;
import os4.Mls;
import os4.dev.AbstractDevice;
import os4.dev.SensorConfig;
import os4.dev.SpectrDispers;
import os4.serv.UParser;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class TaskLinkingComiler {
    static String filter(String txt){
        String ret = txt;
        int index = ret.indexOf('#');
        if(index >= 0)
            ret = ret.substring(0,index);
        ret = ret.trim();
        if(ret.length() == 0)
            return null;
        return ret;
    }
    
    public static int SensorPolinome[] = new int[1024];
    static public class LyToPixel{
        public double Pixel,Ly;
        public LyToPixel(double pixel,double ly){
            Pixel = pixel;
            Ly = ly;
        }
    }
    public static ArrayList<LyToPixel> LyToPixelRange[];
    public static String LastError;
    public static SpectrDispers compile(String txt){
        AbstractDevice dev = Common.getDevice();
        SensorConfig config[] = dev.getSensorConfiguration();
        LyToPixelRange = new ArrayList[config.length];
        SensorPolinome = new int[config.length];
        int pixel_ends[] = new int[config.length];
        int pixel_starts[] = new int[config.length];
        int pixel_end = 0;
        for(int i = 0;i<SensorPolinome.length;i++){
            LyToPixelRange[i] = new ArrayList<>();
            SensorPolinome[i] = -1;
            pixel_starts[i] = pixel_end;
            pixel_end += config[i].Width;
            pixel_ends[i] = pixel_end;
        }
        LastError = "";
        String[] lines = txt.split("\r\n");
        int line_num = 0;
        int sn = -1;
        for(String line : lines){
            line_num ++;
            String fline = filter(line);
            if(fline == null)
                continue;
            char start_char = fline.charAt(0);
            String possible_error = "";
            try{
                if(Character.isDigit(start_char)){
                    String values[] = fline.split("-");
                    if(values.length != 2)
                        throw new Exception(Mls.get("Too many '-'"));
                    if(values.length < 2)
                        throw new Exception(Mls.get("No sine '-' found"));
                    possible_error = Mls.get("Pixel value parsing error");
                    double pixel_val = UParser.parseDouble(values[0]);
                    possible_error = "";
                    if(pixel_val < pixel_starts[sn])
                        throw new Exception(Mls.get("Pixel index is too low"));
                    if(pixel_val >= pixel_ends[sn])
                        throw new Exception(Mls.get("Pixel index is too big"));
                    possible_error = Mls.get("Ly value parsing error");
                    double ly_val = UParser.parseDouble(values[1]);
                    possible_error = "";
                    LyToPixelRange[sn].add(new LyToPixel(pixel_val,ly_val));
                } else {
                    if(start_char == 's'){
                        String tmp = fline.substring(1);
                        String values[] = tmp.split(":");
                        if(values.length != 2)
                            throw new Exception(Mls.get("Too many ':'"));
                        if(values.length < 2)
                            throw new Exception(Mls.get("No sine ':' found"));
                        possible_error = Mls.get("Sensor number parsing error");
                        sn = Integer.parseInt(values[0]) - 1;
                        if(sn >= SensorPolinome.length){
                            possible_error = "";
                            throw new Exception(Mls.get("Sensor number is too big"));
                        }
                        if(sn < 0){
                            possible_error = "";
                            throw new Exception(Mls.get("Sensor number is too litle"));
                        }
                        possible_error = Mls.get("Ly value parsing error");
                        int order = Integer.parseInt(values[1]);
                        SensorPolinome[sn] = order;
                    } else {
                        throw new Exception(Mls.get("Unknown command..."));
                    }
                }
            } catch(Exception ex) {
                String tmp = String.format("Error (%d):'%s' reason: %s %s\r\n",
                        line_num,line,possible_error,ex.toString());
                LastError += tmp;
            }
        }
        try{
            SpectrDispers ret = new SpectrDispers(config);
            for(int i = 0;i < SensorPolinome.length;i++)
                ret.init(i, SensorPolinome[i], LyToPixelRange[i]);
            ret.initEnd();
            if(LastError.length() == 0)
                return ret;
        } catch(Exception ex){
            Common.getLogger(TaskLinkingComiler.class).log(Level.SEVERE,
                    "");
        }
        return null;
    }
}
