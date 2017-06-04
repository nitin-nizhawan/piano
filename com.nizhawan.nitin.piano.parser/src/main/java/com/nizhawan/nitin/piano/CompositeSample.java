package com.nizhawan.nitin.piano;

import java.util.List;

/**
 * Created by nitin on 03/06/17.
 */
public class CompositeSample implements Sample {
    List<Sample> samples;
    public CompositeSample(List<Sample> samples){
        this.samples = samples;
    }
    public List<Sample> getComponents(){
        return samples;
    }
}
