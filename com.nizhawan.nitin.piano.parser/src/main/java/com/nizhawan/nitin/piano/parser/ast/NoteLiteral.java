package com.nizhawan.nitin.piano.parser.ast;

/**
 * Created by nitin on 04/06/17.
 */
public class NoteLiteral {
    public String getNote() {
        return note;
    }

    public int getOctave() {
        return octave;
    }

    public int getDuration() {
        return duration;
    }

    String note;
    int octave;
    int duration;
    public NoteLiteral(String note, int octave, int duration){
        this.note = note;
        this.octave = octave;
        this.duration = duration;
    }
    public String toString(){
        return note+octave+duration;
    }
}
