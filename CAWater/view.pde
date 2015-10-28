/*
	It will draw everything that it can access from the model. 
*/

//	Draws everything on the screen
void draw()
{
  background(245);						//	Sets the background color of the window
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
void drawblock(int x, int y, color c)
{
  fill(c);
  rect(convertX(x), convertY(y), block_width, block_height);
}


void hover(int x, int y, color rgb)
{
  //noStroke();
  drawblock( xToBlock(x) , yToBlock(y), hoverLight);
}






