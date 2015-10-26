
final int I = 4;
final int  J = 4;
color c;
final int MAP_WIDTH = 16;

final int MAP_HEIGHT= 16;
int block_width, block_height;

int [][] blocks = new int[I+2][J+2];  // Any reason why we should add 2?

//// [ Setting up the environment] ////
void setup(){
 
  size(800,600);
  smooth();
  background(100);
  
  block_width = width/MAP_WIDTH;
  block_height= height/MAP_HEIGHT;
  
  initmap();
}

void draw_rect(int i, int j, color c, float filled){
  float screen_x = screen_x( i - 1 );
  float screen_y = screen_y( j - 1 );
  float block_y = screen_y(j-1+filled);
  
  rect( screen_x,block_y,block_width,screen_y-block_y);
  
}

float screen_x( float i ){
  return i * block_width;
}
float screen_y( float j ){
  return (MAP_HEIGHT - j)*block_height;
}


void draw(){
  background(-39);
  for(int i = 0; i < I; i++){
    for(int j = 0; j < J; j++){
      matrix[i][j] = rect(50,50,50,50,4);
  }
  }
}