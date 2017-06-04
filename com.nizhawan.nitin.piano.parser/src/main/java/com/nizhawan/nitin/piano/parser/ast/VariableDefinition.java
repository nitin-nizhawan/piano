package com.nizhawan.nitin.piano.parser.ast;



/**
 * Created by nitin on 04/06/17.
 */
public class VariableDefinition {
    String varName;
    NoteFragment noteFragment;
    public VariableDefinition(String variableName, NoteFragment nf){
          this.varName = variableName;
          this.noteFragment = nf;
    }
    public String getVarName(){
        return varName;
    }
    public NoteFragment getNoteFragment(){
        return this.noteFragment;
    }
    public String toString(){
        return varName + "+=" + noteFragment;
    }
}
