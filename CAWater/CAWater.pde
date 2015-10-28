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

void setup()
{
  size(600,600, FX2D);
  cell_width = width/map_width;		   	 			//	Sets blocks width depending on window and grid size
  cell_height = height/map_height;	   				//	Sets blocks height depending on window and grid size
  initMap();								      	// Draw Random map
}	

//	defines block color; 0=AIR, 1=STONE, 2=WATER
color cellColor(int c)
{
  color[] cellColors = {color(255, 255, 255), color(160, 80, 40), color(21, 24, 117)};	
  return cellColors[c];
}			
