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
  void renderChar (int xPos, int yPos, boolean spinning, boolean dead) {

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
