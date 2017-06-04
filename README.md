# piano
Piano is tiny language for describing musical sequences in textual format
## Getting Started
### Running the program
```
cd com.nizhawan.nitin.piano.parser
mvn clean install
java -jar target/com.nizhawan.nitin.piano.parser-1.0.0-SNAPSHOT.jar src/test/resources/furelise.txt
```
Above will play program in furelise.txt

## Notes
In piano system, notes are denotes by alphabets A to G. Also '#' is used to denote sharp and 'b' is used to denote flats as usual.
A single digit following note is used to denote octave and number followed that is used to denote note length
Examples of note

```
A316  # denotes a full A note in 3rd octave
D#28 # denotes half D sharp note in second octave
Eb54 # denotes a quater E flat note in 5 octave 
```
## Sequences
Sequences are used to denote notes one after the other. Sequence are denoted by enclosing series of notes between square brackets
Example of note sequences
```
[ A34 D#34 Eb48 ]
```
Above sequence will play the notes one after the other
## Blocks
Blocks are used to denote notes which are played simultaneously. Blocks are denoted by enclosing series of notes between curly braces
Blocks can be used to denote cords
Example of note blocks
```
  { C34 E34 G34 }
```
Above block will play as C major chord
## Variables
Variables allow one to give name to musical sequences. These names can then be used in other musical sequences
For examples
```
  cmajor+={C34 E34 G34}
  main+=[
    cmajor cmajor cmajor
  ]
```
Above program defines cmajor and then uses it in `main` sequence thrice. This should play cmajor cord thrice
## Nesting
Sequence and blocks can be arbitrarily nested within each other. Such that one can define different clefs and can have chords or notes being played 
within each clef
