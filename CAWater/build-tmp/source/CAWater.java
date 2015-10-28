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

//	Grid and cell sizes
final int map_width = 20;
final int map_height = 20;
      int cell_width;   
      int cell_height;  

//	Cell states
final int AIR = 0;
final int STONE = 1;
final int WATER = 2;

//	Data structures for cells and cell variables
int[][] cells = new int[map_width][map_height];		//	contains state information

public void setup()
{
  
  cell_width = width/map_width;		   	 			//	Sets blocks width depending on window and grid size
  cell_height = height/map_height;	   				//	Sets blocks height depending on window and grid size
  initMap();								      	// Draw Random map
}	

//	defines block color; 0=AIR, 1=STONE, 2=WATER
public int cellColor(int c)
{
  int[] cellColors = {color(255, 255, 255), color(160, 80, 40), color(21, 24, 117)};	
  return cellColors[c];
}			
/*
	Will controll the simulation and other stuff. It will manipulate the data. 
*/


// sets correct x coordinate which depends on block size
public int convertX(int x)
{
  return x*cell_width;
}
// sets correct y coordinate which depends on block size
public int convertY(int y)
{
  return y*cell_height;
}

public void simulation()
{
  float remainingMass;	//	Need to keep track of the mass in the current Cell

  //	Goes through grid-1
  for ( int x = 1; x < map_height-1; ++x )	
  {
    for ( int y = 1; y < map_height-1; ++y )
    {
      below(x, y);
    }
  }
}

//	Check cell below current cell
public void below(int x, int y)
{
  if (cells[x][y+1] != STONE)	//	If below currentCell != STONE
  {
    cells[x][y+1] = cells[x][y];
    //drawCell(x, y+1, cellColor(cells[x][y]));
    //drawCell(5, 5, cellColor(WATER));
  }
}

public void initMap()
{
  //	Goes through entire grid
  for ( int x = 0; x < map_height; ++x )	
  {
    for ( int y = 0; y < map_height; ++y )
    {
      cells[x][y] = PApplet.parseInt(random(0, 2));		//	Set random states
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
  //simulation();							//	Runs the simulation before drawing

  //	Drawing all the blocks - goes through entire grid
  for ( int x = 0; x < map_height; ++x )	
  {
    for ( int y = 0; y < map_height; ++y )
    {
      if (cells[x][y] == AIR)						// we are only interested in water
      {
        drawCell(x, y, cellColor(AIR));
      } else
      {
        drawCell(x, y, cellColor(STONE));		//	else draw whatever
      }
    }
  }
}

//	code for drawing the blocks
public void drawCell(int x, int y, int c)
{
  fill(c);
  rect(convertX(x), convertY(y), cell_width, cell_height);
}
  public void settings() {  size(600,600, FX2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "CAWater" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
