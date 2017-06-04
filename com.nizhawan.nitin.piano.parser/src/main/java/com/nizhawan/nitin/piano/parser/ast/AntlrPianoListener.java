package com.nizhawan.nitin.piano.parser.ast;

import com.nizhawan.nitin.PianoListener;
import com.nizhawan.nitin.PianoParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Created by nitin on 02/06/17.
 */
public class AntlrPianoListener implements PianoListener {
    Program program;
    public void enterMusicalNote(@NotNull PianoParser.MusicalNoteContext ctx) {

    }

    public void exitMusicalNote(@NotNull PianoParser.MusicalNoteContext ctx) {

    }

    public void enterOctave(PianoParser.OctaveContext ctx) {

    }

    public void exitOctave(PianoParser.OctaveContext ctx) {

    }

    public void enterNoteLength(PianoParser.NoteLengthContext ctx) {

    }

    public void exitNoteLength(PianoParser.NoteLengthContext ctx) {

    }

    public void enterNoteLiteral(@NotNull PianoParser.NoteLiteralContext ctx) {
         // System.out.println(ctx.)
    }

    public void exitNoteLiteral(@NotNull PianoParser.NoteLiteralContext ctx) {

    }

    public void enterBlockLiteral(PianoParser.BlockLiteralContext ctx) {

    }

    public void exitBlockLiteral(PianoParser.BlockLiteralContext ctx) {

    }

    public void enterSequenceLiteral(PianoParser.SequenceLiteralContext ctx) {

    }

    public void exitSequenceLiteral(PianoParser.SequenceLiteralContext ctx) {

    }

    public void enterCompileUnit(@NotNull PianoParser.CompileUnitContext ctx) {
           program = new Program();
    }

    public void exitCompileUnit(@NotNull PianoParser.CompileUnitContext ctx) {

    }

    public void visitTerminal(TerminalNode terminalNode) {

    }

    public void visitErrorNode(ErrorNode errorNode) {

    }

    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }

    public Program getProgram(){
        return program;
    }
}
