package com.nizhawan.nitin.piano;

/**
 * Created by nitin on 02/06/17.
 */
public class SimpleSineGenerator implements SampleGenerator {
    private double volume;
    private double samplesPerSecond;
    public SimpleSineGenerator(int volume, int samplesPerSecond){
        this.samplesPerSecond = samplesPerSecond;
        this.volume = volume;
    }

    public int[] generate(double frequency, double duration) {
        int numSamples = (int) Math.round(duration*samplesPerSecond);
        int d [] = new int[numSamples];
        for(int i=0;i<numSamples;i++){
            double phase = i;
            d[i] = (int) Math.round(volume*Math.sin((phase*frequency*(2.0 * Math.PI))/samplesPerSecond));
        }
        return d;
    }
}
