import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Project_final_v15 extends PApplet {

   // Import the library that does the sound handling.

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

public void setup () {

  
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

public void draw () {

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
    SpeedCounter = SpeedCounter + PApplet.parseInt(speed/4);
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
  if (enemyRandomizer < 0.1f && crabX < 0) {
    explode = false;
    crabX = width;
  }
  crabX = crabX - speed*1.8f; //sets scrolling motion of crab

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

public void keyPressed () {

  //set jump to true if spacebar is pressed
  if ((key == ' ') && (aboveGround == false) && (gameOver == false) && (jump == false)) {
    jump = true;
    jumpSound.play();
  }
}
class badnick {

  int numFrames = 6;  // The number of frames in badnick animation
  int numFramesExplode = 5;  // The number of frames in explpsion.
  int currentFrame = 0; //current frame of badnick animatin
  int currentFrameExplode = 0; //current frame of explosion
  PImage [] badnick = new PImage [numFrames]; //badnick animation array
  PImage [] explosion = new PImage [numFrames]; //badnick animation array

  badnick () {

    //Innitialize array for running animation
    for (int i = 0; i < 6; i = i+1) {
      badnick[i] = loadImage("badnick" + i + ".png");
    }

    //Innitialize array for explosion animation
    for (int i = 0; i < 4; i = i+1) {
      explosion[i] = loadImage("explode" + i + ".png");
    }
  }

  public void renderBadnick (float ObsXPos, boolean destroyed) {

    //Looping frame for badnick animation
    if (currentFrame == 6) {
      currentFrame = 0;
    } else {
      currentFrame = currentFrame + 1;
    }

    //Looping frame for explosion animation
    if (currentFrameExplode == 4) {
      currentFrame = 0;
    } else {
      currentFrame = currentFrame + 1;
    }

    //Checks if the badnick is destroyed or not. 
    //If yes, renders the explosion animation.
    //If no, renders the badnick moving.
    if (destroyed == true) {
      image(explosion[(currentFrameExplode) % numFramesExplode], ObsXPos, 300);
    } else {
      //Draw badnick at varying X-position (ObsXPos), and fixed Y-position of 300 px.
      image(badnick[(currentFrame) % numFrames], ObsXPos, 300);
    }
  }
}
class sonicChar {

  int numFrames = 12;  // The number of frames in sonic running sprite
  int numFramesSpin = 11;  // The number of frames in sonic spinning sprite
  int currentFrame = 0; //Current frame of sonic running sprite
  int currentFrameSpin = 0; //Current frame of sonic spinning sprite
  PImage [] sonicChar = new PImage [numFrames]; //sonic run array
  PImage [] sonicSpin = new PImage [numFramesSpin]; //sonic spin array
  PImage sonicDead; //Declare sonic dead image
  boolean jump;

  sonicChar () {

    frameRate(30);
    jump = false;

    //Innitialize array for running animation
    for (int i = 0; i < 12; i = i+1) {
      sonicChar[i] = loadImage("sonicChar" + i + ".png");
    }

    //Innitialize array for spinning animation
    for (int s = 0; s < 11; s = s+1) {
      sonicSpin[s] = loadImage("spin" + s + ".png");
    }

    //Initialize sonic dead image
    sonicDead = loadImage("sonicDead.png");
  }

  //Draws sonic positioned xPos yPos, checks if he is spinning, checks if he is dead.
  public void renderChar (int xPos, int yPos, boolean spinning, boolean dead) {

    //Looping frame for running animation
    if (currentFrame == 13) {
      currentFrame = 0;
    } else {
      currentFrame = currentFrame + 1;
    }

    //Looping frame for spinning animation
    if (currentFrameSpin == 12) {
      currentFrameSpin = 0;
    } else {
      currentFrameSpin = currentFrameSpin + 1;
    }

    //Either sonic is dead or not.
    //If dead, render sonicDead. 
    //If not dead, either he is above the ground or not.
    //If above ground, render sonic spin.
    //If neither dead, nor above ground, render sonic running.
    if (dead == true) {
      image(sonicDead, xPos, yPos);
    } else if (dead == false) {
      //Check if sonic should be rendered as running or spinning
      if (spinning == false) {
        image(sonicChar[(currentFrame) % numFrames], xPos, yPos);
      } else if (spinning == true) {
        image(sonicSpin[(currentFrameSpin) % numFramesSpin], xPos, yPos);
      }
    }
  }
}
  public void settings() {  size (800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Project_final_v15" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
