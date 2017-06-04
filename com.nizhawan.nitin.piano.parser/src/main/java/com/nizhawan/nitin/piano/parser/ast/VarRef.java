package com.nizhawan.nitin.piano.parser.ast;

/**
 * Created by nitin on 04/06/17.
 */
public class VarRef {
    public String getVarName() {
        return varName;
    }

    String varName;
    int octaveOffset = 0;

    public int getNoteLengthMultiplier() {
        return noteLengthMultiplier;
    }

    public int getOctaveOffset() {
        return octaveOffset;
    }

    int noteLengthMultiplier = 1;
    public VarRef(String varName){
        this.varName = varName;
    }
    public VarRef(String varName,int octaveOffset,int noteLengthMultiplier){
        this(varName);
        this.octaveOffset = octaveOffset;
        this.noteLengthMultiplier = noteLengthMultiplier;
    }
    public String toString(){
        return varName;
    }
}
