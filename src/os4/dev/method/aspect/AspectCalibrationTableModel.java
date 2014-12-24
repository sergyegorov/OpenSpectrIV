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

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import os4.Common;
import os4.Mls;
import os4.dev.method.AbstractFormula;
import os4.dev.method.AbstractMethod;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class AspectCalibrationTableModel extends AbstractTableModel{
    static final Logger Log = Common.getLogger(AspectCalibrationTableModel.class);
    public class Row{
        public int ProbId;
        public int MeasuringId;
        public Date MeasuringDate;
        public String Name,Standart,Prob,Comments;
        public Row(ResultSet rs) throws SQLException{
            ProbId = rs.getInt(1);
            Name = rs.getString(2);
            Standart = rs.getString(3);
            Prob = rs.getString(4);
        }
        public Row(int probId,ResultSet rs) throws SQLException{
            ProbId = probId;
            MeasuringId = rs.getInt(1);
            MeasuringDate = rs.getDate(2);
            Name = MeasuringDate.toString();
            Comments = rs.getString(3);
        }
    }
    
    public class Column{
        public int Id;
        public String FormulaName,Element;
        byte[] ExtraData;
        public Column(ResultSet rs) throws SQLException{
            Id = rs.getInt(1);
            FormulaName = rs.getString(2);
            Element = rs.getString(3);
            ExtraData = rs.getBytes(4);
        }
        
        @Override
        public String toString(){
            return Element.concat("[").concat(FormulaName).concat("]");
        }
        
        AbstractFormula Formula;
        public AbstractFormula getFormula() throws Exception{
            if(Formula == null){
                Formula = Method.createNewFormula(Element, FormulaName);
                Formula.setExtraData(Id,ExtraData);
            }
            return Formula;
        }
    }
    ArrayList<Row> Rows = new ArrayList<>();
    ArrayList<Column> Columns = new ArrayList<>();

    public AbstractFormula getFormula(int column) throws Exception{
        return Columns.get(column).getFormula();
    }
    
    AbstractMethod Method;
    Connection DBConnection;
    PreparedStatement SelectFormulas;
    PreparedStatement SelectProbs,SelectMeasurings;
    public AspectCalibrationTableModel(AbstractMethod method) throws Exception{
        Method = method;
        DBConnection = method.getDBConnection();
        SelectFormulas = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.SELECT.FORMULAS"));
        SelectProbs = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.SELECT.PROB"));
        SelectMeasurings = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.SELECT.MEASURINGS"));
        InsertFormulas = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.INSERT.FORMULAS"));
        InsertProb = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.INSERT.PROB"));
        InsertMeasuring = DBConnection.prepareStatement(Common.MainProperties.getProperty("METHOD.ASPECT.CALIBR.INSERT.MEASURING"));
    }
    
    PreparedStatement InsertFormulas;
    public void insertFormula(AbstractFormula formula) throws SQLException{
        InsertFormulas.setString(1, formula.getElementName());
        InsertFormulas.setString(2, formula.getFormulaName());
        try {
            InsertFormulas.setBlob(3,new ByteArrayInputStream(formula.getExtraData()));
        } catch (Exception ex) {
            Common.getLogger(AspectCalibrationTableModel.class).log(Level.SEVERE, null, ex);
            throw new SQLException(ex.toString());
        }
        if(InsertFormulas.executeUpdate() == 0)
            throw new SQLException("No insert...");
        isUpdated = false;
        fireTableStructureChanged();
    }
    
    PreparedStatement InsertProb;
    public void insertProb(String complect_path,String prob_name) throws SQLException{
        InsertProb.setString(1, prob_name);
        InsertProb.setString(2, complect_path);
        InsertProb.setString(3, prob_name);
        if(InsertProb.executeUpdate() == 0)
            throw new SQLException("No insert...");
        isUpdated = false;
    }
    
    PreparedStatement InsertMeasuring;
    public void insertMeasuringForRow(int row) throws SQLException{
        int prob_id = Rows.get(row).ProbId;
        InsertMeasuring.setInt(1, prob_id);
        InsertMeasuring.setDate(2, new Date(System.currentTimeMillis()));
        if(InsertMeasuring.executeUpdate() == 0)
            throw new SQLException("No insert...");
        isUpdated = false;
    }
    
    boolean isUpdated = false;
    public void upDate(){
        if(isUpdated)
            return;
        try{
        	if(Columns == null)
        		return;
            isUpdated = true;
            
            Columns.clear();
            Rows.clear();
            
            ResultSet formula_rs = SelectFormulas.executeQuery();
            while(formula_rs.next())
                Columns.add(new Column(formula_rs));
            
            ResultSet prob_rs = SelectProbs.executeQuery();
            while(prob_rs.next()){
                Row r = new Row(prob_rs);
                Rows.add(r);
                SelectMeasurings.setInt(1, r.ProbId);
                ResultSet measurings_rs = SelectMeasurings.executeQuery();
                while(measurings_rs.next()){
                    Rows.add(new Row(r.ProbId,measurings_rs));
                }
            }
        } catch(Exception ex){
            Log.log(Level.SEVERE,"Wrong select for prob list",ex);
        }
    }
    
    @Override
    public int getRowCount() {
        upDate();
        return Rows.size();
    }
    
    @Override
    public String getColumnName(int column){
        if(column == 0)
            return Mls.get("Пробы");
        return Columns.get(column-1).toString();
    }

    @Override
    public int getColumnCount() {
        upDate();
        int ret = Columns.size()+1;
        return ret;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0){
            Row r = Rows.get(rowIndex);
            if(r.Standart != null)
                return r.Name;
            else
                return "  "+r.Name;
        }
        columnIndex --;
        return "??????";
    }
}
