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

package os4;

import java.io.File;
import java.io.IOException;
import static os4.Common.DirBase;
import os4.dev.AbstractDevice;
import os4.dev.debug.DeviceDebug;
import os4.task.params.GUIParameterCollection;
import os4.task.params.GUIParameterDouble;
import os4.task.params.GUIParameterSelector;
import os4.task.params.GUIParameterString;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class MainConfig {
    public static GUIParameterCollection Values;
    
    public static final String ConfigDevType = "Dev.Type";
        public static final String ConfigDevTypeV_Debug = "Debug";
        public static final String ConfigDevTypeV_MLE2K = "Multi Line Ethernet 2K";
        public static final String ConfigDevTypeV_MLE3K = "Multi Line Ethernet 3K";
        public static final String ConfigDevTypeV_MLUSB3K = "Multi Line Usb 3K";

    public static final String ConfigDevDivider = "Dev.Divider";
        public static final String ConfigDevDividerV_1 = "1";
        public static final String ConfigDevDividerV_2 = "2";
        public static final String ConfigDevDividerV_4 = "4";
        public static final String ConfigDevDividerV_8 = "8";
    
    public static final String ConfigDevIPAddr4 = "Dev.IP4";
    public static final String ConfigBkpPath = "BkpPath";

    public static void init() throws OS4ExceptionInternalError, IOException{
        Values = new GUIParameterCollection("Root");
        
        GUIParameterCollection group = new GUIParameterCollection("Device");
        //Values.add(group);
        
        GUIParameterSelector selector = new GUIParameterSelector(ConfigDevType, 
                "Device Type. Select type of the hardware registrator.", DirBase, 
                new String[]{ConfigDevTypeV_Debug,ConfigDevTypeV_MLE2K,
                    ConfigDevTypeV_MLE3K,ConfigDevTypeV_MLUSB3K});
        Values.add(selector);
        
        selector = new GUIParameterSelector(ConfigDevDivider, 
                "Registrator divider. Select registratpr speed divider.", DirBase, 
                new String[]{ConfigDevDividerV_1,ConfigDevDividerV_2,
                    ConfigDevDividerV_4,ConfigDevDividerV_8});
        Values.add(selector);
        
        GUIParameterDouble dval = new GUIParameterDouble(ConfigDevIPAddr4,
                "Ip address. This is last digit of ip address 172.168.143.X",
                    1,1,0,0,250,"0");
        Values.add(dval);
        
        GUIParameterString sval = new GUIParameterString(ConfigBkpPath,
                "Backup path. Where to put reserve copy of the data storage",
                    "bkp\\","bkp\\");
        Values.add(sval);
        
        File f = new File("config.bin");
        if(f.exists() == false)
            f.createNewFile();
        Values.load(f);
    }
    
    static AbstractDevice Dev = null;
    static public AbstractDevice getDevice() throws OS4ExceptionInternalError{
        int val = Values.get(ConfigDevType).getAsInt();
        switch(val){
            case 0:
                Dev = new DeviceDebug(new File(Common.DirBase+"\\debug_data\\"));
                break;
            default:
                throw new OS4ExceptionInternalError("Unknown type of device: "+val);
        }
        return Dev;
    }
    
    static public int getSpectrPicWidth(){
        return 7;
    }
    
    static public int getMaxSpectrShift(){
        return 50;
    }
    
    static public int getBlankBeg(int sensor){
        if((sensor & 1) == 0)
            return 32;
        else
            return 414;
    }
    
    static public int getBlankEnd(int sensor){
        if((sensor & 1) == 1)
            return 32;
        else
            return 414;
    }
}
