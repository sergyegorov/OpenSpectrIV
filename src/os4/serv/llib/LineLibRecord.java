/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package os4.serv.llib;

import java.io.IOException;
import java.io.InputStream;
import os4.serv.elements.ElementTable;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class LineLibRecord {
    public short Element = 255;
    public String getElementName() {
        return ElementTable.Elements[Element].FullName;
    }
    public short IonLevel = 255;
    public float Ly;
    public short NistIntens = -1;
    public String NistIntensRem = "";
    public short ZIntensDuga = -1, ZIntensIskra = -1;
    public boolean ZDugaR, ZIskraR;
    public float ZElemInt = -1;
    public String ZElemIntSrc = "";
    public short PDugaIntens = -1;
    
    public void load(InputStream is) throws IOException{
        if(is.read() != 1) //if (br.ReadByte() != 1)
            throw new RuntimeException("Wrong record version.");
        Element = (short)(0xFF & is.read()); //Element = br.ReadByte();
        IonLevel = (short)(0xFF & is.read()); //IonLevel = br.ReadByte();
        if(IonLevel > 30)
            throw new RuntimeException("Wrong IonLevel: "+IonLevel);
        Ly = LineLib.readFloat(is); //Ly = br.ReadSingle();
        if(Ly < 1600 || Ly > 10000)
            throw new RuntimeException("Ly is out of range: "+Ly);
        NistIntens = LineLib.readShort(is);    //NistIntens = br.ReadInt16();
        NistIntensRem = LineLib.readString(is);    //NistIntensRem = br.ReadString();
        ZIntensDuga = LineLib.readShort(is);    //ZIntensDuga = br.ReadInt16();
        ZIntensIskra = LineLib.readShort(is);    //ZIntensIskra = br.ReadInt16();
        ZDugaR = LineLib.readBoolean(is);    //ZDugaR = br.ReadBoolean();
        ZIskraR = LineLib.readBoolean(is);    //ZIskraR = br.ReadBoolean();
        ZElemInt = LineLib.readFloat(is);    //ZElemInt = br.ReadSingle();
        ZElemIntSrc = LineLib.readString(is);    //ZElemIntSrc = br.ReadString();
        PDugaIntens = LineLib.readShort(is);    //PDugaIntens = br.ReadInt16();
        if(is.read() != 38)    //if (br.ReadByte() != 38)
            throw new RuntimeException("Wrong end of record.");//   throw new Exception("Неправильное окончание LineDbRecord.");
    }
}
