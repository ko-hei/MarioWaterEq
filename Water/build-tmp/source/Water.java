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

public class Water extends PApplet {

/*
 * MarioWaterEquation with Cellular automata
 * Class SW101 - Simulating with KojSyd
 */
/*################## GLOBALS ####################*/

//	Window size 
final int display_width = 600;
final int display_height = 600;

//	Cell size - set in setup()
int cell_width;		
int cell_height;	

//	Grid
final int map_width = 20;
final int map_height = 20;

//	Cell states
final int AIR = 0;
final int MUD = 1;
final int WATER = 2;

//	Data structures for cells and cell variables
int[][] cells = new int[map_width][map_height];			//	contains state information

//	initialization
public void setup()
{
  
    cell_width = width/map_width;			//	Sets blocks width depending on window and grid size
  cell_height = height/map_height;		//	Sets blocks height depending on window and grid size
  initMap();								//	Draw Random map
}	

//	Draws everything on the screen
public void draw()
{
  background(245);						//	Sets the background color of the window
  simulation();							//	Runs the simulation before drawing

  //	Drawing all the blocks - goes through entire grid
  for ( int x = 0; x < map_height; ++x )	
  {
    for ( int y = 0; y < map_height; ++y )
    {
      if (cells[x][y] == WATER)						// we are only interested in water
      {
        drawCell(x, y, cellColor(WATER));
      } else
      {
        drawCell(x, y, cellColor(cells[x][y]));		//	else draw whatever
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

//	defines block color; 0=AIR, 1=MUD, 2=WATER
public int cellColor(int c)
{
  int[] cellColors = {color(255, 255, 255), color(77, 77, 77), color(21, 24, 117)};	
  return cellColors[c];
}			

/*	############### SIMULATION ##################	*/

/* * Overall algorithm: 
 * 
 * If there are two or more water cells stacked
 * vertically, the bottom cells will be able to hold slightly
 * more water than normal. This way we won't need to track pressure gradients. 
 * We can just look at how much excess water a cell has and move it upwards if required. 
 *
 *  Steps: 
 *	1. 	Take the mass of the current cell and the cell below it and
 *		figure out how much water the bottom cell should contain.
 *	  	If it has less than that, remove the corresponding amount
 *		from the current cell and add it to the bottom cell.
 *
 *	2. 	Check the cell to the left of "current cell". If it has less water,
 *		move over enough water to make both cells contain the same amount.
 *		Do the same thing for the right neighbour.
 *
 *	3. 	Do the same thing as in step 1., but for the cell above the current one.
 *
 */

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
  if (cells[x][y+1] != MUD)	//	If below currentCell != MUD
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
      cells[x][1] = PApplet.parseInt(random(0, 3));		//	Set random states
    }
  }
}
  public void settings() {  size(600,600, P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Water" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
