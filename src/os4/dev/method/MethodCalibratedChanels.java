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

package os4.dev.method;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import os4.dev.SpectrData;
import os4.dev.method.aspect.AspectCalibration;
import os4.dev.method.aspect.AspectChanelSet;
import os4.dev.method.aspect.AspectDescription;
import os4.dev.method.chanels.MultiLineSpectrChanel;
import os4.dev.method.formula.FormulaGOST;
import os4.task.AbstractTask;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class MethodCalibratedChanels implements AbstractMethod{
    File BaseDirectory;
    IMethodAspect[] Aspects;
    public MethodCalibratedChanels(File base_directory) throws Exception{
        BaseDirectory = base_directory;
        Aspects = new IMethodAspect[3];
        try{
            Aspects[0] = new AspectDescription(this);
            Aspects[1] = new AspectChanelSet(this);
            Aspects[2] = new AspectCalibration(this);
            getDBConnection().commit();
        } catch(SQLException sql_ex){
            getDBConnection().rollback();
            throw sql_ex;
        }
    }

    @Override
    public AbstractTask getGUIEditor() {
        return new MultiAspectMethodEditor(this);
    }

    @Override
    public IMethodAspect[] getAspectSet() {
        return Aspects;
    }

    @Override
    public AbstractMethodResult[] calculate(SpectrData data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void commit() throws Exception{
        CurrentConnection.commit();
    }

    Connection CurrentConnection;
    @Override
    final public Connection getDBConnection() throws Exception{
        if(CurrentConnection == null || CurrentConnection.isClosed()){
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            CurrentConnection = DriverManager.getConnection("jdbc:hsqldb:file:"+getFile("db"), "SA", "");
        }
        return CurrentConnection;
    }

    @Override
    public File getFile(String name) {
        if(name == null)
            return new File(BaseDirectory.getAbsolutePath());
        return new File(BaseDirectory.getAbsolutePath()+File.separator+name);
    }

    @Override
    public AbstractMethod cloneMethod() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbstractChanel createNewChanel(String name) throws Exception {
        return new MultiLineSpectrChanel(name,this);
    }

    @Override
    public AbstractFormula createNewFormula(String element, String formula) {
        return new FormulaGOST(element,formula,this);
    }
}
