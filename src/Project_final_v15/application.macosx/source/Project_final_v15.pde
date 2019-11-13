import processing.sound.*;   // Import the library that does the sound handling.

//Declaring Background
PImage bg;
int bgx = 0; 
int speed;

//Declaring Sounds
SoundFile greenHillJingle; //BGM
SoundFile jumpSound; //declare jumpsound
SoundFile crabCollisionSound;
SoundFile spikeCollisionSound;
SoundFile crabExplosionSound;

//Declaring Sonic
sonicChar sonic;
int charX;
int charY;

//Declaring Jumping Mechanics
boolean jump;
boolean badJump;
int badJumpCounter;
boolean aboveGround;
int distanceFromGround;
int jumpPoint;
int groundPoint;

//Declaring Enemy Generator
float enemyRandomizer;

//Declaring Max Speed Counter;
int SpeedCounter;

//Declaring Badnick of name Crab
badnick crab;
float crabX;

//Declaring Collisions
boolean explode;
boolean gameOver;
boolean theFall;

void setup () {

  size (800, 600);
  bg = loadImage("bg.jpg");
  bg.resize(800, 600);
  SpeedCounter = 0;
  gameOver = false;
  theFall = false;

  //Initialize sounds 
  greenHillJingle  = new SoundFile(this, "greenHillJingle.mp3");
  jumpSound  = new SoundFile(this, "jumpSound.mp3");
  crabExplosionSound = new SoundFile(this, "crabExplosionSound.mp3");
  crabCollisionSound = new SoundFile (this, "crabCollisionSound.mp3");
  spikeCollisionSound = new SoundFile (this, "spikeCollisionSound.mp3");

  //Play BGM
  greenHillJingle.play();
  greenHillJingle.loop();

  //Initialize Grounding and Jumping Mechanics
  jumpPoint = 450;  //how high sonic can jump
  groundPoint = 300;  //how low can sonic go
  distanceFromGround = groundPoint;
  jump = false;
  aboveGround = false;
  badJumpCounter = 0;

  //Initialize Sonic
  sonic = new sonicChar();

  //Initialize Crab
  crab = new badnick ();
  crabX = width;
}

void draw () {

  //Speed accelerating. Increase denominator of fraction to decrease acceleration. Constrained between 0 and the maximum speed.
  speed = constrain((7 + (millis()/1500)), 0, 30); 

  //Image copied twice. First on the far left of screen.
  //Second outside the screen on the right.:
  image(bg, bgx, 0);
  image(bg, bgx+bg.width, 0);

  //Motion of background 
  bgx= bgx - speed;

  //Looping. When one image leaves the screen, 
  //It goes back to x=0 while the second copy plays.
  if (bgx < -1 * bg.width) {
    bgx=0;
  }

  //Accumulates score if game is not over
  if (gameOver == false) {
    SpeedCounter = SpeedCounter + int(speed/4);
  }

  //Scoreboard
  fill (0);
  textSize(20);
  text("Distance Covered: " + SpeedCounter + "m", 40, 50);

  //Sonic's position on screen
  charX = 30;

  //If game is on, sonic runs on the ground and can be controlled with spacebar.
  //If game is over, sonic goes up, then down.
  //Game over animations.
  if (gameOver == false) {
    charY = height-distanceFromGround;
  } else if ((gameOver == true) && (charY > 100) && (theFall == false)) {
    charY = charY - 20;
  } else if (charY == 100) {
    theFall = true;
  }
  if (theFall == true) {
    charY = charY + 25;
    fill (0);
    textSize (30);
    text("Game Over", 300, 250);
  }

  //Check if sonic is above ground
  if (charY < groundPoint) {
    aboveGround = true;
  } else {
    aboveGround = false;
  }

  //Render sonic at x-pos charX, y-pos charY, and check jump to see to spin or not. Spin if aboveGround.
  sonic.renderChar (charX, charY, aboveGround, gameOver); 

  //If sonic is high, set jump to false.
  if (distanceFromGround >= jumpPoint) {
    jump = false;
  }

  //If jump is true, move up to jump point.
  if (jump == true) {
    distanceFromGround = distanceFromGround + 20; //change the last +int to change jump-up speed
  }

  //If jump is false, move down to ground point.
  if (jump == false) {
    distanceFromGround = constrain((distanceFromGround - 15), groundPoint, jumpPoint); //change the last -int to change jump-down speed
  }

  //The enemyRandomizer picks a random float from 0 to 10, once per frame. If the picked float is less than a specified amount, an enemy is rendered.
  enemyRandomizer = random(0, 10);

  //Collisions

  //Check if crab was hit (crab-plosion!)
  if ((crabX < charX+100) && (crabX > -150) && (charY > 220) && (aboveGround == true) && (jump == false)) {
    explode = true;
    crabExplosionSound.play();
  }

  //Check if sonic was hit (Game Over!)
  if ((crabX < charX+100) && (crabX > 0) && (aboveGround == false) && (explode == false) && (gameOver == false)) {
    gameOver = true;
    crabCollisionSound.play();
  }

  //Render badnick. If the float picked by the enemyRandomizer is less than 0.1, draw the crab.
  crab.renderBadnick (crabX, explode);
  if (enemyRandomizer < 0.1 && crabX < 0) {
    explode = false;
    crabX = width;
  }
  crabX = crabX - speed*1.8; //sets scrolling motion of crab

  //Detects bad jumps using the boolean badJump ====== to do.

  if (((crabX < -250) || (crabX > width)) && (jump == true)) {
    badJump = true;
  } else {
    badJump = false;
  }

  //Counts bad jumps using the in badJumpCounter.
  if (badJump == true) {
    badJumpCounter = badJumpCounter + 1;
  }

  //Punish bad jumps.
  if ((badJumpCounter/8 > 4) && (theFall == false)) {
    gameOver = true;
    spikeCollisionSound.play();
  }

  //Print the badJumpCounter.
  fill (0);
  textSize (20);
  text ("Bad Jumps: " + badJumpCounter/8 +"/5", 600, 50);

  //What to print to console
  println ("Speed: " + speed + ", bad jump?: " + badJump + ", bad jump counter = " + badJumpCounter);
}

void keyPressed () {

  //set jump to true if spacebar is pressed
  if ((key == ' ') && (aboveGround == false) && (gameOver == false) && (jump == false)) {
    jump = true;
    jumpSound.play();
  }
}
