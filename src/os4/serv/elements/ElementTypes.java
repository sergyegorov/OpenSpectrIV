/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package os4.serv.elements;

/**
 *
 * @author Serg A. Yegorov (sergyegorov2012@gmail.com root)
 */
public enum ElementTypes {
        ShelochnieMetali(0),
        ShelochnozemelnijMetali(1),
        Lantanoidi(2),
        Aktinoidi(3),
        Perehodnie(4),
        LiogkieMetali(5),
        Polumetali(6),
        Nemetali(7),
        Galogeni(8),
        InertnieGazi(9),
        KantMolek(10);
        
        int Val;
        private ElementTypes(int val){
            this.Val = val;
        }
        
        public int getValue(){ return Val; }
}
