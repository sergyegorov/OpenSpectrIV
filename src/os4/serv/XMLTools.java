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

import java.util.Base64;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class XMLTools {
    public static String getHTML(String txt){
        String ret = txt.replaceAll("\r\n", "<br>");
        ret = ret.replaceAll("\n", "<br>");
        return ret;
    }
    
    public static String getXML(String txt){
        String ret = txt.replaceAll("\n", "&#xA;");
        ret = ret.replaceAll("\r", "&#xD;");
        return ret;
    }
    
    public static String getText(String xml){
        String ret = xml.replace("&#xA;", "\n");
        ret = ret.replace("&#xD;", "\r");
        return ret;
    }
    
    public static String encodeByteArray(byte[] array){
        return Base64.getEncoder().encodeToString(array);
    }
    
    public static byte[] decodeByteArray(String xml){
        return Base64.getDecoder().decode(xml);
    }
}

/*
    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
    
    Step 1: 
    import org.apache.commons.codec.binary.Base64;
String yourString = "testing";
byte[] encoded = Base64.encodeBase64(yourString.getBytes());
    
    Step 2:
    import javax.xml.bind.DatatypeConverter;
 
 //print hex string version of HELLO WORLD
 byte[] helloBytes = "HELLO WORLD".getBytes();
 String helloHex = DatatypeConverter.printHexBinary(helloBytes);
 System.out.printf("Hello hex: 0x%s\n", helloHex);
 
 //convert hex-encoded string back to original string
 byte[] decodedHex = DatatypeConverter.parseHexBinary(helloHex);
 String decodedString = new String(decodedHex, "UTF-8");
 System.out.printf("Hello decoded : %s\n", decodedString);

    And here’s the program output:

Hello hex: 0x48454C4C4F20574F524C44
Hello decoded : HELLO WORLD
    */
