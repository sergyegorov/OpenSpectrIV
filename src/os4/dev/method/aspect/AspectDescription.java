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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import os4.Common;
import os4.Mls;
import os4.dev.method.AbstractMethod;
import os4.dev.method.IMethodAspectEditor;
import os4.dev.method.IMethodDescription;
import os4.serv.DBTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class AspectDescription implements IMethodDescription {
    AbstractMethod Method;
    
    PreparedStatement SelectDescription;
    PreparedStatement UpdateDescription;
    PreparedStatement InsertDescription;
    
    Connection DBConnection;
    int Version;
    public AspectDescription(AbstractMethod method) throws Exception{
        Method = method;
        DBConnection = method.getDBConnection();
        Version = DBTools.getVersion(DBConnection,"descr");
        if(Version == 0){
            Statement st = DBConnection.createStatement();
            String sql = Common.MainProperties.getProperty("METHOD.ASPECT.DESCRIPTION.CREATE_TABLE");
            st.executeUpdate(sql);
            Version = DBTools.setVersion(DBConnection,"descr", 1);
        }
        SelectDescription = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.DESCRIPTION.SELECT"));
        UpdateDescription = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.DESCRIPTION.UPDATE"));
        InsertDescription = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.DESCRIPTION.INSERT"));
    }

    public static final String DefaultValue = "";
    @Override
    public String getDescription(DescriptionTypes type) {
        try{
            SelectDescription.setString(1, type.toString());
            ResultSet results = SelectDescription.executeQuery();
            if(results.next() == false)
                throw new SQLException("Can't find row");
            return results.getString(1);
        } catch(SQLException ex){
            
        }
        return DefaultValue;
    }

    void setDescriptionInternal(DescriptionTypes type, String value) throws Exception{
        UpdateDescription.setString(1, value);
        UpdateDescription.setString(2, type.toString());
        if(UpdateDescription.executeUpdate() == 0){
            InsertDescription.setString(2, value);
            InsertDescription.setString(1, type.toString());
            if(InsertDescription.executeUpdate() == 0)
                throw new Exception("Can't insert row by command "+InsertDescription.toString());
        }
    }
    
    String generateDescription(){
        String ret = "<html>";
        for(DescriptionTypes type : DescriptionTypes.values()){
            String val = getDescription(type);
            if(type == DescriptionTypes.Full || DefaultValue == val)
                continue;
            ret += "<h1>"+type.getHeader()+"</h1><br><i>";
            ret += val;
            ret += "</i><br>";
        }
        ret += "</html>";
        return ret;
    }
    
    @Override
    public void setDescription(DescriptionTypes type, String value) throws Exception {
        if(type != DescriptionTypes.Full){
            setDescriptionInternal(type,value);
            setDescriptionInternal(DescriptionTypes.Full,generateDescription());
            DBConnection.commit();
        }
    }

    @Override
    public String getId() {
        return AbstractMethod.ASPECT_DESCRIPTION;
    }

    @Override
    public IMethodAspectEditor getGUIEditor() {
        return new AspectDescriptionEditor(this);
    }
    
    @Override
    public String toString(){
        return Mls.get("Общее описание");
    }

    @Override
    public void commit() throws Exception{
        Method.commit();
    }

    @Override
    public void updateGUI() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
