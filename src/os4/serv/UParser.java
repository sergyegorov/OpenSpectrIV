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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class UParser {
    public static final double parseDouble(String text_val) throws ParseException{
        double d;
        text_val = text_val.trim();
        if(text_val.indexOf('.') > 0){
            NumberFormat nf = new DecimalFormat ("0.0");
            d = nf.parse(text_val).doubleValue();
        } else {
            NumberFormat nf = new DecimalFormat ("0,0");
            d = nf.parse(text_val).doubleValue();
        }
        return d;
    }
}