package com.nizhawan.nitin.piano.parser;

import com.nizhawan.nitin.piano.parser.ast.BlockLiteral;
import com.nizhawan.nitin.piano.parser.ast.NoteLiteral;
import com.nizhawan.nitin.piano.parser.ast.Program;
import com.nizhawan.nitin.piano.parser.ast.NoteFragment;
import com.nizhawan.nitin.piano.parser.ast.SequenceLiteral;
import com.nizhawan.nitin.piano.parser.ast.VarRef;
import com.nizhawan.nitin.piano.parser.ast.VariableDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitin on 04/06/17.
 */
public class ASTGenerator {
    private Program program = new Program();
    final String programStr;
    int pos = -1;
    int charPos = 0;
    int lineNo = 0;
    char currentChar;
    private boolean isEOF(){
        if(pos >= (programStr.length() -1) ){
            return true;
        }
        return false;
    }

    private void readChar(){
        if(isEOF()){
            error("Unexpected end of file (EOF) ");
        }
        currentChar = programStr.charAt(++pos);
        if(currentChar == '\n'){
            lineNo++;
            charPos=0;
        } else {
            charPos++;
        }

    }

    private char currentChar(){
        return currentChar;
    }

    private boolean isWhiteSpace(){
        return currentChar == ' ' || currentChar == '\n' || currentChar == '\r' || currentChar == '\t';
    }
    private void skipWhiteSpace(){
        while((!isEOF()) && isWhiteSpace()){
            readChar();
        }
    }
    private void init(){
        if(isEOF()){ throw new IllegalStateException("Unexected EOF at the beggning of program"); }
        readChar();
    }
    private boolean isNewLine(){
        return currentChar() == '\n';
    }
    private void readComment(){
        int startPos = pos;
        while(!isEOF() || !isNewLine()) {
            readChar();
        }
        String comment = programStr.substring(startPos,pos);
        program.addComment(comment);
    }
    private void error(String string){
        throw new IllegalStateException(string+"at "+charPos+","+lineNo);
    }
    private String parseVarName(){
        // var name cannot begin with capital alphabet or number
        if(currentChar() >= 'A' && currentChar() <= 'Z'){
            error("Unexpected variable name starting with capital letter");
        }
        int startPos = pos;
        while(currentChar() != '+' && !isWhiteSpace()){
            readChar();
        }
        String varName = programStr.substring(startPos,pos);
        return varName;
    }
    public void match(char chr){
        if(currentChar == chr){
           if(!isEOF()) readChar();
        } else {
            error("Unexpected char "+currentChar+" , expected "+chr);
        }
    }

    private boolean isCapital(){
        return currentChar >= 'A' && currentChar <= 'Z';
    }
    private boolean isSmall(){
        return currentChar >= 'a' && currentChar <='z';
    }
    /**
     * NoteFragment can be a
     * NoteLiteral | SequenceLiteral | BlockLiteral | variable reference
     */
    public NoteFragment parseNoteFragment(){
        if(currentChar == '['){
            SequenceLiteral sl = parseSequenceLiteral();
            return new NoteFragment(sl);
        } else if(currentChar == '{'){
            BlockLiteral blockLiteral = parseBlockLiteral();
            return new NoteFragment(blockLiteral);
        } else if(isCapital()){
            NoteLiteral nl = parseNoteLiteral();
            return new NoteFragment(nl);
        } else if(isSmall()){
            VarRef ref = parseVariableReference();
            return new NoteFragment(ref);
        } else {
            error("Unexpected char "+currentChar);
        }
        return null;
    }

    private boolean isNumber(){
        return currentChar >= '0' && currentChar <='9';
    }
    private VarRef parseVariableReference() {
         String varName = parseVarName();
        return new VarRef(varName);
    }

    private NoteLiteral parseNoteLiteral() {
        parseComments();
        if((currentChar >='A' && currentChar <='G') || currentChar == 'R'){
            String note = ""+currentChar;
            int octave = 0;
            int duration = 0;
            readChar();
            if(currentChar == '#' || currentChar == 'b'){
                note+=currentChar;
                readChar();
            }
            if(isNumber()){
                octave = currentChar - '0';
                readChar();
            } else {
                error("Expected number octave ");
            }
            if(isNumber()){
                do {
                    duration = duration*10 + (currentChar - '0');
                    if(!isEOF()) readChar();
                } while(isNumber() && !isEOF());
            } else {
                error("Expected number for duration");
            }
            return new NoteLiteral(note,octave,duration);
        }
        return null;
    }

    private BlockLiteral parseBlockLiteral() {
        match('{');
        List<NoteFragment> noteFragments = new ArrayList<NoteFragment>();
        while(currentChar != '}'){
            skipWhiteSpace();
            parseComments();
            skipWhiteSpace();
            NoteFragment nf = parseNoteFragment();
            skipWhiteSpace();
            parseComments();
            skipWhiteSpace();
            noteFragments.add(nf);
        }
        match('}');
        return new BlockLiteral(noteFragments);
    }

    private SequenceLiteral parseSequenceLiteral() {
        match('[');
        List<NoteFragment> noteFragments = new ArrayList<NoteFragment>();
        while(currentChar != ']'){
            skipWhiteSpace();
            parseComments();
            skipWhiteSpace();
            NoteFragment nf = parseNoteFragment();
            skipWhiteSpace();
            parseComments();
            skipWhiteSpace();
            noteFragments.add(nf);
        }
        match(']');
        return new SequenceLiteral(noteFragments);
    }

    private void match(String str){
        for(int i=0;i<str.length();i++){
            match(str.charAt(i));
        }
    }

    private void parseOptionalComment(){
        skipWhiteSpace();
        match('#');
        int startPos = pos;
        while(!isEOF() && !isNewLine()){
            readChar();
        }
        String comment = programStr.substring(startPos,pos);
    }
    private void parseComments(){
        skipWhiteSpace();
        while(currentChar == '#'){
            parseOptionalComment();
            skipWhiteSpace();
        }
    }

    /**
     * grammar rule
     * variableDefinition -> variableName += noteFragment
     * @return
     */
    private void parseVarDef(){
        String varName = parseVarName();
        skipWhiteSpace();
        match("+=");
        skipWhiteSpace();
        NoteFragment noteFragment = parseNoteFragment();
        program.addVariableDefintion(new VariableDefinition(varName, noteFragment));

    }
    public ASTGenerator(String programStr){
        this.programStr = programStr;

        if(isEOF()){ // empty program
            return;
        } else {
            init();
        }
        skipWhiteSpace();
        while(!isEOF()){
            parseComments();
            skipWhiteSpace();
            if(!isEOF()) {
                parseVarDef();
            }
        }

    }
    Program getProgram(){
        return program;
    }
}
