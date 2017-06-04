package com.nizhawan.nitin.piano.parser.ast;

/**
 * Created by nitin on 04/06/17.
 */
public class NoteFragment {
    BlockLiteral bl;

    public BlockLiteral getBlockLiteral() {
        return bl;
    }

    public SequenceLiteral getSequenceLiteral() {
        return sl;
    }

    public VarRef getVarRef() {
        return varRef;
    }

    public NoteLiteral getNoteLiteral() {
        return nl;
    }

    SequenceLiteral sl;
    VarRef varRef;
    NoteLiteral nl;
    public NoteFragment(BlockLiteral bl){
        this.bl = bl;

    }

    public NoteFragment(SequenceLiteral sl){
        this.sl = sl;
    }
    public NoteFragment(VarRef varRef){
        this.varRef = varRef;
    }
    public NoteFragment(NoteLiteral nl){
        this.nl = nl;
    }

    public String toString(){
        if(sl != null){
            return sl+"";
        }
        if(varRef != null){
            return varRef+"";
        }
        if(bl != null){
            return bl+"";
        }
        if(nl != null){
            return nl+"";
        }
        return "";
    }
}
