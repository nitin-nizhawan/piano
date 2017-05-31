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
const int MAX_NOTES=7000;

short waveform[10000000];
short waveform2[10000000];
const int NUM_SAMPLES = (WAVFILE_SAMPLES_PER_SECOND*2);
const int NOTE_SAMPLES =(int) (((double)WAVFILE_SAMPLES_PER_SECOND)/4.0);// samples in quater note
double freqs[MAX_NOTES];
double freqs2[MAX_NOTES];
int duration2[MAX_NOTES];
int duration[MAX_NOTES];// in multiples of 1/16th note, a quater note is 4, full note is 16
int getOctave(char* line){
   if (line[1]=='#' || line[1]=='b'){
      return line[2]-'0'; 
   } else {
      return line[1]-'0';
   }
}
int getNoteOrder(char *line){
   int order=0;
   switch(line[0]){
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
   if(line[1]=='b'){
      order--;
   } else if(line[1]=='#'){
      order++;
   }
   return order;
}
double getFreq(char *line){
    if(line[0]=='R'){
      return 0.0;
    }
    int noteIndex = getNoteOrder(line);
    int octave = getOctave(line);
    printf("octable %d\n",octave);
    double keyNum = 12*octave +  noteIndex - 8;
    printf("Key Num: %lf\n",keyNum);
    return  pow(2,(keyNum - 49.0)/12.0)*440.0;
}
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
} 
int parseWords(char * line,char buff[500][500]){
   int i=0;
   int j=0;
   int k=0;
   while(line[i]!='\n' && line[i]!='\r' && line[i] != '\0'){
      while(line[i] != ' ' && line[i]!='\n' && line[i] !='\r' && line[i] != '\0'){
        buff[j][k++]=line[i];
        i++;
      }
      if(k>0){
         buff[j][k]='\0';
         k=0;
         j++;
      }
      if(line[i]==' '){
          i++;
      }
   }
   return j;
}
int readFreqs(char * fileName,double *freqs,int * duration) {
     FILE *f = fopen(fileName,"r");
     char line[1024];
     char words[500][500]; 
     int i=0;
     int j=0;
     while(fgets(line,sizeof(line),f)){
       if(line[0]=='#'){
            printf("%s\n",line);
            continue; //ignore comments
       }
         int num_words = parseWords(line,words);
       for(j=0;j<num_words;j++){
         freqs[i]=getFreq(words[j]); 
         duration[i++]=getDuration(words[j]);
         printf("%lf\n",freqs[i-1]);
       } 
     } 
     fclose(f);
     return i;
}

int getTotalDuration(int* duration,int num_notes){
       int total_duration=0;
       int z=0;
        for(z=0;z<num_notes;z++){
           total_duration+=duration[z];
        }
       return total_duration;
} 
void generateSamples(int NUM_NOTES,double* freqs,int* duration,int total_samples,short* waveform){

	int volume = 32000;
 int k;
int l=0;
int i;
printf("start generating samples\n");
for(k=0;k<NUM_NOTES;k++){
   int NUM_SAMPLES = (duration[k]*WAVFILE_SAMPLES_PER_SECOND)/16;
   if(freqs[k] < 2){ //silence
       for(i=0;i<NUM_SAMPLES;i++){
          waveform[l++]=0; 
       }
       continue;
   }
   double damping_factor = pow(0.000001,16.0/(duration[k]*freqs[k])); //
   printf("damping %lf\n",damping_factor);
   int delay_line = (int) (((double)WAVFILE_SAMPLES_PER_SECOND)/freqs[k]);
   printf("delay_line %d\n",delay_line);
   int repeat = (int) (freqs[k] * (double) duration[k])/16.0;
   printf("repeat %d\n",repeat);
   int j;
   int x_i;
   int y_i;
   for(i=0;i<delay_line;i++){
       waveform[l++] = volume*((double)rand()/RAND_MAX);
   }
   for(j=1;j<repeat;j++){
       for(i=0;i<delay_line;i++){
           x_i = waveform[l-delay_line];
           y_i = waveform[l-1];
           waveform[l++] = y_i + damping_factor*(x_i-y_i);
       }
   }
       
}
//printf("total_samples_generated %d\n",l);
while(l<total_samples){
     waveform[l++]=0;
}

 
}
int main(int argc, char** argv)
{
       if(argc < 2){
          printf("Usage : ./example <filename>\n");
          return 0;
       }
       
        int NUM_NOTES2 = 0;
        if(argc > 2){
           NUM_NOTES2 = readFreqs(argv[2],freqs2,duration2);
        } 
        int NUM_NOTES = readFreqs(argv[1],freqs,duration); 
        int total_duration = getTotalDuration(duration,NUM_NOTES);
        int total_duration2 = getTotalDuration(duration2,NUM_NOTES2);

        int total_samples = (total_duration*WAVFILE_SAMPLES_PER_SECOND)/16+16;
        int total_samples2 = (total_duration2*WAVFILE_SAMPLES_PER_SECOND)/16+16;
        printf("total_samples %d\n",total_samples);
	int volume = 32000;
	int length = NUM_SAMPLES;
// gen karplus strong samples
generateSamples(NUM_NOTES,freqs,duration,total_samples,waveform);
generateSamples(NUM_NOTES2,freqs2,duration2,total_samples2,waveform2);
/*
int k;
int l=0;
int i;
printf("start generating samples\n");
for(k=0;k<NUM_NOTES;k++){
   int NUM_SAMPLES = (duration[k]*WAVFILE_SAMPLES_PER_SECOND)/16;
   if(freqs[k] < 2){ //silence
       for(i=0;i<NUM_SAMPLES;i++){
          waveform[l++]=0; 
       }
       continue;
   }
   double damping_factor = pow(0.000001,16.0/(duration[k]*freqs[k])); //
   printf("damping %lf\n",damping_factor);
   int delay_line = (int) (((double)WAVFILE_SAMPLES_PER_SECOND)/freqs[k]);
   printf("delay_line %d\n",delay_line);
   int repeat = (int) (freqs[k] * (double) duration[k])/16.0;
   printf("repeat %d\n",repeat);
   int j;
   int x_i;
   int y_i;
   for(i=0;i<delay_line;i++){
       waveform[l++] = volume*((double)rand()/RAND_MAX);
   }
   for(j=1;j<repeat;j++){
       for(i=0;i<delay_line;i++){
           x_i = waveform[l-delay_line];
           y_i = waveform[l-1];
           waveform[l++] = y_i + damping_factor*(x_i-y_i);
       }
   }
       
}
//printf("total_samples_generated %d\n",l);
while(l<total_samples){
     waveform[l++]=0;
}
*/

/*
gen pure tone samples
	int i;
        int k;
        int l=0;
     for(k=0;k<NUM_NOTES;k++){
        int NUM_SAMPLES = (duration[k]*WAVFILE_SAMPLES_PER_SECOND)/16;
	for(i=0;i<NUM_SAMPLES;i++) {
		double t = (double) i / WAVFILE_SAMPLES_PER_SECOND;
		waveform[l++] = volume*sin(freqs[k]*t*2*M_PI);
	}
     }
*/
int l=0;
while(l<total_samples2){
   if(waveform2[l]>1){
      waveform[l] = (waveform[l] + waveform2[l])/2;
   }
l++;
}


	FILE * f = wavfile_open("sound.wav");
	if(!f) {
		printf("couldn't open sound.wav for writing: %s",strerror(errno));
		return 1;
	}

	wavfile_write(f,waveform,total_samples);
	wavfile_close(f);

	return 0;
}
