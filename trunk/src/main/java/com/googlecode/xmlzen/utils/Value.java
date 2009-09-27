/*
 * Copyright 2009 Tomas Varaneckas 
 * http://www.varaneckas.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.xmlzen.utils;

/**
 * A mutable Value object.
 * 
 * <p>Used to pass any kind of object or generic into any method with a 
 * possibility to modify it and return the reference.</p>
 * 
 * <p>Example use:</p>
 * <pre>
 * Value&lt;Integer&gt; iterations = new Value();
 * //Let's say that someInput makes 3 iterations
 * //and sets that count with iterations.setValue(3);
 * Object data = doSomeCalculations(someInput, iterations);
 * System.out.println("Processing result: " + data);
 * //Should print "Iterations: 3"
 * System.out.println("Iterations: " + iterations.getValue()); 
 * </pre>
 * 
 * @author Tomas Varaneckas &lt;tomas.varaneckas@gmail.com&gt;
 * @version $Id$
 * @param Type Type of the value
 */
public class Value<Type> {

    /**
     * Value of this object
     */
    private Type value;
    
    /**
     * Default constructor
     */
    public Value() {
        //nothing to do
    }
    
    /**
     * Constructor with initial value
     * 
     * @param initial Initial value
     */
    public Value(final Type initial) {
        this.value = initial;
    }
    
    /**
     * Setter for the value
     * 
     * @param value New value
     */
    public void setValue(final Type value) {
        this.value = value;
    }
    
    /**
     * Value getter
     * 
     * @return Object's value
     */
    public Type getValue() {
        return value;
    }
    
    @Override
    /**
     * Returns String.valueOf object's internal value
     */
    public String toString() {
        return String.valueOf(value);
    }
    
}
