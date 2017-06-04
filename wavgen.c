/*
This example program makes use of the simple
sound library to generate a sine wave and write the
output to sound.wav.
For complete documentation on the library, see:
http://www.nd.edu/~dthain/courses/cse20211/fall2013/wavfile
Go ahead and modify this program for your own purposes.
*/


#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <errno.h>

#include "wavfile.h"

short waveform[10000000];
int main(int argc, char** argv)
{
        FILE * fin = fopen(argv[1],"r");
        int sample;
           
	FILE * f = wavfile_open("out.wav");
	if(!f) {
		printf("couldn't open sound.wav for writing: %s",strerror(errno));
		return 1;
	}
        int total_samples=0;

        while(fscanf(fin,"%d",&sample)>0){
            waveform[total_samples++]=(short) sample;
        }
	wavfile_write(f,waveform,total_samples);
	wavfile_close(f);
        fclose(fin);
	return 0;
}
