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

package os4.dev.method.formula;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JPanel;
import os4.Common;
import os4.dev.method.AbstractFormula;
import os4.dev.method.AbstractMethod;
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class FormulaGOST extends AbstractFormula{
    AbstractMethod Method;
    public float TimeFrom = 0,TimeTo = 1000,ConFrom = 0,ConTo = 100;
    public int CalibrFk = 0;
    public int ChanelAnalitId,ChanelCompId;
    
    public FormulaGOST(String element,String formula,AbstractMethod method){
        super(element,formula);
        Method = method;
    }

    PreparedStatement UpdateStatement;
    public void update(Connection con) throws Exception{
        if(UpdateStatement == null)
            UpdateStatement = con.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.UPDATE.FORMULAS"));
        UpdateStatement.setString(1,getElementName());
        UpdateStatement.setString(2,getFormulaName());
        UpdateStatement.setBlob(3, new ByteArrayInputStream(getExtraData()));
        UpdateStatement.setInt(4,Id);
        if(UpdateStatement.executeUpdate() != 1)
            throw new SQLException("No data to update");
    }
    
    @Override
    public byte[] getExtraData() throws Exception {
        ByteArrayOutputStream byte_stream = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(byte_stream)) {
            StreamTools.versionBlockBegin(dos, 1);
            dos.writeFloat(TimeFrom);
            dos.writeFloat(TimeTo);
            dos.writeFloat(ConFrom);
            dos.writeFloat(ConTo);
            dos.writeInt(CalibrFk);
            dos.writeInt(ChanelAnalitId);
            dos.writeInt(ChanelCompId);
            StreamTools.versionBlockEnd(dos);
            dos.flush();
        }
        return byte_stream.toByteArray();
    }

    int Id;
    @Override
    public void setExtraData(int id,byte[] data) throws Exception {
        Id = id;
        if(data.length == 1 && data[0] == 0)
            return;
        ByteArrayInputStream byte_stream = new ByteArrayInputStream(data);
        try(DataInputStream dis = new DataInputStream(byte_stream)){
            StreamTools.versionBlockBegin(dis, 1, 1);
            TimeFrom = dis.readFloat();
            TimeTo = dis.readFloat();
            ConFrom = dis.readFloat();
            ConTo = dis.readFloat();
            CalibrFk = dis.readInt();
            ChanelAnalitId = dis.readInt();
            ChanelCompId = dis.readInt();
            StreamTools.versionBlockEnd(dis);
        }
    }

    FormulaGOSTEditor Editor;
    @Override
    public JPanel getEditor() {
        if(Editor == null)
            Editor = new FormulaGOSTEditor(this,Method);
        return Editor;
    }
}