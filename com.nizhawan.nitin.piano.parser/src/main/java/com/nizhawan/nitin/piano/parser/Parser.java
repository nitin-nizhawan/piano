package com.nizhawan.nitin.piano.parser;

import com.nizhawan.nitin.PianoLexer;
import com.nizhawan.nitin.PianoParser;
import com.nizhawan.nitin.piano.CompositeSample;
import com.nizhawan.nitin.piano.Sample;
import com.nizhawan.nitin.piano.SampleGenerator;
import com.nizhawan.nitin.piano.SingularSample;
import com.nizhawan.nitin.piano.parser.ast.AntlrPianoListener;
import com.nizhawan.nitin.piano.parser.ast.Program;
import com.nizhawan.nitin.piano.wav.generator.KarplusStrongGenerator;
import com.nizhawan.nitin.piano.wav.generator.WavGenerator;
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
import java.util.Stack;

/**
 * Created by nitin on 02/06/17.
 */

public class Parser {

    public static String[] getLines(String input){
        return input.split("\\n");
    }

    public static Program parse(String input){
        for(int i=0;i<input.length();i++){
            char currentChar = input.charAt(i);

        }
        return null;
    }


    public static void compile(String program,File outFile) throws Exception
    {
        int SAMPLES_PER_SECOND = 44100;
        int volume = 32000;

        SampleGenerator sg = new KarplusStrongGenerator(volume,SAMPLES_PER_SECOND);
        //int samples [] = sg.generate(440.0,SAMPLES_PER_SECOND);

        //WavGenerator.generateFile(samples,new File("test.wav"));

        //String noteSentence = "[ {C416 E416 G416}  { C416 E416 G416} { C416 E416 G416} { C416 E416 G416} { C416 E416 G416}  ]";


        PianoLexer lexer = new PianoLexer(new ANTLRInputStream(program));

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        PianoParser parser = new PianoParser(tokens);

        // Specify our entry point
        PianoParser.CompileUnitContext pianoContext = parser.compileUnit();

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        AntlrPianoListener listener = new AntlrPianoListener();
        walker.walk(listener, pianoContext);

        //Program program = listener.getProgram();
        PianoParser.NoteLiteralContext note = pianoContext.noteLiteral();
        PianoParser.BlockLiteralContext block = pianoContext.blockLiteral();
        PianoParser.SequenceLiteralContext sequence = pianoContext.sequenceLiteral();
        List<Sample> samples1 =  new ArrayList<Sample>() ;
        if(note != null){
            samples1 = noteLiteralToSamples(note,sg);
        } else if(block != null){
            samples1 = blockLiteralToSamples(block,sg);
        } else if(sequence != null){
            samples1 = sequenceLiteralToSamples(sequence,sg);
        }
        System.out.println("Starting flatten Samples");
        int smpls[] = flattenSamples(samples1);
        WavGenerator.generateFile(smpls, outFile);
    }
    public static int [] flattenSamples(List<Sample> samples){
        System.out.println("samples to flatten" + samples.size());
        int [] finalSamples = new int[samples.size()];
        for(int i=0;i<finalSamples.length;i++){
            Sample currentSample = samples.get(i);
            finalSamples[i] = flattenSample(currentSample);
        }
        return finalSamples;
    }
    public static int flattenSample(Sample sample){
        System.out.println("Sample to flatten "+sample);
        if(sample instanceof SingularSample){
            SingularSample singularSample = (SingularSample) sample;
            return singularSample.getValue();
        } else {
            List<SingularSample> singularSamples = new ArrayList<SingularSample>();
            Stack<Sample> sampleStack = new Stack<Sample>();
            sampleStack.push(sample);
            while(!sampleStack.isEmpty()){
                  Sample stackTop = sampleStack.pop();
                if(stackTop instanceof SingularSample){
                    singularSamples.add((SingularSample)stackTop);
                } else {
                    CompositeSample compositeSample = (CompositeSample) stackTop;
                    List<Sample> components = compositeSample.getComponents();
                    sampleStack.addAll(components);
                }
            }

            double totalSample = 0;

            for(SingularSample singularSample:singularSamples){
                totalSample+=singularSample.getValue();
            }

            return (int) Math.round(totalSample/singularSamples.size());
        }
    }
    public static Integer[] box(int [] input){
        Integer[] ret = new Integer[input.length];
        for(int i=0;i<input.length;i++){
            ret[i] = input[i];
        }
        return ret;
    }
    public static int[] unbox(Integer [] input){
        int[] ret = new int[input.length];
        for(int i=0;i<input.length;i++){
            ret[i] = input[i];
        }
        return ret;
    }
    public static List<Sample> blockLiteralToSamples(PianoParser.BlockLiteralContext block,SampleGenerator sg){

        List<List<Sample>> samples = new ArrayList<List<Sample>>();

        int depth = block.getChildCount();
        for(int i=1;i<depth-1;i++){
            List<Sample> samples1 = new ArrayList<Sample>();
            ParseTree parseTree = block.getChild(i);

            if(parseTree instanceof PianoParser.NoteLiteralContext){
                PianoParser.NoteLiteralContext note = (PianoParser.NoteLiteralContext) parseTree;
                samples1 = noteLiteralToSamples(note, sg);
            } else if(parseTree instanceof PianoParser.BlockLiteralContext){
                PianoParser.BlockLiteralContext block2 = (PianoParser.BlockLiteralContext) parseTree;
                samples1 = blockLiteralToSamples(block2, sg);
            } else if(parseTree instanceof PianoParser.SequenceLiteralContext){
                PianoParser.SequenceLiteralContext sequence2 = (PianoParser.SequenceLiteralContext) parseTree;
                samples1 = sequenceLiteralToSamples(sequence2,sg);
            }

            samples.add(samples1);

        }
        int maxlen = 0;
        for(int  i=0;i<samples.size();i++){
            if(maxlen < samples.get(i).size()){
                maxlen = samples.get(i).size();
            }
        }
        List<Sample> finalsamples = new ArrayList<Sample>();
        for(int i=0;i<maxlen;i++){
            List<Sample> addedSample = new ArrayList<Sample>();
            long sampleValue = 0;
            int numSamplesAdded = 0;
            for(int j=0;j<samples.size();j++){
                List<Sample> currentSampleSource = samples.get(j);
                if(currentSampleSource.size() > i){
                    numSamplesAdded++;
                    //sampleValue+=currentSampleSource[i];
                    Sample currentSample = currentSampleSource.get(i);
                    addedSample.add(currentSample);

                }
            }
            CompositeSample compositeSample = new CompositeSample(addedSample);
            if(numSamplesAdded==0) numSamplesAdded = 1;
            finalsamples.add(compositeSample);
            //finalsamples[i]= (int)(sampleValue/numSamplesAdded);
        }
        return finalsamples;
    }
    public static List<Sample> sequenceLiteralToSamples(PianoParser.SequenceLiteralContext sequence,SampleGenerator sg){

        List<Sample> samples = new ArrayList<Sample>();

        int depth = sequence.getChildCount();
        for(int i=1;i<depth-1;i++){
            List<Sample> samples1 = new ArrayList<Sample>();
            ParseTree parseTree = sequence.getChild(i);
            if(parseTree instanceof PianoParser.NoteLiteralContext){
                PianoParser.NoteLiteralContext note = (PianoParser.NoteLiteralContext) parseTree;
                samples1 = noteLiteralToSamples(note, sg);
            } else if(parseTree instanceof PianoParser.BlockLiteralContext){
                PianoParser.BlockLiteralContext block = (PianoParser.BlockLiteralContext) parseTree;
                samples1 = blockLiteralToSamples(block, sg);
            } else if(parseTree instanceof PianoParser.SequenceLiteralContext){
                PianoParser.SequenceLiteralContext sequence2 = (PianoParser.SequenceLiteralContext) parseTree;
                samples1 = sequenceLiteralToSamples(sequence2,sg);
            }
            samples.addAll(samples1);

        }
        return samples;
    }

    public static List<Sample> noteLiteralToSamples(PianoParser.NoteLiteralContext note, SampleGenerator sg){
        String musicalNote = note.musicalNote().getText();
        System.out.println("Octage "+note.octave().getText());
        int octave = Integer.parseInt(note.octave().getText());
        double freq = Converter.noteToFrequency(musicalNote,octave);
        System.out.println("Freq "+freq);
        System.out.println("NoteLen " + note.noteLength().getText());
        int length = Integer.parseInt(note.noteLength().getText());
        double duration = Converter.duration(length);
        int samples1[]= sg.generate(freq,duration);
        List<Sample> samples = new ArrayList<Sample>();
        for(int sample : samples1){
            samples.add(new SingularSample(sample));
        }
        return samples;
    }


}
