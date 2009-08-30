package com.googlecode.xmlzen.utils;

public class Value<Type> {

    private Type value;
    
    public Value() {
    }
    
    public Value(Type initial) {
        this.value = initial;
    }
    
    public void setValue(Type value) {
        this.value = value;
    }
    
    public Type getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
}
