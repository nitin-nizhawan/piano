package com.nizhawan.nitin.piano.parser;

import com.nizhawan.nitin.piano.CompositeSample;
import com.nizhawan.nitin.piano.Sample;
import com.nizhawan.nitin.piano.SampleGenerator;
import com.nizhawan.nitin.piano.SingularSample;
import com.nizhawan.nitin.piano.parser.ast.BlockLiteral;
import com.nizhawan.nitin.piano.parser.ast.NoteFragment;
import com.nizhawan.nitin.piano.parser.ast.NoteLiteral;
import com.nizhawan.nitin.piano.parser.ast.Program;
import com.nizhawan.nitin.piano.parser.ast.SequenceLiteral;
import com.nizhawan.nitin.piano.parser.ast.VarRef;
import com.nizhawan.nitin.piano.parser.ast.VariableDefinition;
import com.nizhawan.nitin.piano.wav.generator.KarplusStrongGenerator;
import com.nizhawan.nitin.piano.wav.generator.WavGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by nitin on 04/06/17.
 */
public class ASTProcessor {
    Map<String,VariableDefinition> symbolTable = new HashMap<String,VariableDefinition>();
    Stack<Map<String,Integer>> scope = new Stack<Map<String,Integer>>();
    SampleGenerator sg;

    private void enterScope(int octaveOffset,int noteLengthMultiplier){
        Map<String,Integer> map = new HashMap<String, Integer>();
        map.put("octaveOffset",octaveOffset);
        map.put("noteLengthMultiplier",noteLengthMultiplier);
        scope.push(map);
    }
    private int getCurrentOctaveOffset(){
        if(scope.empty()){
            return 0;
        }
        return scope.peek().get("octaveOffset");
    }
    private int getCurrentNoteLengthMultiplier(){
        if(scope.empty()){
            return 1;
        }
        return scope.peek().get("noteLengthMultiplier");
    }
    private void exitScope(){
        scope.pop();
    }
    public ASTProcessor(){
        int SAMPLES_PER_SECOND = 44100;
        int volume = 32000;
        sg = new KarplusStrongGenerator(volume,SAMPLES_PER_SECOND);
    }
    public  void compile(String programStr,File outFile) throws Exception
    {



        //int samples [] = sg.generate(440.0,SAMPLES_PER_SECOND);

        //WavGenerator.generateFile(samples,new File("test.wav"));

        //String noteSentence = "[ {C416 E416 G416}  { C416 E416 G416} { C416 E416 G416} { C416 E416 G416} { C416 E416 G416}  ]";


        ASTGenerator asg = new ASTGenerator(programStr);
        Program program = asg.getProgram();


        int smpls[] = programToSamples(program);//flattenSamples(samples1);
        WavGenerator.generateFile(smpls, outFile);
    }
    public  int [] programToSamples(Program program){

        List<VariableDefinition> variableDefinitions = program.getVariableDefinitions();
        for(VariableDefinition variableDefinition : variableDefinitions){
            String varName = variableDefinition.getVarName();
            NoteFragment noteFragment = variableDefinition.getNoteFragment();
            symbolTable.put(varName,variableDefinition);
        }

        VariableDefinition mainVarDef = symbolTable.get("main");
        System.out.println("Variable definitions added");
        List<Sample> mainSamples = noteFragmentToSamples(mainVarDef.getNoteFragment());
        System.out.println("Starting flatten Samples");
        return flattenSamples(mainSamples);
    }
    private  List<Sample> noteFragmentToSamples(NoteFragment noteFragment){
        if(noteFragment.getBlockLiteral() != null){
            return blockLiteralToSamples(noteFragment.getBlockLiteral());
        } else if(noteFragment.getNoteLiteral() != null){
            return noteLiteralToSamples(noteFragment.getNoteLiteral());
        } else if(noteFragment.getSequenceLiteral() != null){
            return sequenceLiteralToSamples(noteFragment.getSequenceLiteral());
        } else if(noteFragment.getVarRef() != null){
            VarRef varRef = noteFragment.getVarRef();
            System.out.println("var Ref "+varRef.getNoteLengthMultiplier());
            VariableDefinition varDef = symbolTable.get(varRef.getVarName());

            enterScope(varRef.getOctaveOffset(),varRef.getNoteLengthMultiplier());
            List<Sample> samples = noteFragmentToSamples(varDef.getNoteFragment());
            System.out.println("Removing from scope"+getCurrentNoteLengthMultiplier());
            exitScope();
            return samples;
        } else {
            throw new IllegalStateException("Unexpected empty note fragment");
        }
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
       // System.out.println("Sample to flatten "+sample);
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
    public  List<Sample> blockLiteralToSamples(BlockLiteral block){

        List<List<Sample>> samples = new ArrayList<List<Sample>>();

        int depth = block.getNoteFragments().size();
        for(int i=0;i<depth;i++){
            NoteFragment noteFragment = block.getNoteFragments().get(i);
            List<Sample> samples1 = noteFragmentToSamples(noteFragment);
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
    public  List<Sample> sequenceLiteralToSamples(SequenceLiteral sequence){

        List<Sample> samples = new ArrayList<Sample>();

        int depth = sequence.getNoteFragments().size();
        for(int i=0;i<depth;i++){
            NoteFragment noteFragment = sequence.getNoteFragments().get(i);
            List<Sample> samples1 = noteFragmentToSamples(noteFragment);
            samples.addAll(samples1);
        }
        return samples;
    }

    public  List<Sample> noteLiteralToSamples(NoteLiteral noteLiteral){
        String musicalNote = noteLiteral.getNote();
        System.out.println("Octage "+noteLiteral.getOctave());
        int octave = noteLiteral.getOctave()+getCurrentOctaveOffset();
        double freq = Converter.noteToFrequency(musicalNote,octave);
        System.out.println("Freq "+freq);
        System.out.println("NoteLen " + noteLiteral.getDuration());
        int multiplier = getCurrentNoteLengthMultiplier();
        int length = noteLiteral.getDuration()* multiplier;
        System.out.println("NoteLen After Multiplying " + length);
        double duration = Converter.duration(length);
        int samples1[]= sg.generate(freq,duration);
        List<Sample> samples = new ArrayList<Sample>();
        for(int sample : samples1){
            samples.add(new SingularSample(sample));
        }
        return samples;
    }
}
