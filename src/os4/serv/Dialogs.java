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

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import os4.Common;
import os4.Mls;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class Dialogs {
    public static final boolean showDialogWithCustomPanelMLS(String title,JPanel pan){
        return (new CustomPanelDialogOk(Mls.get(title),pan)).showDialog();
    }
    
    public static final void errorWarnningMLS(String msg){
        JOptionPane.showMessageDialog(Common.MainWindow, Mls.get(msg),
                Mls.get("Wait!"),JOptionPane.WARNING_MESSAGE);
    }
    
    public static final String getStringMLS(String msg,String title){
        String val = JOptionPane.showInputDialog(Common.MainWindow, 
                Mls.get(msg),Mls.get(title),JOptionPane.INFORMATION_MESSAGE);
        return val;
    }
    
    public static final boolean getConfirmYesNoMLS(String msg,String title){
        return JOptionPane.showConfirmDialog(Common.MainWindow, Mls.get(msg),
                Mls.get(title),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static final boolean getConfirmYesNoMLS(String msg,String title,String name){
        return JOptionPane.showConfirmDialog(Common.MainWindow, Mls.get(msg, name),
                Mls.get(title),JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
