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
public class SpectrPatternFinder {
    static public Function findPattern(float[] spectr_src,float[] pattern,
            int active_pixel_from,int active_pixel_to,
            int max_value,int max_shift) throws OS4AnalitException{
        if(spectr_src.length == pattern.length){
            //float spectr[] = SpectrTools.filterPicOnly(spectr_src);
            float spectr[] = spectr_src;
            double[] x = {0,spectr.length};
            boolean[] en = { true, true };

            Function fk_found = null;
            double corel_found = 0;
            int active_len = spectr.length-max_shift*2;
            for(int sh = -max_shift;sh<max_shift;sh++){
                double[] y = {x[0]+sh,x[1]+sh};
                Function fk = new Function(Function.FType.Poly2, en, x, y);
                double cand = corel(spectr,pattern,active_pixel_from+max_shift*5,active_pixel_to-max_shift*5,
                        fk,max_value);
                if(cand > corel_found){
                    fk_found = fk;
                    corel_found = cand;
                }
            }
            
            if(fk_found == null)
                return null;
            
            double x2[] = {0, spectr.length/3, spectr.length*2/3, spectr.length};
            double y2[] = {
                fk_found.calcDirect(x2[0]),
                fk_found.calcDirect(x2[1]),
                fk_found.calcDirect(x2[2]),
                fk_found.calcDirect(x2[3])};
            boolean en2[] = {true,true,true,true};
            fk_found = new Function(Function.FType.Poly2,en2,x2,y2);
            
            //return fk_found;//*
            int dlt = 8;
            active_pixel_from += (int)Math.abs(fk_found.K[0])+dlt*2;
            active_pixel_to -= ((int)Math.abs(fk_found.K[0])+dlt*2);

            int left_to = active_pixel_to/2;
            active_len = (active_pixel_to-active_pixel_from);
            int right_from = active_pixel_to-active_len/2;            
            int center1_center = active_pixel_from + active_len/3;
            int center2_center = active_pixel_to - active_len/3;
            int center_len = active_len/5;
            //double left_found = y2[0];
            //double center1 = y2[1];
            //double center2 = y2[2];
            //double right_found = y2[3]; 
            int founds = 1;
            dlt = 1;
            for(int i = 0;i<10 && founds > 0;i++){//while(dlt > 0 && found > 0){
                founds = 0;
                Function fk = checkForNewK(en2,x2,y2,spectr,pattern,
                        active_pixel_from,left_to,
                        fk_found,max_value,0);
                if(fk != null){
                    fk_found = fk;
                    founds ++;
                }
                
                fk = checkForNewK(en2,x2,y2,spectr,pattern,
                        right_from,active_pixel_to,
                        fk_found,max_value,3);
                if(fk != null){
                    fk_found = fk;
                    founds ++;
                }
                
                fk = checkForNewK(en2,x2,y2,spectr,pattern,
                        center1_center-center_len,center1_center+center_len,
                        fk_found,max_value,1);
                if(fk != null){
                    fk_found = fk;
                    founds ++;
                }
                
                fk = checkForNewK(en2,x2,y2,spectr,pattern,
                        center2_center-center_len,center2_center+center_len,
                        fk_found,max_value,2);
                if(fk != null){
                    fk_found = fk;
                    founds ++;
                }
                /*founds = 0;

                double corel_found_left = corel(spectr,pattern,
                    active_pixel_from,left_to,
                            fk_found,max_value);
                double left_found = y2[0];
                for(int sh = -dlt;sh<=dlt;sh++){
                    if(sh == 0)
                        continue;
                    double[] y = {y2[0]+sh,y2[1],y2[2],y2[3]};
                    Function fk = new Function(Function.FType.Poly2, en2, x2, y);
                    double cand = corel(spectr,pattern,active_pixel_from,left_to,
                            fk,max_value);
                    if(cand > corel_found_left){
                        left_found = y2[0]+sh;
                        //fk_found = fk;
                        corel_found_left = cand;
                        founds ++;
                    }
                }
                y2[0] = left_found;

                double corel_found_right = corel(spectr,pattern,
                    right_from,active_pixel_to,
                            fk_found,max_value);
                double right_found = y2[3];
                for(int sh = -dlt;sh<=dlt;sh++){
                    if(sh == 0)
                        continue;
                    double[] y = {y2[0],y2[1],y2[2],y2[3]+sh};
                    Function fk = new Function(Function.FType.Poly2, en2, x2, y);
                    double cand = corel(spectr,pattern,right_from,active_pixel_to,
                            fk,max_value);
                    if(cand > corel_found_right){
                        right_found = y2[3]+sh;
                        //fk_found = fk;
                        corel_found_right = cand;
                        founds ++;
                    }
                }
                y2[3] = right_found;

                /*double corel_found_center1 = corel(spectr,pattern,
                    center1_center-center_len,center1_center+center_len,
                            fk_found,max_value);
                double center1_found = y2[1];
                for(int sh = -dlt;sh<=dlt;sh++){
                    if(sh == 0)
                        continue;
                    double[] y = {y2[0],y2[1]+sh,y2[2],y2[3]};
                    Function fk = new Function(Function.FType.Poly2, en2, x2, y);
                    double cand = corel(spectr,pattern,
                            center1_center-center_len,center1_center+center_len,
                            fk,max_value);
                    if(cand > corel_found_center1){
                        center1_found = y[1];
                        //fk_found = fk;
                        corel_found_center1 = cand;
                        founds ++;
                    }
                }
                y2[1] = center1_found;

                double corel_found_center2 = corel(spectr,pattern,
                    center2_center-center_len,center2_center+center_len,
                            fk_found,max_value);
                double center2_found = y2[2];
                for(int sh = -dlt;sh<=dlt;sh++){
                    if(sh == 0)
                        continue;
                    double[] y = {y2[0],y2[1],y2[2]+sh,y2[3]};
                    Function fk = new Function(Function.FType.Poly2, en2, x2, y);
                    double cand = corel(spectr,pattern,
                            center2_center-center_len,center2_center+center_len,
                            fk,max_value);
                    if(cand > corel_found_center2){
                        center2_found = y[2];
                        corel_found_center2 = cand;
                        founds ++;
                    }
                }
                y2[1] = center2_found;

                //*/
                //fk_found = new Function(Function.FType.Poly2,en2,x2,y2);
            }
            return fk_found;//*/
        }
        return null;
    }
    
    static Function checkForNewK(boolean en2[],double x2[],double y2[],
            float[] spectr,float[] pattern,
            int from,int to,
            Function fk_found,
            int max_value,
            int param) throws OS4AnalitException{
        int founds = 0;
        double corel_value = corel(spectr,pattern,
                    from,to,fk_found,max_value);
        for(int sh = -2;sh<=2;sh++){
            if(sh == 0)
                continue;
            double[] y = {y2[0],y2[1],y2[2],y2[3]};
            y[param] += sh;
            Function fk = new Function(Function.FType.Poly2, en2, x2, y);
            double cand_val = corel(spectr,pattern,
                    from,to,
                    fk,max_value);
            if(cand_val > corel_value){
                y2[param] = y[param];
                //fk_found = fk;
                corel_value = cand_val;
                founds ++;
            }
        }
        if(founds > 0)
            return new Function(Function.FType.Poly2, en2, x2, y2);
        return null;
    }
    
    //TODO: Coreletion:
    // Was: sum(s1*s2)
    // But need:  sum(s1*s2*d(s1)*d(s2)) 
    //            where d() - derivative by x
    //
    static double corel(float[] s1,float[] s2,
            int s1_from,int s1_to,
            Function fk,int max){
        double ret = 0;
        int l = (s1_to-s1_from);
        int n = 0;
        //ArrayList<String> debug = new ArrayList<>();
        for(int i = 0;i<l;i++){
            int s1_pixel = i+s1_from;
            double s2_pixel_d = fk.calcDirect(s1_pixel);//(int)Math.round(i*s2_k+s2_from);
            int s2_pixel_i = (int)(s2_pixel_d);
            double s2_1 = s2_pixel_d - s2_pixel_i;
            double s2_2 = 1 - s2_1;
            float val1 = s1[s1_pixel];
            float val2 = (float)(s2[s2_pixel_i]*s2_2+s2[s2_pixel_i+1]*s2_1);
            if(val1 > max || val2 > max)
                continue;
            double an_val = val1*val2;
            //debug.add((val1+";"+val2+";"+an_val+";"+s1_pixel+";"+s2_pixel_d+";"+s2_1+";"+s2_2+";\r\n").replace('.', ','));
            ret += an_val;
            n ++;
        }
        //File f = new File("tmp.csv");
        //StreamTools.writeText(f, debug);// Files.write(f.toPath(), debug.getBytes(), DefaultOp)
        return ret;
    }
}
