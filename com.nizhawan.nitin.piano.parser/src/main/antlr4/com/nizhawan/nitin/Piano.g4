grammar Piano;

musicalNote : 'A#' | 'C#' | 'D#' | 'F#' | 'G#' | 'A' | 'B' | 'C' | 'D' | 'E' | 'F' | 'G' | 'R' ;
octave : DIGIT;
noteLength : DIGIT+;
noteLiteral : musicalNote octave noteLength;
DIGIT : ('0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9') ;
blockLiteral : '{' (noteLiteral | blockLiteral | sequenceLiteral)* '}';
sequenceLiteral : '[' (noteLiteral | blockLiteral | sequenceLiteral)* ']';
compileUnit
    : noteLiteral | blockLiteral | sequenceLiteral
    ;
