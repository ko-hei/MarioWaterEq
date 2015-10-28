/*
	This is model file containing all the math, data structures, etc.
*/

// UI stuff
color hoverLight = color(180, 250, 180, 200);

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

void setup()
{
	frameRate(60);
	size(600,600, FX2D);
  	block_width = width/map_width;				//	Sets blocks width depending on window and grid size
  	block_height = height/map_height;	   		//	Sets blocks height depending on window and grid size
  	initMap();								// Draw Random map
}	

//	Defines block color; 0=AIR, 1=STONE, 2=WATER
color blockColor(int c)
{
  color[] blockColors = {color(255, 255, 255), color(160, 80, 40), color(21, 24, 117)};	
  return blockColors[c];
}			
