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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import os4.Common;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public abstract class ListSimpleDriver<E> implements Collection{
    private static final Logger Log = Common.getLogger( ListSimpleDriver.class );
    
    JList GUIList;
    JButton NewBtn,DeleteBtn;
    boolean Initing;
    public ListSimpleDriver(){
    }
    
    public void init(JList list,JButton new_btn,JButton delete_btn){
        GUIList = list;
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        GUIList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try{
                    if(Initing)
                        return;
                    if(e.getFirstIndex() < 0)
                        selectCallBack(null);
                    else
                        selectCallBack(Data.get(e.getFirstIndex()));
                } catch(Exception ex){
                    Log.log(Level.SEVERE,"Lelecting list error",ex);
                }
            }
        });
        NewBtn = new_btn;
        NewBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    E new_obj = createNewCallBack();
                    if(new_obj == null)
                        return;
                    add(new_obj);
                    upToDate(null);
                } catch(Exception ex){
                    Log.log(Level.SEVERE,"Creating new object error: ",ex);
                }
            }
        });
        DeleteBtn = delete_btn;
        DeleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    selectCallBack(Data.get(GUIList.getSelectedIndex()));
                } catch(Exception ex){
                    Log.log(Level.SEVERE,"Creating new object error: ",ex);
                }
            }
        });
    }
     
    boolean ListInited = false;
    public void upToDate(String new_selection){
        try{
            Initing = true;
            Object selected_val = GUIList.getSelectedValue();
            DefaultListModel<E> model = null;
            if(ListInited == false)
                model = new DefaultListModel<>();
            else
                model = (DefaultListModel)GUIList.getModel();//new DefaultListModel<>();
            for(int i = 0;i<Data.size();i++){
                E element = Data.get(i);
                if(i < model.size())
                    model.setElementAt(element, i);
                else
                    model.addElement(element);
            }
            GUIList.setModel(model);
            try{
                if(new_selection != null)
                    GUIList.setSelectedValue(new_selection, true);
                else{
                    if(selected_val != null)
                        GUIList.setSelectedValue(selected_val, true);
                }
            }catch(Exception ex){}
        } finally {
            Initing = false;
        }
    }
    
    public abstract E createNewCallBack() throws Exception;
    public abstract void removeCallBack(E val) throws Exception;
    public abstract void selectCallBack(E val) throws Exception;
    
    ArrayList<E> Data = new ArrayList<>();
    
    public E get(int index){
        return Data.get(index);
    }
    
    @Override
    public void clear(){
        Data.clear();
    }

    @Override
    public int size() {
        return Data.size();
    }

    @Override
    public boolean isEmpty() {
        return Data.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return Data.contains(o);
    }

    @Override
    public Iterator iterator() {
        return Data.iterator();
    }

    @Override
    public Object[] toArray() {
        return Data.toArray();
    }

    @Override
    public Object[] toArray(Object[] a) {
        return Data.toArray(a);
    }

    @Override
    public boolean add(Object e) {
        return Data.add((E)e);
    }

    @Override
    public boolean remove(Object o) {
        return Data.remove(o);
    }

    @Override
    public boolean containsAll(Collection c) {
        return Data.containsAll(c);
    }

    @Override
    public boolean addAll(Collection c) {
        return Data.addAll(c);
    }

    @Override
    public boolean removeAll(Collection c) {
        return Data.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection c) {
        return Data.retainAll(c);
    }
}
