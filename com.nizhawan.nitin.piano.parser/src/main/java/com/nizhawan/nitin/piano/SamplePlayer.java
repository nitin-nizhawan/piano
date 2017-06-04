package com.nizhawan.nitin.piano;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

/**
 * Created by nitin on 04/06/17.
 */
public class SamplePlayer {
    public static void play(int [] samples){
        try {
            // select audio format parameters
            AudioFormat af = new AudioFormat(44100, 16, 1, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);

            // generate some PCM data (a sine wave for simplicity)
            byte[] buffer = new byte[64];

            // prepare audio output
            line.open(af, 4096);
            line.start();
            // output wave form repeatedly
            for (int n=0; n<samples.length;) {
                for(int i=0;i<64 && n < samples.length;i+=2){
                    int curSample = samples[n];
                    buffer[i] = (byte) curSample;
                    buffer[i+1] = (byte) (curSample >> 8);
                    n++;
                }
                line.write(buffer, 0, buffer.length);
            }
            // shut down audio
            line.drain();
            line.stop();
            line.close();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
