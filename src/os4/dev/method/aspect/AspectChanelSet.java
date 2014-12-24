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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JPanel;

import os4.Common;
import os4.Mls;
import os4.dev.method.AbstractChanel;
import os4.dev.method.AbstractMethod;
import os4.dev.method.IMethodAspectEditor;
import os4.dev.method.IMethodChanelSet;
import os4.dev.method.chanels.MultiLineSpectrChanel;
import os4.serv.DBTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class AspectChanelSet implements IMethodChanelSet{
    static private final Logger Log = Common.getLogger(AspectChanelSet.class);
    
    AbstractMethod Method;
    
    PreparedStatement SelectChanelList;
    PreparedStatement UpdateDescription;
    PreparedStatement InsertDescription;
    
    Connection DBConnection;
    int Version;
    public AspectChanelSet(AbstractMethod method) throws Exception{
        Method = method;
        DBConnection = method.getDBConnection();
        Version = DBTools.getVersion(DBConnection,"chanels");
        if(Version == 0){
            Statement st = DBConnection.createStatement();
            String sql = Common.MainProperties.getProperty("METHOD.ASPECT.CHANEL_SET.CREATE_TABLE");
            st.executeUpdate(sql);
            Version = DBTools.setVersion(DBConnection,"chanels", 1);
        }
        SelectChanelList = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CHANEL_SET.SELECT"));
        //UpdateDescription = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.DESCRIPTION.CHANEL_SET"));
        //InsertDescription = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.DESCRIPTION.CHANEL_SET"));
    }
    
    List<AbstractChanel> ChanelCash = null;
    void loadList(){
        if(ChanelCash == null)
            ChanelCash = new ArrayList<>();
        try {
            ResultSet rs = SelectChanelList.executeQuery();
            while(rs.next())
                try{
                    AbstractChanel ach = Method.createNewChanel(null);
                    ach.load(rs);
                    if(ach instanceof MultiLineSpectrChanel)
                        ((MultiLineSpectrChanel)ach).init(this);
                    ChanelCash.add(ach);
                }catch(Exception ex){
                    Log.log(Level.SEVERE,"Can't create absract chanel",ex);
                }
        } catch (SQLException ex) {
            Log.log(Level.SEVERE, "Can't select chanel information", ex);
        }
    }

    @Override
    public int getChanelCount() {
        if(ChanelCash == null)
            loadList();
        return ChanelCash.size();
    }

    @Override
    public AbstractChanel getChanel(int index) {
        if(ChanelCash == null)
            loadList();
        return ChanelCash.get(index);
    }

    @Override
    public void updateChanel(AbstractChanel ch) throws SQLException {
        ch.update(DBConnection);
    }

    @Override
    public AbstractChanel addChanel(String name) throws SQLException  {
        AbstractChanel ch;
        try {
            ch = Method.createNewChanel(name);
        } catch (Exception ex) {
            Log.log(Level.SEVERE, "Can't create new chanel ", ex);
            throw new SQLException("Can't create new chanel");
        }
        ch.insert(DBConnection);
        try {
            Method.commit();
        } catch (Exception ex) {
            Log.log(Level.SEVERE, "Commit error...", ex);
        }
        loadList();
        return ch;
    }

    @Override
    public void removeChanel(int index) throws SQLException  {
        ChanelCash.get(index).delete(DBConnection);
    }

    @Override
    public String getId() {
        return AbstractMethod.ASPECT_CHANEL;
    }

    AspectChanelSetEditor Editor;
    @Override
    public IMethodAspectEditor getGUIEditor() {
        if(Editor == null)
            Editor = new AspectChanelSetEditor(Method);
        return Editor;
    }
    
    @Override
    public String toString(){
        return Mls.get("Каналы для фотометрирования");
    }

    @Override
    public void commit() throws Exception {
        Editor.updateList();
        Method.commit();
    }

    @Override
    public void updateGUI() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
