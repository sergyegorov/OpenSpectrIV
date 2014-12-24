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

package os4.serv;

import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class FixedColumnTable implements ChangeListener, PropertyChangeListener
{
    private JTable main;
    private JTable fixed;
    private JScrollPane scrollPane;

    final public void update(int fixedColumns, JScrollPane scrollPane){
        this.scrollPane = scrollPane;

        main = ((JTable)scrollPane.getViewport().getView());
        main.setAutoCreateColumnsFromModel( false );
        main.addPropertyChangeListener( this );

        //  Use the existing table to create a new table sharing
        //  the DataModel and ListSelectionModel
        int totalColumns = main.getColumnCount();

        fixed = new JTable();
        fixed.setAutoCreateColumnsFromModel( false );
        fixed.setModel( main.getModel() );
        fixed.setSelectionModel( main.getSelectionModel() );
        fixed.setFocusable( false );

        //  Remove the fixed columns from the main table
        //  and add them to the fixed table
        for (int i = 0; i < fixedColumns; i++) {
            TableColumnModel columnModel = main.getColumnModel();
            TableColumn column = columnModel.getColumn( 0 );
            columnModel.removeColumn( column );
                    fixed.getColumnModel().addColumn( column );
        }

        //  Add the fixed table to the scroll pane
        fixed.setPreferredScrollableViewportSize(fixed.getPreferredSize());
        scrollPane.setRowHeaderView( fixed );
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, fixed.getTableHeader());

        // Synchronize scrolling of the row header with the main table
        scrollPane.getRowHeader().addChangeListener( this );
    }
    
    /*
     *  Specify the number of columns to be fixed and the scroll pane
     *  containing the table.
     */
    public FixedColumnTable(int fixedColumns, JScrollPane scrollPane)
    {
        update(fixedColumns, scrollPane);
    }

    /*
     *  Return the table being used in the row header
     */
    public JTable getFixedTable()
    {
        return fixed;
    }
    
    /*
    *  Implement the ChangeListener
    */
    @Override
    public void stateChanged(ChangeEvent e)
    {
            //  Sync the scroll pane scrollbar with the row header
            JViewport viewport = (JViewport) e.getSource();
            scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }
//
//  Implement the PropertyChangeListener
//
    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        //  Keep the fixed table in sync with the main table
        if ("selectionModel".equals(e.getPropertyName())){
            fixed.setSelectionModel( main.getSelectionModel() );
        }

        if ("model".equals(e.getPropertyName())){
            fixed.setModel( main.getModel() );
        }
    }
}
