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

package os4.dev;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import os4.serv.Point3D;
import os4.serv.Rectangle3D;
import os4.serv.StreamTools;
import os4.task.comp.CustomDrawingConverter;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class CustomDrawing {
    public static final int DrawColorSpectrLight = 0;
    public static final int DrawColorSpectrDark = 1;

    class SegmentedLine{
        public int DrawColor;
        public float BaseLevel; 
        public ArrayList<Point3D> Points = new ArrayList<>();
        public void updateMax(Rectangle3D rect){
            for(Point3D point : Points)
                rect.fitIn(point);
        }
        
        public SegmentedLine(int c,float base_level){
            DrawColor = c;
            BaseLevel = base_level;
        }
        
        public SegmentedLine(DataInputStream str) throws IOException{
            load(str);
        }
        
        public void add(Point3D point){
            Points.add(point);
        }
        
        public void save(DataOutputStream dos) throws IOException{
            StreamTools.versionBlockBegin(dos, 1);
            dos.writeInt(DrawColor);
            dos.writeFloat(BaseLevel);
            dos.writeInt(Points.size());
            int index = 0;
            for(Point3D point : Points)
                point.saveARC(dos,0,0,0,1,1,1,index++);
            StreamTools.versionBlockEnd(dos);
        }
        
        final public void load(DataInputStream dis) throws IOException{
            int ver = StreamTools.versionBlockBegin(dis, 1, 1);
            DrawColor = dis.readInt();
            BaseLevel = dis.readFloat();
            int num = dis.readInt();
            for(int i = 0;i<num;i++){
                Point3D p = new Point3D();
                p.loadARC(dis,0,0,0,1,1,1);
                Points.add(p);
            }
            StreamTools.versionBlockEnd(dis);
        }
    }
    
    ArrayList<SegmentedLine> Lines = new ArrayList<>();
    public Rectangle3D getDimension(){
        Rectangle3D ret = new Rectangle3D(0,0,0,
                1,1,1);
        for(SegmentedLine line : Lines)
            line.updateMax(ret);
        return ret;
    }
    
    public int addLine(int c,float base_level){
        Lines.add(new SegmentedLine(c,base_level));
        return Lines.size()-1;
    }
    
    public void addPoint(int segment,Point3D point){
        Lines.get(segment).add(point);
    }
    
    public CustomDrawingConverter getConverter(Rectangle3D view,Dimension panel_size){
        double kx = panel_size.width / view.Width;
        double ky = (panel_size.height-10) / view.Height;
        double x0 = view.X;
        double y0 = view.Y;
        return new CustomDrawingConverter(kx, ky, x0, y0, 0, 5, panel_size.height);
    }
    /**
     * This method draws all information and returns 
     * @param g
     * @param view
     * @param disp
     * @param palitra
     * @return 
     */
    public void drawView(Graphics g,CustomDrawingConverter view,
            SpectrDispers disp,Color palitra[]){
        double kx = view.Kx;
        double ky = view.Ky;
        double x0 = view.X0;
        double y0 = view.Y0;
        int height = view.Height;
        for(SegmentedLine line : Lines) {
            Color c = palitra[line.DrawColor];
            g.setColor(c);
            Point3D p = line.Points.get(0);
            int sensor = disp.findSensorByGlobalPixel(p.X);
            int px = (int)((p.getX(disp, sensor)-x0)*kx);
            int py = height - (int)((p.Y-y0)*ky) + 5;
            for(int i = 1;i<line.Points.size();i++){
                p = line.Points.get(i);
                int x = (int)((p.getX(disp,sensor)-x0)*kx);
                int y = height - (int)((p.Y-y0)*ky) + 5;
                g.drawLine(px, py, x, y);
                px = x;
                py = y;
            }
        }
    }
    
    public void load(DataInputStream dis) throws IOException{
        int ver = StreamTools.versionBlockBegin(dis, 1, 1);
        int count = dis.readInt();
        for(int i = 0;i<count;i++)
            Lines.add(new SegmentedLine(dis));
        StreamTools.versionBlockEnd(dis);
    }
    
    public void save(DataOutputStream dos) throws IOException{
        StreamTools.versionBlockBegin(dos, 1);
        dos.writeInt(Lines.size());
        for(SegmentedLine segm : Lines)
            segm.save(dos);
        StreamTools.versionBlockEnd(dos);        
    }
}
