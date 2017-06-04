package com.nizhawan.nitin.piano.parser.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 02/06/17.
 */
public class Program {
    List<String> comments;
    List<VariableDefinition> variableDefinitions = new ArrayList<VariableDefinition>();
    public Program(){
        comments = new ArrayList<String>();
    }
    public void addComment(String comment){
        comments.add(comment);
    }
    public void addVariableDefintion(VariableDefinition variableDefinition){
        variableDefinitions.add(variableDefinition);
    }
    public List<VariableDefinition> getVariableDefinitions(){
        return this.variableDefinitions;
    }

    public String toString(){
        String str="";
        for(VariableDefinition variableDefinition:variableDefinitions){
            str+=variableDefinition + "\n";
        }
        return str;
    }
}
