package com.nizhawan.nitin.piano.parser.ast;

/**
 * Created by nitin on 04/06/17.
 */
public class VarRef {
    public String getVarName() {
        return varName;
    }

    String varName;
    public VarRef(String varName){
        this.varName = varName;
    }
    public String toString(){
        return varName;
    }
}
