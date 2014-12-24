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

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class Rectangle3D {
    public float X,Y,Z;
    public float Width,Height,Depth;
    public Rectangle3D(){
        
    }
    
    public Rectangle3D(Rectangle3D from){
        X = from.X;
        Y = from.Y;
        Z = from.Z;
        Width = from.Width;
        Height = from.Height;
        Depth = from.Depth;
    }
    
    public Rectangle3D(float x,float y,float z,
            float width,float height,float depth){
        X = x;
        Y = y;
        Z = z;
        Width = width;
        Height = height;
        Depth = depth;
    }
    
    public float getRight(){return X+Width;}
    public float getTop(){return Y+Height;}
    public float getBack(){return Z+Depth;}
    
    public void setRight(float val){Width = val-X;}
    public void setTop(float val){Height = val-Y;}
    public void setBack(float val){Depth = val-Z;}
    
    public void fitIn(Rectangle3D r){
        fitIn(new Point3D(r.X,r.Y,r.Z));
        fitIn(new Point3D(r.getRight(),r.getTop(),r.getBack()));
    }
    
    public void fitIn(Point3D p){
        if(p.X < X)
            X = p.X;
        
        if(p.Y < Y)
            Y = p.Y;
        
        if(p.Z < Z)
            Z = p.Z;
        
        float v = getRight();
        if(p.X > v)
            setRight(p.X);
        
        v = getTop();
        if(p.Y > v)
            setTop(p.Y);
        
        v = getBack();
        if(p.Z > v)
            setBack(p.Z);
    }
}
