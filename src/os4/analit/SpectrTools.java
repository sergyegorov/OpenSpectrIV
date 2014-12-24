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

package os4.analit;

import java.util.ArrayList;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class SpectrTools {
    static public float[] moveToZerro(float[] src){
        float min = 0;
        int base = src.length-1;
        for(int i = 0;i<16;i++)
            min += src[base-i];
        min /= 16;
        
        float[] ret = new float[src.length];
        for(int i = 0;i<ret.length;i++)
            ret[i] = src[i] - min;
        
        return ret;
    }
    
    static public float[] filterPicOnly(float[] spectr_src){
        float[] spectr = new float[spectr_src.length];
        ArrayList<Integer> pics_coordinates = findPic(spectr_src, 6);
        for(Integer pic : pics_coordinates){
            int middle = pic;
            for(int i = 0;i<10;i++){
                int ind = middle+i;
                if(ind < spectr_src.length-1 && spectr_src[ind] >= spectr_src[ind+1])
                    spectr[ind] = spectr_src[ind];
                else{
                    spectr[ind] = spectr_src[ind];
                    break;
                }
            }
            for(int i = 0;i<10;i++){
                int ind = middle-i;
                if(ind > 1 && spectr_src[ind] >= spectr_src[ind-1])
                    spectr[ind] = spectr_src[ind];
                else{
                    spectr[ind] = spectr_src[ind];
                    break;
                }
            }
        }
        return spectr;
    }
    
    static public ArrayList<Integer> findPic(float[] data,int min_width){
        ArrayList<Integer> ret = new ArrayList<>();
        for(int i = min_width;i<data.length-min_width;i++){
            if(data[i-1] < data[i] && data[i] >= data[i+1] && data[i+1] > data[i+2]){
                int left = 1;
                for(int j = 1;j<min_width;j++) {
                    if(data[i-j-1] < data[i-j])
                        left ++;
                    else
                        break;
                }
                int right = 1;
                for(int j = 1;j<min_width;j++) {
                    if(data[i+j] > data[i+j+1])
                        right ++;
                    else
                        break;
                }
                if(right + left > min_width)
                    ret.add(i);
                i += right;
            }
        }
        return ret;
    }
}
