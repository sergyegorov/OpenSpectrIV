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
package os4.dev.method;

import os4.Mls;

/**
 *
 * @author root
 */
public interface IMethodDescription extends IMethodAspect{
    public enum DescriptionTypes{
        Description("Description",Mls.get("Общее описание")),
        Warrning("Warrning",Mls.get("Предупреждение перед измерением")),
        Elements("Elements",Mls.get("Измеряемые элементы")),
        Standarts("Standarts",Mls.get("Используемые стандарты")),
        Full("Full",Mls.get("Полный отчет"));
        
        String sName,sHeader;
        private DescriptionTypes(String name,String header) {
            sName = name;
            sHeader = header;
        }
        
        public String getHeader(){
            return sHeader;
        }
        
        @Override
        public String toString(){
            return sName;
        }
    }
    String getDescription(DescriptionTypes type);
    void setDescription(DescriptionTypes type,String value) throws Exception;
}
