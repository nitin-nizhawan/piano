package com.nizhawan.nitin.piano.wav.generator;

import com.nizhawan.nitin.piano.SampleGenerator;

/**
 * Created by nitin on 02/06/17.
 */
public class KarplusStrongGenerator implements SampleGenerator {
    double volume;
    double samplesPerSecond;
    public KarplusStrongGenerator(int volume, int samplesPerSecond){
        this.volume = volume;
        this.samplesPerSecond = samplesPerSecond;
    }
    public int[] generate(double frequency, double duration) {

        int numSamples = (int) Math.round(duration*samplesPerSecond);
        int samples[] = new int[numSamples];
        if(frequency < 2.0){
            for(int i=0;i<samples.length;i++){
                samples[i]  = 0;
            }
            return samples;
        }
        int delay_line = (int) (samplesPerSecond / frequency);
        int repeat = (int) (frequency*duration);

        int samplesCount=0;
        for(int i=0;i<delay_line;i++){
             samples[samplesCount++] = (int) Math.round(volume* (Math.random()*2 - 1));
        }
        int x_i;
        int y_i;
        double damping_factor = Math.pow(0.000001,1.0/(duration*frequency));
        for(int j=1;j<repeat;j++){
            for(int i=0;i<delay_line;i++){
                 x_i = samples[samplesCount-delay_line];
                y_i = samples[samplesCount-1];
                if(samplesCount >= samples.length){
                    break;
                }
                samples[samplesCount++] = (int)(y_i + damping_factor * (x_i - y_i));
            }
        }

        return samples;
    }
}
