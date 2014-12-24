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

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class EverCalc{
    long[] Sum;
    //long[] Sum10;
    public EverCalc(int[] dim){
        Sum = new long[dim.length+1];
        //Sum10 = new long[dim.length*10+1];
        long sum = 0;
        for(int i = 0;i<dim.length;i++){
            Sum[i] = sum;
            sum += dim[i];
        }
        Sum[dim.length] = sum;
        /*int dim10[] = new int[dim.length*10+1];
        for(int i = 0;i<Sum10.length-10;i++){
            double position = i/10.0;
            int from = (int)position;
            int to = from+1;
            double micro_step = position - from;
            double micro_step1 = 1 - micro_step;
            if(i < dim.length)
                dim10[i] = (int)(dim[from]*micro_step1 + dim[to]*micro_step);
            else
                dim10[i] = (int)(dim[from]);
        }
        sum = 0;
        for(int i = 0;i<dim.length;i++){
            Sum10[i] = sum;
            sum += dim10[i];
        }*/
    }

    public long calcEver(int from,int to){
        return Sum[to+1]-Sum[from];
    }

    public long calcEver(double from,double to){
        return calcEver((int)from,(int)to);//return Sum10[(int)(to*10+1)] - Sum10[(int)(from*10)];
    }
}
