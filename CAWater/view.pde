/*
	It will draw everything that it can access from the model. 
*/

//	Draws everything on the screen
void draw()
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
void drawCell(int x, int y, color c)
{
  fill(c);
  rect(convertX(x), convertY(y), cell_width, cell_height);
}
