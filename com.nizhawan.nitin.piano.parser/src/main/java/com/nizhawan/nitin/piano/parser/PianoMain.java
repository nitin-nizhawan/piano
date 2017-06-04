package com.nizhawan.nitin.piano.parser;


import com.nizhawan.nitin.piano.SamplePlayer;
import com.nizhawan.nitin.piano.parser.ast.Program;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PianoMain
{
    public static byte [] readFully(InputStream is) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte [] buff = new byte[4*1024*1024];

        int bytesRead = 0;
        while((bytesRead = is.read(buff)) > 0){
            bos.write(buff,0,bytesRead);
        }

        return bos.toByteArray();
    }
    public static void main(String[] args)throws Exception {
       String filePath = args[0];
       File file = new File(filePath);
       FileInputStream fis = new FileInputStream(file);
       byte [] bytes = readFully(fis);
        String str = new String(bytes);
        ASTGenerator astGenerator = new ASTGenerator(str);
        Program program = astGenerator.getProgram();
        ASTProcessor processor = new ASTProcessor();
        int [] samples = processor.programToSamples(program);
        SamplePlayer.play(samples);
        fis.close();
    }
}
