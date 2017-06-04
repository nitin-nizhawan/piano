package com.nizhawan.nitin.piano;

/**
 * Created by nitin on 03/06/17.
 */
public class SingularSample implements  Sample {
    int sample;
    public SingularSample(int sample){
        this.sample = sample;
    }
    public int getValue(){
        return sample;
    }
}
