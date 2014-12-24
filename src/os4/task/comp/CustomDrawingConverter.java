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

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class CustomDrawingConverter {
    public double Kx,Ky,X0,Y0;
    public int ShiftX0,ShiftY0;
    public int Height;
    public CustomDrawingConverter(double kx,double ky,double x0,double y0,
            int shift_x0,int shift_y0,
            int height){
        Kx = kx;
        Ky = ky;
        X0 = x0;
        Y0 = y0;
        ShiftX0 = shift_x0;
        ShiftY0 = shift_y0;
        Height = height;
    }
    
    public CustomDrawingConverter(CustomDrawingConverter to_copy){
        Kx = to_copy.Kx;
        Ky = to_copy.Ky;
        X0 = to_copy.X0;
        Y0 = to_copy.Y0;
        ShiftX0 = to_copy.ShiftX0;
        ShiftY0 = to_copy.ShiftY0;
        Height = to_copy.Height;
    }
    
    //int px = (int)((p.getX(disp, sensor)-x0)*kx);
    public double getVirtalCoodinateX(int screen_x){
        return (screen_x-ShiftX0)/Kx + X0;
    }
    
    public int getScreenCoodinateX(double virtual_x){
        return (int)((virtual_x-X0)*Kx + ShiftX0);
    }

    //int py = panel_size.height - (int)((p.Y-y0)*ky) + 5;
    public double getVirtalCoodinateY(int screen_y){
        return (Height - screen_y + ShiftY0)/Ky + Y0;
    }
    
    public int getScreenCoodinateY(double virtual_y){
        return (int)(Height - (virtual_y-Y0)*Ky + ShiftY0);
    }
}
