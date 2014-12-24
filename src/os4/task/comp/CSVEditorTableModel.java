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

package os4.task.comp;

import java.io.File;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import os4.Mls;
import os4.serv.Dialogs;
import os4.serv.MathTools;
import os4.serv.StreamTools;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class CSVEditorTableModel extends AbstractTableModel {
    ArrayList<String> ColumnNames;
    ArrayList<String> RowNames;
    ArrayList<ArrayList<String>> RowValues;
    CSVEditor EditGui;
    File SourceFile;
    public CSVEditorTableModel(File csv_file,CSVEditor editor_gui){
        SourceFile = csv_file;
        EditGui = editor_gui;
        ColumnNames = new ArrayList<>();
        RowNames = new ArrayList<>();
        RowValues = new ArrayList<>();
        String txt = StreamTools.readText(csv_file);
        
        if(txt == null || txt.trim().length() < 2)
            return;
        
        String[] lines = txt.split("\n");
        String[] headers = lines[0].trim().split(";");
        for(int i = 1;i<headers.length;i++){
            String header_name = headers[i].trim();
            if(header_name.equals("\r"))
                continue;
            ColumnNames.add(header_name);
        }
        for(int r = 1;r<lines.length;r++){
            String[] values = lines[r].trim().split(";");
            RowNames.add(values[0].trim());
            ArrayList<String> row_value_collection = new ArrayList<>();
            RowValues.add(row_value_collection);
            for(int c = 1;c<values.length;c++){
                String value = values[c].trim();
                if(value.equals("\r"))
                    continue;
                row_value_collection.add(value);
            }
        }
    }
    
    @Override
    public int getRowCount() {
        return RowNames.size();
    }

    @Override
    public int getColumnCount() {
        return ColumnNames.size()+1;
    }

    @Override
    public String getColumnName(int column) {
        if(column == 0)
            return Mls.get("Имя пробы");
        return ColumnNames.get(column-1);
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try{
           if(columnIndex == 0)
               return RowNames.get(rowIndex);
            return RowValues.get(rowIndex).get(columnIndex-1);
        }catch(Exception ex){
            return "-";
        }
    }
    
    public ConValue getValueAt(String probName,String elementName) throws Exception{
        for(int column = 0;column < ColumnNames.size();column ++){
            String column_name = ColumnNames.get(column);
            if(column_name.equals(elementName)){
                for(int row = 0;row < RowNames.size();row ++){
                    String row_name = RowNames.get(row);
                    if(row_name.equals(probName)){
                        return new ConValue((String)getValueAt(row,column));
                    }
                }
                break;
            }
        }
        return null;
    }
    
    public class ConValue{
        public double Concentration;
        public boolean IsPreliminary;
        public ConValue(String txt_value) throws Exception{
            String value = txt_value.trim();
            String f_value = value;
            if(value.length() > 0){
                char c = value.charAt(0);
                switch(c){
                    case '~':
                        IsPreliminary = true;
                        f_value = value.substring(1).trim();
                        break;
                }
                Concentration = MathTools.parseFloat(f_value);
                if(Concentration < 0)
                    throw new Exception("Значение меньше 0");
                if(Concentration > 100)
                    throw new Exception("Значение концентрации больше 100");
            }
        }
    }
    
    @Override
    public void setValueAt(Object value_src, int row, int column) {
        try{
            String value = (String)value_src;
            if(column > 0){
                ConValue val = new ConValue(value);
                ArrayList<String> row_data = RowValues.get(row);
                column --;
                while(row_data.size() <= column)
                    row_data.add("0");
                RowValues.get(row).set(column, value);
            } else {
                RowNames.set(row, value);
            }
            commit();
        }catch(Exception ex){
            if(column != 0)
                Dialogs.errorWarnningMLS("Неправильное заначение для концентрации:"+ex.getMessage());
            else
                Dialogs.errorWarnningMLS("Неправильное заначение для имени пробы:"+ex.getMessage());
        }
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
       return EditGui != null;
    }
    
    public void addColumn(String element_name){
        ColumnNames.add(element_name);
        for(ArrayList<String> row : RowValues)
            row.add("");
        commit();
    }
    
    public void addRow(String name){
        RowNames.add(name);
        ArrayList<String> row_data = new ArrayList<>();
        for(int i = 0;i<ColumnNames.size();i++)
            row_data.add("");
        RowValues.add(row_data);
        commit();
    }
    
    public void commit(){
        ArrayList<String> txt = new ArrayList<>();
        txt.add(";");
        for(String header : ColumnNames)
            txt.add(header + ";");
        txt.add("\r\n");
        for(int row = 0;row<RowValues.size();row++){
            txt.add(RowNames.get(row)+";");
            for(String values : RowValues.get(row))
                txt.add(values+";");
            txt.add("\r\n");
        }
        StreamTools.writeText(SourceFile, txt);
    }
}
