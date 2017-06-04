package com.nizhawan.nitin.piano.parser;

import com.nizhawan.nitin.PianoLexer;
import com.nizhawan.nitin.PianoParser;
import com.nizhawan.nitin.piano.SampleGenerator;
import com.nizhawan.nitin.piano.SamplePlayer;
import com.nizhawan.nitin.piano.SimpleSineGenerator;
import com.nizhawan.nitin.piano.parser.ast.AntlrPianoListener;
import com.nizhawan.nitin.piano.parser.ast.Program;
import com.nizhawan.nitin.piano.wav.generator.KarplusStrongGenerator;
import com.nizhawan.nitin.piano.wav.generator.WavGenerator;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class PianoMainTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PianoMainTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PianoMainTest.class );
    }

    public void testASTGenerator() throws  Exception {
       // String programStr = " #comment abc \n abcd+=[A#45 { B45} ]main+=[A34 abcd ]";
       InputStream is = this.getClass().getResourceAsStream("/furelise.txt");
       String  programStr = IOUtils.toString(is, "UTF-8");
        ASTGenerator astGenerator = new ASTGenerator(programStr);
        Program program = astGenerator.getProgram();
        ASTProcessor processor = new ASTProcessor();
        int [] samples = processor.programToSamples(program);
         //processor.compile(programStr,new File("tmp.samples"));
        SamplePlayer.play(samples);
        //System.out.println(program+"");
    }
    /**
     * Rigourous Test :-)
     */
   public void testComptine() throws Exception {
       /*InputStream is = this.getClass().getResourceAsStream("/comptine.txt");
       String noteSentence  = IOUtils.toString(is, "UTF-8");
       Parser.compile(noteSentence,new File("tmp.samples"));*/
   }
}
