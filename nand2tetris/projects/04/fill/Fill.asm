// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

//initialize some variables and read from the keyboard

(READ)
@SCREEN
D = A
@address
M = D //base address to the screen

@8192
D = A
@n  // number of iterations
M= D

//read from keyboard
@KBD
D = M

@WHITE // if keyboard is untouched, turn screen white
D; JEQ

@BLACK // else turn screen black
0; JMP



(WHITE)
@address
A = M
M = 0

@n
M = M -1
D = M

@READ
D ; JEQ

@address
M = M + 1

@WHITE
0 ; JMP


(BLACK)
@address
A = M
M = -1

@n
M = M -1
D = M

@READ
D ; JEQ

@address
M = M + 1

@BLACK
0 ; JMP
