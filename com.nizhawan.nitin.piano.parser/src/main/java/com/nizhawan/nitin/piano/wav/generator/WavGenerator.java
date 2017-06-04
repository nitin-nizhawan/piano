package com.nizhawan.nitin.piano.wav.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Created by nitin on 02/06/17.
 */
public class WavGenerator {
    public static void generateFile(int [] samples,File file) throws Exception {
        FileOutputStream fos = new FileOutputStream(file);
        PrintWriter pw = new PrintWriter(fos);
        for(int sample : samples){
            pw.println(sample+"");
        }
        pw.close();
        fos.close();
    }
}
