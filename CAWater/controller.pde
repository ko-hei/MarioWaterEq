/*
	Will controll the simulation and other stuff. It will manipulate the data. 
*/

void mousePressed()
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

void update()
{
  simulation();
}

// converts matrix pos to pixel pos. (depends on block size)
int convertX(int x)
{
  return x*block_width;
}

int convertY(int y)
{
  return y*block_height;
}

int xToBlock(int x)
{
  return x/(block_width);
}

int yToBlock(int y)
{
  return y/(block_height); 
}


void simulation()
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
void below(int x, int y)
{
  if (blocks[x][y+1] != STONE)	//	If below currentblock != STONE
  {
    blocks[x][y+1] = blocks[x][y];
    //drawblock(x, y+1, blockColor(blocks[x][y]));
    //drawblock(5, 5, blockColor(WATER));
  }
}

void initMap()
{
  //	Goes through entire grid
  for ( int x = 0; x < map_height; ++x )	
  {
    for ( int y = 0; y < map_height; ++y )
    {
      blocks[x][y] = int(random(0, 2));		//	Set random states
    }
  }
}



