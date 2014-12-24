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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class Vector {
    public float[] Components;
    
    public Vector(short val){
        Components = new float[1];
        Components[0] = val;
    }
    
    public Vector(float val){
        Components = new float[1];
        Components[0] = val;
    }
    
    public float getModule(){
        double ret = 0;
        for(float comp : Components)
            ret += comp*comp;
        return (float)Math.sqrt(ret);
    }
    
    public float asFloat(){
        return Components[0];
    }
    
    public Vector(DataInputStream dis) throws IOException{
        load(dis);
    }
    
    final public void save(DataOutputStream os) throws IOException{
        os.writeShort(Components.length);
        for(float val : Components)
            os.writeFloat(val);
    }
    
    final public void load(DataInputStream is) throws IOException{
        short len = is.readShort();
        Components = new float[len];
        for(int i = 0;i<len;i++)
            Components[i] = is.readFloat();
    }
}
