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

package os4.dev.method.chanels;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JPanel;
import os4.Common;
import os4.dev.SpectrData;
import os4.dev.SpectrDataChanel;
import os4.dev.method.AbstractChanel;
import os4.dev.method.AbstractMethod;
import os4.dev.method.aspect.AspectChanelSet;
import os4.serv.MathTools;
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class MultiLineSpectrChanel implements AbstractChanel{
    int Id;
    String Name;
    float Ly;
    int Sn;
    byte[] Profile;
    int ProfileMidle;
    int FindMaxMinType = 2;
    float MaxMinPlusMinus = 1;
    
    public int getFindMaxMinType(){
        return FindMaxMinType;
    }
    
    public void setFindMaxMinType(int find){
        FindMaxMinType = find;
    }
    
    public float getMaxMinPlusMinus(){
        return MaxMinPlusMinus;
    }
    
    public void setMaxMinPlusMinus(float findPlusMinus){
        MaxMinPlusMinus = findPlusMinus;
    }
    
    public void setProfile(double[] profile,int profile_midle){
        double min = profile[0];
        double max = min;
        for(int i = 1;i<profile.length;i++){
            if(profile[i] < min)
                min = profile[i];
            if(profile[i] > max)
                max = profile[i];
        }
        double dlt = max-min;
        Profile = new byte[profile.length];
        for(int i = 0;i<profile.length;i++)
            Profile[i] = (byte)(Byte.MAX_VALUE*(profile[i]-min)/dlt);
        ProfileMidle = profile_midle;
    }
    
    public byte[] getProfile(){
        return Profile;
    }
    
    public int getProfileMidle(){
        return ProfileMidle;
    }
    
    public String getName(){
        return Name;
    }
    
    public void setName(String name){
        Name = name;
    }
    
    public float getLy(){
        return Ly;
    }
    
    public void setLy(float ly){
        Ly = ly;
    }
    
    public int getSn(){
        return Sn;
    }
    
    public void setSn(int sn){
        Sn = sn;
    }
    
    public MultiLineSpectrChanel(String name,AbstractMethod method){
        Name = name;
        Ly = 3000;
        Method = method;
    }
    
    AspectChanelSet ChSet;
    public void init(AspectChanelSet ch){
        ChSet = ch;
    }
    
    static final String TYPE = "MLSCh";
    final void initByExtraData(byte[] extra_data) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(extra_data);
        DataInputStream dis = new DataInputStream(bais);
        int ver = StreamTools.versionBlockBegin(dis, 1, 1);
        ProfileMidle = dis.readInt();
        int len = dis.readInt();
        Profile = new byte[len];
        dis.read(Profile);
        FindMaxMinType = dis.readInt();
        MaxMinPlusMinus = dis.readFloat();
        StreamTools.versionBlockEnd(dis);
        //*/
    }
    
    byte[] getExtraData() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        StreamTools.versionBlockBegin(dos, 1);
        dos.writeInt(ProfileMidle);
        dos.writeInt(Profile.length);
        dos.write(Profile);
        dos.writeInt(FindMaxMinType);
        dos.writeFloat(MaxMinPlusMinus);
        StreamTools.versionBlockEnd(dos);
        dos.flush();
        return  baos.toByteArray();
    }
    
    @Override
    public SpectrDataChanel getValues(SpectrData data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    static PreparedStatement InsertSQL;
    @Override
    public void insert(Connection con) throws SQLException{
        if(InsertSQL == null){
            InsertSQL = con.prepareStatement(Common.MainProperties.
                    getProperty("METHOD.ASPECT.CHANEL_SET.INSERT"));
        }
        InsertSQL.setString(1, TYPE);
        InsertSQL.setString(2, Name);
        InsertSQL.setFloat(3, Ly);
        InsertSQL.setInt(4, Sn);
        try {
            InsertSQL.setBytes(5, getExtraData());
        } catch (IOException ex) {
            throw new SQLException(ex);
        }
        
        InsertSQL.executeUpdate();
    }

    static PreparedStatement UpdateSQL;
    @Override
    public void update(Connection con) throws SQLException {
        if(UpdateSQL == null){
            UpdateSQL = con.prepareStatement(Common.MainProperties.
                    getProperty("METHOD.ASPECT.CHANEL_SET.UPDATE"));
        }
        UpdateSQL.setString(1, TYPE);
        UpdateSQL.setString(2, Name);
        UpdateSQL.setFloat(3, Ly);
        UpdateSQL.setInt(4, Sn);
        try {
            UpdateSQL.setBytes(5, getExtraData());
        } catch (IOException ex) {
            throw new SQLException(ex);
        }
        
        UpdateSQL.setInt(6, Id);
        
        int ret = UpdateSQL.executeUpdate();
        if(ret != 1)
            throw new SQLException("Wrong number of updated rows:"+ret);
    }

    @Override
    public void load(ResultSet rs) throws SQLException {
        Id = rs.getInt("id");
        String type = rs.getString("type");
        if(type.equals(TYPE) == false)
            throw new SQLException("Wrong object type:'"+type+"' but need '"+TYPE+"'");
        Name = rs.getString("name");
        Ly = rs.getFloat("ly");
        Sn = rs.getInt("sn");
        byte[] extra_data = rs.getBytes("extra_data");
        try {
            initByExtraData(extra_data);
        } catch (IOException ex) {
            throw new SQLException(ex);
        }
    }

    MultiLineSpectrChanelEditor Editor = null;
    AbstractMethod Method;
    @Override
    public JPanel getEditor() {
        if(Editor == null)
            Editor = new MultiLineSpectrChanelEditor(this,Method);
        return Editor;
    }

    @Override
    public void delete(Connection con) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public String toString(){
        return Name +" "+ MathTools.getGoodValue(Ly, 2);
    }

    @Override
    public void commit() throws Exception {
        if(ChSet == null)
            throw new Exception("Call: public void init(AspectChanelSet ch)");
        ChSet.commit();
    }

    @Override
    public int getId() {
        return Id;
    }
}
