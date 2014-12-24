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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import os4.dev.AbstractDevice;
import os4.serv.Dialogs;
import os4.serv.llib.LineLib;
import os4.task.TaskProbLib;
//import org.apache.log4j.Logger;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class Common {
    public static boolean Debug = true;
    
    public static final String SpectrExt = "sp";
    
    // User permisions
    public static final int USER_LEVEL_LABORANT = 0;
    public static final int USER_LEVEL_METHODIST = 1;
    public static final int USER_LEVEL_SETUP = 2;
    public static final int USER_LEVEL_EXPERIMENTAL = 3;
    public static int UserLevel = USER_LEVEL_LABORANT;
    
    // Main window
    public static OpenSpectrIV MainWindow;
    
    public static Properties MainProperties;
    
    public static Logger getLogger(Class log_class){
        return Logger.getLogger(log_class.getCanonicalName());
    }
    
    final public static int Version = 0,SubVersion = 0,Build = 1;
    final public static String getGuiName(){
        File f = new File(".");
        String ret = Mls.get("Open Spectr IV")+" "+Mls.get("Ver")+Version+"."+SubVersion+"["+Build+"]"+
                " http://www.spectrometer.com.ua";
        if(Debug == true)
            ret += " WDir:"+f.getAbsolutePath();
        return ret;
    }
    
    final public static String getRelativeDataPath(String path){
        if(path.startsWith(DirData))
            path = path.substring(DirData.length()+1);
        return path;
    }
    
    public static TaskProbLib ProbLib;
    public static String DirBase;
    public static String DirData;
    public static LineLib LLib;
    final public static void init() throws OS4ExceptionInternalError, IOException{
        MainProperties = new Properties();
        File prop_file = new File("osp4.properties");
        if(prop_file.exists())
            MainProperties.load(new FileInputStream(prop_file));
        else
            Log.log(Level.WARNING,"Can't find property file osp4.prop");
        // do not close porp_file for keep the only copy of the programm in memory
        File f = new File("");
        DirBase = f.getAbsolutePath();
        DirData = DirBase+"\\Data";
        f = new File(DirData);
        if(f.exists() == false)
            f.mkdir();

        MainConfig.init();
        
        LLib = new LineLib("ld.bin");
        ProbLib = new TaskProbLib();
    }
    
    private static final Logger Log = getLogger( Common.class );
    public static AbstractDevice getDevice(){
        AbstractDevice ret;
        try{
            ret = MainConfig.getDevice();
        } catch(Exception ex){
            ret = null;
            Log.log(Level.SEVERE,"Can't open device ",ex);
        }
        if(ret == null){
            Dialogs.errorWarnningMLS("Нет открытого устройства... Либо ошибка подключения, либо не настроена программа.");
        }
        return ret;
    }
}
