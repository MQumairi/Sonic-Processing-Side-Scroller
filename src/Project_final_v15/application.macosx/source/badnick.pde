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

  void renderBadnick (float ObsXPos, boolean destroyed) {

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
