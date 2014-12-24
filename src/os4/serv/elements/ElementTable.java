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
public class ElementTable {
        public static int FindIndex(String name)
        {
            for (int i = 0; i < Elements.length; i++)
                if (Elements[i].Name.toLowerCase().trim().equals(name.toLowerCase().trim()))
                    return i;
            return -1;
        }

        public static final int UsingNone = 0;
        public static final int UsingExotic = 1;
        public static final int UsingRear = 2;
        public static final int Using = 3;
        public static final int UsingOfften = 4;
        
        static public ChElement[] Elements = 
        {
            new ChElement(1,"H","Водород",ElementTypes.Nemetali,1,1,UsingExotic),
            new ChElement(2,"He","Гелий",ElementTypes.InertnieGazi,18,1,UsingExotic),

            new ChElement(3,"Li","Литий",ElementTypes.ShelochnieMetali,1,2,UsingRear),
            new ChElement(4,"Be","Берилий",ElementTypes.ShelochnozemelnijMetali,2,2,Using),
            new ChElement(5,"B","Бор",ElementTypes.Polumetali,13,2,Using),
            new ChElement(6,"C","Углерод",ElementTypes.Nemetali,14,2,Using),
            new ChElement(7,"N","Азот",ElementTypes.Nemetali,15,2,Using),
            new ChElement(8,"O","Кислород",ElementTypes.Nemetali,16,2,Using),
            new ChElement(9,"F","Фтор",ElementTypes.Galogeni,17,2,UsingExotic),
            new ChElement(10,"Ne","Неон",ElementTypes.InertnieGazi,18,2,UsingExotic),

            new ChElement(11,"Na","Натрий",ElementTypes.ShelochnieMetali,1,3,UsingRear),
            new ChElement(12,"Mg","Магний",ElementTypes.ShelochnozemelnijMetali,2,3,UsingOfften),
            new ChElement(13,"Al","Алюминий",ElementTypes.LiogkieMetali,13,3,UsingOfften),
            new ChElement(14,"Si","Кремний",ElementTypes.Polumetali,14,3,UsingOfften),
            new ChElement(15,"P","Фосфор",ElementTypes.Nemetali,15,3,UsingOfften),
            new ChElement(16,"S","Сера",ElementTypes.Nemetali,16,3,UsingOfften),
            new ChElement(17,"Cl","Хлор",ElementTypes.Galogeni,17,3,UsingNone),
            new ChElement(18,"Ar","Аргон",ElementTypes.InertnieGazi,18,3,Using),

            new ChElement(19,"K","Калий",ElementTypes.ShelochnieMetali,1,4,Using),
            new ChElement(20,"Ca","Кальций",ElementTypes.ShelochnozemelnijMetali,2,4,UsingOfften),
            new ChElement(21,"Sc","Скандий",ElementTypes.Perehodnie,3,4,Using),
            new ChElement(22,"Ti","Титан",ElementTypes.Perehodnie,4,4,UsingOfften),
            new ChElement(23,"V","Ванадий",ElementTypes.Perehodnie,5,4,UsingOfften),
            new ChElement(24,"Cr","Хром",ElementTypes.Perehodnie,6,4,UsingOfften),
            new ChElement(25,"Mn","Марганец",ElementTypes.Perehodnie,7,4,UsingOfften),
            new ChElement(26,"Fe","Железо",ElementTypes.Perehodnie,8,4,UsingOfften),
            new ChElement(27,"Co","Кобальт",ElementTypes.Perehodnie,9,4,UsingOfften),
            new ChElement(28,"Ni","Никель",ElementTypes.Perehodnie,10,4,UsingOfften),
            new ChElement(29,"Cu","Медь",ElementTypes.Perehodnie,11,4,UsingOfften),
            new ChElement(30,"Zn","Цинк",ElementTypes.Perehodnie,12,4,UsingOfften),
            new ChElement(31,"Ga","Галий",ElementTypes.LiogkieMetali,13,4,UsingOfften),
            new ChElement(32,"Ge","Германий",ElementTypes.Polumetali,14,4,UsingOfften),
            new ChElement(33,"As","Мышьяк",ElementTypes.Polumetali,15,4,UsingOfften),
            new ChElement(34,"Se","Селен",ElementTypes.Nemetali,16,4,UsingOfften),
            new ChElement(35,"Br","Бром",ElementTypes.Galogeni,17,4,UsingNone),
            new ChElement(36,"Kr","Криптон",ElementTypes.InertnieGazi,18,4,UsingNone),

            new ChElement(37,"Rb","Рубидий",ElementTypes.ShelochnieMetali,1,5,UsingNone),
            new ChElement(38,"Sr","Стронций",ElementTypes.ShelochnozemelnijMetali,2,5,Using),
            new ChElement(39,"Y","Иттрий",ElementTypes.Perehodnie,3,5,UsingNone),
            new ChElement(40,"Zr","Цирконий",ElementTypes.Perehodnie,4,5,Using),
            new ChElement(41,"Nb","Ниобий",ElementTypes.Perehodnie,5,5,UsingRear),
            new ChElement(42,"Mo","Молибден",ElementTypes.Perehodnie,6,5,UsingOfften),
            new ChElement(43,"Tc","Технеций",ElementTypes.Perehodnie,7,5,true,UsingNone),
            new ChElement(44,"Ru","Рутений",ElementTypes.Perehodnie,8,5,UsingNone),
            new ChElement(45,"Rh","Родий",ElementTypes.Perehodnie,9,5,UsingNone),
            new ChElement(46,"Pd","Палладий",ElementTypes.Perehodnie,10,5,Using),
            new ChElement(47,"Ag","Серебро",ElementTypes.Perehodnie,11,5,UsingRear),
            new ChElement(48,"Cd","Кадмий",ElementTypes.Perehodnie,12,5,UsingOfften),
            new ChElement(49,"In","Индий",ElementTypes.LiogkieMetali,13,5,UsingOfften),
            new ChElement(50,"Sn","Олово",ElementTypes.LiogkieMetali,14,5,UsingOfften),
            new ChElement(51,"Sb","Сурьма",ElementTypes.Polumetali,15,5,UsingOfften),
            new ChElement(52,"Te","Теллур",ElementTypes.Polumetali,16,5,UsingExotic),
            new ChElement(53,"I","Иод",ElementTypes.Galogeni,17,5,UsingExotic),
            new ChElement(54,"Xe","Ксенон",ElementTypes.InertnieGazi,18,5,UsingNone),

            new ChElement(55,"Cs","Цезий",ElementTypes.ShelochnieMetali,1,6,Using),
            new ChElement(56,"Ba","Барий",ElementTypes.ShelochnozemelnijMetali,2,6,Using),
            // lantonoids
            new ChElement(252,"*","Лантаноиды",ElementTypes.Lantanoidi,3,6,UsingNone),

            new ChElement(57,"La","Лантан",ElementTypes.Lantanoidi,1,9,UsingNone),
            new ChElement(58,"Ce","Церий",ElementTypes.Lantanoidi,2,9,UsingNone),
            new ChElement(59,"Pr","празеодим",ElementTypes.Lantanoidi,3,9,UsingNone),
            new ChElement(60,"Nd","неодим",ElementTypes.Lantanoidi,4,9,Using),
            new ChElement(61,"Pm","прометий",ElementTypes.Lantanoidi,5,9,UsingNone),
            new ChElement(62,"Sm","самарий",ElementTypes.Lantanoidi,6,9,Using),
            new ChElement(63,"Eu","европий",ElementTypes.Lantanoidi,7,9,UsingNone),
            new ChElement(64,"Gd","европий",ElementTypes.Lantanoidi,8,9,UsingNone),
            new ChElement(65,"Tb","тербий",ElementTypes.Lantanoidi,9,9,UsingNone),
            new ChElement(66,"Dy","диспрозий",ElementTypes.Lantanoidi,10,9,UsingNone),
            new ChElement(67,"Ho","гольмий",ElementTypes.Lantanoidi,11,9,UsingNone),
            new ChElement(68,"Er","эрбий",ElementTypes.Lantanoidi,12,9,UsingNone),
            new ChElement(69,"Tm","тулий",ElementTypes.Lantanoidi,13,9,UsingNone),
            new ChElement(70,"Yb","тулий",ElementTypes.Lantanoidi,14,9,UsingNone),
            new ChElement(71,"Lu","лютеций",ElementTypes.Lantanoidi,15,9,UsingNone),
            new ChElement(72,"Hf","гафний",ElementTypes.Perehodnie,4,6,Using),
            new ChElement(73,"Ta","гафний",ElementTypes.Perehodnie,5,6,Using),
            new ChElement(74,"W","вольфрам",ElementTypes.Perehodnie,6,6,UsingOfften),
            new ChElement(75,"Re","рений",ElementTypes.Perehodnie,7,6,UsingExotic),
            new ChElement(76,"Os","осмий",ElementTypes.Perehodnie,8,6,UsingExotic),
            new ChElement(77,"Ir","иридий",ElementTypes.Perehodnie,9,6,UsingExotic),
            new ChElement(78,"Pt","платина",ElementTypes.Perehodnie,10,6,UsingExotic),
            new ChElement(79,"Au","платина",ElementTypes.Perehodnie,11,6,Using),
            new ChElement(80,"Hg","платина",ElementTypes.Perehodnie,12,6,Using),
            new ChElement(81,"Tl","таллий",ElementTypes.LiogkieMetali,13,6,UsingExotic),
            new ChElement(82,"Pb","свинец",ElementTypes.LiogkieMetali,14,6,UsingOfften),
            new ChElement(83,"Bi","висмут",ElementTypes.LiogkieMetali,15,6,Using),
            new ChElement(84,"Po","полоний",ElementTypes.Polumetali,16,6,UsingNone),
            new ChElement(85,"At","полоний",ElementTypes.Galogeni,17,6,true,UsingExotic),
            new ChElement(86,"Rn","радон",ElementTypes.InertnieGazi,18,6,UsingNone),

            new ChElement(87,"Fr","франций",ElementTypes.ShelochnieMetali,1,7,UsingNone),
            new ChElement(88,"Ra","Радий",ElementTypes.ShelochnozemelnijMetali,2,7,UsingNone),
            // actinoids
            new ChElement(253,"**","актиний",ElementTypes.Aktinoidi,3,7,UsingNone),

            new ChElement(89,"Ac","актиний",ElementTypes.Aktinoidi,1,10,UsingNone),
            new ChElement(90,"Th","Торий",ElementTypes.Aktinoidi,2,10,UsingNone),
            new ChElement(91,"Pa","протактиний",ElementTypes.Aktinoidi,3,10,UsingNone),
            new ChElement(92,"U","Уран",ElementTypes.Aktinoidi,4,10,true,UsingExotic),
            new ChElement(93,"Np","Нептуний",ElementTypes.Aktinoidi,5,10,true,UsingExotic),
            new ChElement(94,"Pu","Плутоний",ElementTypes.Aktinoidi,6,10,true,UsingExotic),
            new ChElement(95,"Am","Америций",ElementTypes.Aktinoidi,7,10,true,UsingNone),
            new ChElement(96,"Cm","кюрий",ElementTypes.Aktinoidi,8,10,true,UsingNone),
            new ChElement(97,"Bk","берклий",ElementTypes.Aktinoidi,9,10,true,UsingNone),
            new ChElement(98,"Cf","калифорний",ElementTypes.Aktinoidi,10,10,true,UsingNone),
            new ChElement(99,"Es","эйнштейний",ElementTypes.Aktinoidi,11,10,true,UsingNone),
            new ChElement(100,"Fm","фермий",ElementTypes.Aktinoidi,12,10,true,UsingNone),
            new ChElement(101,"Md","менделевий",ElementTypes.Aktinoidi,13,10,true,UsingNone),
            new ChElement(102,"No","нобелий",ElementTypes.Aktinoidi,14,10,true,UsingNone),
            new ChElement(103,"Lr","Лоуренсий",ElementTypes.Aktinoidi,15,10,true,UsingNone),
            new ChElement(104,"Rf","Резерфордий",ElementTypes.Perehodnie,4,7,true,UsingNone),
            new ChElement(105,"Db","дубний",ElementTypes.Perehodnie,5,7,true,UsingNone),
            new ChElement(106,"Sg","сиборгий",ElementTypes.Perehodnie,6,7,true,UsingNone),
            new ChElement(107,"Bh","борий",ElementTypes.Perehodnie,7,7,true,UsingNone),
            new ChElement(108,"Hs","хассий",ElementTypes.Perehodnie,8,7,true,UsingNone),
            new ChElement(109,"Mt","мейтнерий",ElementTypes.Perehodnie,9,7,true,UsingNone),
            new ChElement(110,"Ds","дармштадтий",ElementTypes.Perehodnie,10,7,true,UsingNone),
            new ChElement(111,"Rg","рентгений",ElementTypes.Perehodnie,11,7,true,UsingNone),
            new ChElement(112,"Cp","коперниций",ElementTypes.Perehodnie,12,7,true,UsingNone),
            //new ChElement(113,"Uut","",ElementTypes.LiogkieMetali,13,7,true),
            new ChElement(254,"k","Кант молекуларной полосы",ElementTypes.KantMolek,18,7,false,UsingOfften)
        };

        public ChElement get(int index)
        {
            return Elements[index];
        }
}
