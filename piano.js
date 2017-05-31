function noteNameToNoteIndex(noteName){
   var noteSequence  = ["C","C#","D",
                        "D#","E","F",
                        "F#","G","G#","A","A#","B"];
   var noteSeq2 = ["A","Bb","B",
                        "C","Db","D",
                        "Eb","E","F",
                        "Gb","G","Ab"];                        
   return noteSequence.indexOf(noteName);
}
function getKeyNum(noteSignature){
    var noteName = noteSignature.substring(0,noteSignature.length-1);
   var noteIndex = noteNameToNoteIndex(noteName);
   var octave = parseInt(noteSignature.substring(noteSignature.length-1,noteSignature.length));
   var keyNum = 12*octave + noteIndex - 8;
   return keyNum;
}

function getFrequency(noteSignature){   
   var keyNum = getKeyNum(noteSignature);
   var exp = (keyNum - 49)/12;
   var freq = Math.pow(2,exp) * 440.0;
   return freq;
}
function generateSample(sampleNumber,freq) {
  let angularFreq = freq*2*Math.PI;
  let currentTime = sampleNumber / 44100;
  let currentAngle = currentTime * angularFreq;
  return Math.sin(currentAngle);
}

var p = document.querySelector("#inp").innerText.split(",").map(function(a){
     return a.trim();
});;
var t = 0.25;
var samples = t*44100;
let audioContext = new AudioContext();
let myBuffer = audioContext.createBuffer(1, samples*p.length, 44100);
let myArray = myBuffer.getChannelData(0);
for(var k=0;k<p.length;k++){
   var freq = getFrequency(p[k]);
   for (let sampleNumber = 0 ; sampleNumber < samples; sampleNumber++) {
      myArray[samples*k+sampleNumber] = generateSample(sampleNumber,freq);
    }
}
let src = audioContext.createBufferSource();
src.buffer = myBuffer;
src.connect(audioContext.destination);
src.start();


