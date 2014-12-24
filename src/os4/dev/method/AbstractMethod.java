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

import java.io.File;
import java.sql.Connection;
import os4.dev.SpectrData;
import os4.task.AbstractTask;

/**
 *
 * @author root
 */
public interface AbstractMethod {
    AbstractTask getGUIEditor();
        
    static final String ASPECT_DESCRIPTION = "desc";
    static final String ASPECT_CHANEL = "chanel";
    static final String ASPECT_CALIBR = "calibr";
    
    IMethodAspect[] getAspectSet();
    
    default IMethodAspect getAspect(String id_string){
        IMethodAspect[] aspects = getAspectSet();
        if(aspects == null)
            return null;
        for(IMethodAspect aspect : aspects)
            if(aspect.getId().equals(id_string))
                return aspect;
        return null;
    }

    default String getDescription(){
        IMethodDescription description = (IMethodDescription)getAspect(ASPECT_DESCRIPTION);
        return description.getDescription(IMethodDescription.DescriptionTypes.Full);
    }
    
    AbstractMethodResult[] calculate(SpectrData data);
    AbstractChanel createNewChanel(String name) throws Exception;
    AbstractFormula createNewFormula(String element,String formula);
    
    void commit() throws Exception;
    
    Connection getDBConnection() throws Exception;
    File getFile(String name);

    AbstractMethod cloneMethod();
}
