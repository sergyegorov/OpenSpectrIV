/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package os4.serv.elements;

import java.awt.Color;
import os4.Mls;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public class ChElement {
    public String Name;
    public String FullName;
    public int AtomNum;
    public ElementTypes Type;
    public int Col, Row;
    public boolean RadioActive;
    public int UsedFrequancy;

    /**
     * Creates record about chemical element
     * @param atom_num - atom number
     * @param name - short name of element
     * @param name_full - full name of element
     * @param type - ElementTypes
     * @param col - column into Mendeleev table
     * @param row - row into Mendeleev table
     * @param using
     */
    public ChElement(int atom_num, String name,String name_full,
        ElementTypes type,
        int col,int row,int using)
    {
        AtomNum = atom_num;
        Name = name;
        FullName = name_full;
        Type = type;
        Col = col;
        Row = row;
        UsedFrequancy = using;
    }

    /**
     * Creates record about chemical element
     * @param atom_num - atom number
     * @param name - short name of element
     * @param name_full - full name of element
     * @param type - ElementTypes
     * @param col - column into Mendeleev table
     * @param row - row into Mendeleev table
     * @param radio_active - radio active
     */
    public ChElement(int atom_num, String name,String name_full,
        ElementTypes type,
        int col,int row,boolean radio_active,int using_level)
    {
        AtomNum = atom_num;
        Name = name;
        FullName = name_full;
        Type = type;
        Col = col;
        Row = row;
        RadioActive = radio_active;
        UsedFrequancy = using_level;
    }
    
    public Color getColorForType()
    {
        switch (Type)
        {
            case Galogeni: return new Color(255, 251, 160);
            case InertnieGazi: return new Color(198, 252, 252);
            case KantMolek: return Color.yellow;
            case LiogkieMetali: return new Color(207, 203, 205);
            case Nemetali: return new Color(152, 255, 172);
            case Perehodnie: return new Color(255, 189, 186);
            case Polumetali: return new Color(206, 203, 150);
            case ShelochnieMetali: return new Color(255, 90, 87);
            case ShelochnozemelnijMetali: return new Color(255, 220, 162);
            case Aktinoidi: return new Color(237, 154, 208);
            case Lantanoidi: return new Color(255, 193, 255);
            default: return Color.white;
        }
    }

    public String getNameOfType()
    {
        switch (Type)
        {
            case Galogeni: return Mls.get("Галогены");
            case InertnieGazi: return Mls.get("Инертные газы");
            case KantMolek: return Mls.get("Кант молекуларной полосы");
            case LiogkieMetali: return Mls.get("Лёгкие металлы");
            case Nemetali: return Mls.get("Неметаллы");
            case Perehodnie: return Mls.get("Переходные элементы");
            case Polumetali: return Mls.get("Полуметаллы");
            case ShelochnieMetali: return Mls.get("Щелочные металлы");
            case ShelochnozemelnijMetali: return Mls.get("Щелочноземельные металлы");
            case Aktinoidi: return Mls.get("Актиноиды");
            case Lantanoidi: return Mls.get("Лантаноиды");
            default: return Mls.get("Неизвестные");
        }
    }
}   
