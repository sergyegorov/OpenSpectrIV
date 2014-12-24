/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package os4;

import java.awt.Component;
import java.awt.Container;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class Mls {
    private static final Logger Log = Common.getLogger( Mls.class );
    
    public static String get(String txt,Object val){
        return String.format(get(txt), val);
    }
    
    public static String get(String in){
        if(Common.Debug == true){
            if(in.trim().toLowerCase().startsWith("<html>"))
                return "<html>."+in.trim().substring(6);
            else
                return "."+in;   
        }else
            return in;
    }
    
    final static void translate(Border border){
        if(border != null && border instanceof TitledBorder){
            TitledBorder tb = (TitledBorder)border;
            tb.setTitle(Mls.get(tb.getTitle()));
        }
    }
    
    final public static void translate(Container cont){
        if(cont == null)
            return;
        if(cont instanceof JPanel)
            translate(((JPanel)cont).getBorder());
        for(int i = 0;i < cont.getComponentCount();i++){
            Component comp = cont.getComponent(i);
            if(comp instanceof JPanel){
                JPanel jp = (JPanel)comp;
                translate(jp);
            }
            else{
                if(comp instanceof JSplitPane){
                    JSplitPane pan = (JSplitPane)comp;
                    translate((JPanel)pan.getLeftComponent());
                    translate((JPanel)pan.getRightComponent());
                    translate(pan.getBorder());
                    continue;
                }
                if(comp instanceof AbstractButton){
                    AbstractButton b = (AbstractButton)comp;
                    String text = b.getText();
                    b.setText(get(text));
                    continue;
                }
                if(comp instanceof JLabel){
                    JLabel b = (JLabel)comp;
                    String text = b.getText();
                    b.setText(get(text));
                    continue;
                }
                if(comp instanceof JTextComponent ||
                        comp instanceof JScrollPane ||
                        comp instanceof JSpinner)
                    continue;
                Log.log(Level.WARNING,"Element has not founded..." + comp.toString());
            }
        }
    }
}
