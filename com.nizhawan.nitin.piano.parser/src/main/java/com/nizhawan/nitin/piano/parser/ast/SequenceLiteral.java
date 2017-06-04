package com.nizhawan.nitin.piano.parser.ast;

import java.util.List;

/**
 * Created by nitin on 04/06/17.
 */
public class SequenceLiteral {
    public List<NoteFragment> getNoteFragments() {
        return noteFragments;
    }

    List<NoteFragment> noteFragments;
    public SequenceLiteral(List<NoteFragment> noteFragmentList){
        this.noteFragments = noteFragmentList;
    }
    public String toString(){
        String str="[";
        for(NoteFragment noteFragment:noteFragments){
            str+=noteFragment + " ";
        }
        return str+"]";
    }
}
