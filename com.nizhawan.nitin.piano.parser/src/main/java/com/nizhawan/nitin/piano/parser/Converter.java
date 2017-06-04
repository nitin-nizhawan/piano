package com.nizhawan.nitin.piano.parser;

/**
 * Created by nitin on 02/06/17.
 */
public class Converter {

    /*
    int getOctave(char* line){
        if (line[1]=='#' || line[1]=='b'){
            return line[2]-'0';
        } else {
            return line[1]-'0';
        }
    }*/

    public static int getNoteOrder(String line){
        int order=0;
        switch(line.charAt(0)){
            case 'C':
                order = 0;
                break;
            case 'D':
                order = 2;

                break;
            case 'E':
                order = 4;

                break;
            case 'F':
                order = 5;

                break;
            case 'G':
                order = 7;

                break;
            case 'A':
                order = 9;

                break;
            case 'B':
                order = 11;

                break;
        }
        if(line.length() > 1) {
            if (line.charAt(1) == 'b') {
                order--;
            } else if (line.charAt(1) == '#') {
                order++;
            }
        }
        return order;
    }
   public static double getFreq(String note,int octave){
        if(note.charAt(0)=='R'){
            return 0.0;
        }
        int noteIndex = getNoteOrder(note);

       // printf("octable %d\n",octave);
        double keyNum = 12*octave +  noteIndex - 8;
       // printf("Key Num: %lf\n",keyNum);
        return  Math.pow(2, (keyNum - 49.0) / 12.0)*440.0;
    }
    /*
    int getDuration(char *line){
        char d = 4;
        if(line[1] == '#' || line[1] == 'b'){
            d= line[3];
        } else {
            d= line[2];
        }
        if(d=='F'){
            d = 16;
        } else if(d == 'H'){
            d = 8;
        } else if(d == 'Q' || d == '4'){
            d=4;
        } else if(d == '2'){
            d = 2;
        } else if(d=='1'){
            d = 1;
        }
        if(d>16 || d < 1){
            return 4;
        }
        return d;
    }*/
    public static double noteToFrequency(String note,int octave){
            return getFreq(note,octave);
    }
    public static double duration(int noteLength){
        return ((double)noteLength)/16.0;
    }
}
