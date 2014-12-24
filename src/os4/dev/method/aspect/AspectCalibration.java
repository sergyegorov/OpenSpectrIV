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

package os4.dev.method.aspect;

import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import os4.Common;
import os4.Mls;
import os4.dev.method.AbstractMethod;
import os4.dev.method.IMethodAspect;
import os4.dev.method.IMethodAspectEditor;
import os4.dev.method.IMethodDescription;
import os4.serv.DBTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class AspectCalibration implements IMethodAspect{
    int Version;
    Connection DBConnection;
    
    AbstractMethod Method;
    public AspectCalibration(AbstractMethod method) throws Exception{
        Method = method;
        DBConnection = method.getDBConnection();
        Version = DBTools.getVersion(DBConnection,"calibr");
        if(Version == 0){
            Statement st = DBConnection.createStatement();
            
            st.executeUpdate("DROP TABLE t_results IF EXISTS");
            st.executeUpdate("DROP TABLE t_formulas IF EXISTS");
            st.executeUpdate("DROP TABLE t_measuring IF EXISTS");
            st.executeUpdate("DROP TABLE t_probs IF EXISTS");
            
            String sql = Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.CREATE_TABLE.PROB");
            st.executeUpdate(sql);
            
            sql = Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.CREATE_TABLE.MEASURING");
            st.executeUpdate(sql);
            
            sql = Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.CREATE_TABLE.FORMULAS");
            st.executeUpdate(sql);
            
            sql = Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.CREATE_TABLE.RESULTS");
            st.executeUpdate(sql);
            
            Version = DBTools.setVersion(DBConnection,"calibr", 1);
            DBConnection.commit();
        }
    }
    
    AspectCalibrationTableModel TM;
    public AbstractTableModel getModel() throws Exception{
        if(TM == null)
            TM = new AspectCalibrationTableModel(Method);
        return TM;
    }
    
    @Override
    public String getId() {
        return AbstractMethod.ASPECT_CALIBR;
    }

    AspectCalibrationEditor Editor;
    @Override
    public IMethodAspectEditor getGUIEditor() {
        if(Editor == null)
            try {
                Editor = new AspectCalibrationEditor(this,Method);
        } catch (Exception ex) {
            Common.getLogger(AspectCalibration.class).log(Level.SEVERE, "Can't create calibration editor", ex);
        }
        try {
            updateGUI();
        } catch (Exception ex) {
            Common.getLogger(AspectCalibration.class).log(Level.SEVERE, null, ex);
        }
        return Editor;
    }

    @Override
    public void commit() throws Exception {
        Method.commit();
    }

    public String filterElementName(String element_cand) throws Exception{
        updateGUI();
        for(String element : Elements)
            if(element.toLowerCase().equals(element_cand.trim().toLowerCase()))
                return element;
        return null;
    }
    
    public String filterStandartName(String standart_cand) throws Exception{
        updateGUI();
        for(String standart : Standarts)
            if(standart.toLowerCase().equals(standart_cand.trim().toLowerCase()))
                return standart;
        return null;
    }
    
    public String Elements[];
    public String Standarts[];
    @Override
    public void updateGUI() throws Exception {
        IMethodDescription desc = (IMethodDescription)Method.getAspect(AbstractMethod.ASPECT_DESCRIPTION);
        Elements = desc.getDescription(IMethodDescription.DescriptionTypes.Elements).split(",");
        for(int i = 0;i<Elements.length;i++)
            Elements[i] = Elements[i].trim();
        Standarts = desc.getDescription(IMethodDescription.DescriptionTypes.Standarts).split(",");
        for(int i = 0;i<Standarts.length;i++)
            Standarts[i] = Standarts[i].trim();
        Editor.update();
    }
    
    @Override
    public String toString(){
        return Mls.get("Калибровка зависимости амплитуды линии от концентрации");
    }
}
