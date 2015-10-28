import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class CAWater extends PApplet {

/*
	This is model file containing all the math, data structures, etc.
*/

// UI stuff
int hoverLight = color(180, 250, 180, 200);

//	Grid and block sizes
final int map_width = 20;
final int map_height = 20;
      int block_width;   
      int block_height;  

//	Block states
final int AIR = 0;
final int STONE = 1;
final int WATER = 2;

//	Data structures for the blocks - it contains state info
int[][] blocks = new int[map_width][map_height];

public void setup()
{
	frameRate(60);
	
  	block_width = width/map_width;				//	Sets blocks width depending on window and grid size
  	block_height = height/map_height;	   		//	Sets blocks height depending on window and grid size
  	initMap();								// Draw Random map
}	

//	Defines block color; 0=AIR, 1=STONE, 2=WATER
public int blockColor(int c)
{
  int[] blockColors = {color(255, 255, 255), color(160, 80, 40), color(21, 24, 117)};	
  return blockColors[c];
}			
/*
	Will controll the simulation and other stuff. It will manipulate the data. 
*/

public void mousePressed()
{
  if(mouseButton == LEFT)
  {
    blocks[xToBlock(mouseX)][yToBlock(mouseY)] = 1;
  }
  else if(mouseButton == RIGHT)
  {
    blocks[xToBlock(mouseX)][yToBlock(mouseY)] = 2;
  }
}

public void update()
{
  simulation();
}

// converts matrix pos to pixel pos. (depends on block size)
public int convertX(int x)
{
  return x*block_width;
}

public int convertY(int y)
{
  return y*block_height;
}

public int xToBlock(int x)
{
  return x/(block_width);
}

public int yToBlock(int y)
{
  return y/(block_height); 
}


public void simulation()
{
  float remainingMass;	//	Need to keep track of the mass in the current block

  //	Goes through grid-1
  for ( int x = 1; x < map_height-1; ++x )	
  {
    for ( int y = 1; y < map_height-1; ++y )
    {
      below(x, y);
    }
  }
}

//	Check block below current block
public void below(int x, int y)
{
  if (blocks[x][y+1] != STONE)	//	If below currentblock != STONE
  {
    blocks[x][y+1] = blocks[x][y];
    //drawblock(x, y+1, blockColor(blocks[x][y]));
    //drawblock(5, 5, blockColor(WATER));
  }
}

public void initMap()
{
  //	Goes through entire grid
  for ( int x = 0; x < map_height; ++x )	
  {
    for ( int y = 0; y < map_height; ++y )
    {
      blocks[x][y] = PApplet.parseInt(random(0, 2));		//	Set random states
    }
  }
}



/*
	It will draw everything that it can access from the model. 
*/

//	Draws everything on the screen
public void draw()
{
  background(245);						//	Sets the background color of the window
  simulation();
  //	Drawing all the blocks - goes through entire grid
  for ( int x = 0; x < map_height; ++x )  
  {
    for ( int y = 0; y < map_height; ++y )
    {
      if (blocks[x][y] == WATER)            // we are only interested in water
      {
        drawblock(x, y, blockColor(WATER));
      } 
      else if(blocks[x][y] == STONE)
      {
        drawblock(x, y, blockColor(STONE));   //  else draw whatever
      }
      else
      {
        drawblock(x, y, blockColor(AIR));   //  else draw whatever
      }
    }
  }
  drawblock(xToBlock(mouseX), yToBlock(mouseY), hoverLight);
}

//	code for drawing the blocks
public void drawblock(int x, int y, int c)
{
  fill(c);
  rect(convertX(x), convertY(y), block_width, block_height);
}







  public void settings() { 	size(600,600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CAWater" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
