/*
	Will controll the simulation and other stuff. It will manipulate the data. 
*/


// sets correct x coordinate which depends on block size
int convertX(int x)
{
  return x*cell_width;
}
// sets correct y coordinate which depends on block size
int convertY(int y)
{
  return y*cell_height;
}

void simulation()
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
void below(int x, int y)
{
  if (cells[x][y+1] != STONE)	//	If below currentCell != STONE
  {
    cells[x][y+1] = cells[x][y];
    //drawCell(x, y+1, cellColor(cells[x][y]));
    //drawCell(5, 5, cellColor(WATER));
  }
}

void initMap()
{
  //	Goes through entire grid
  for ( int x = 0; x < map_height; ++x )	
  {
    for ( int y = 0; y < map_height; ++y )
    {
      cells[x][y] = int(random(0, 2));		//	Set random states
    }
  }
}



