final int MAP_WIDTH = 16;
final int MAP_HEIGHT= 16;

final float CORNER = 0; 

final int AIR = 0;
final int GROUND = 1;
final int WATER = 2;

color[] block_colors = {
color(255),color(160,82,45),color(70,120,160)};  // For now, only the color of the ground is defined


// Tools to make the simulation more handy!
boolean grid = false;  // grid-variable 

int [][] blocks = new int[MAP_WIDTH][MAP_HEIGHT];  // Any reason why we should add 2?

int block_width, block_height;
//Color definition

// Color for the mouse!
color m_color = color(150,150,200,40);

//// [ Setting up the environment] ////
void setup(){
  size(600,600);
  smooth();
  background(180);
  
//  frameRate(0.5);
  
  // This shows and hides the grid-lines
  if( grid ){
    stroke(0);
  }else{
    noStroke();
  }
  
  block_width = width/MAP_WIDTH;
  block_height= height/MAP_HEIGHT;
  initmap();
}

void draw_rect(int x, int y, color c, float filled){
  float screen_x = screen_x( x -0);
  float screen_y = screen_y( y +1);
  float block_y = screen_y( y - 0);
  
  fill(c);
  rect( screen_x, block_y, block_width, screen_y-block_y);
  
}

float screen_x( float x ){
  return x * block_width;
}
float screen_y( float y ){
  return (MAP_HEIGHT - y)*block_height;
}

void initmap(){
 for( int x = 0; x < MAP_WIDTH; x++){
   for( int y = 0; y < MAP_HEIGHT; y++){
  
       blocks[x][y] = int(random(0,3));
   }
 }

 
 //for ( int i = 0; i<100; i++){
  
 //     int mx = int(random(1,17));
 //     int my = int(random(1,17));
 //     draw_rect(mx,my,block_colors[ 1 ],1);

//}

}

void draw(){
  background(255);

  int mx = constrain(int (mouseX/block_width),0,block_width);
  int my = constrain( MAP_HEIGHT - int (mouseY/block_height+1),0,block_height);
  
  for ( int x = 0; x < MAP_HEIGHT; x++){
   for ( int y = 0; y < MAP_WIDTH; y++){
     if ( blocks[x][y] == AIR){
       draw_rect(x,y,block_colors[0],1);
     }else if (blocks[x][y] == GROUND){
         draw_rect(x,y,block_colors[1],1);
       }else{
         draw_rect(x,y,block_colors[2],1);
     }
   }
     noStroke();
     draw_rect( mx ,my, m_color ,1);
     mouse();
 
 
 
 }
  
  //background(255);
  //initmap();
  /*for ( int i = 0; i<100; i++){
    int mx = int(random(0,16));
    int my = int(random(0,16));
    draw_rect(mx,my,block_colors[0],1);
  }*/
}

void mouse(){
  int mx = constrain(int (mouseX/block_width),0,block_width);
  int my = constrain( MAP_HEIGHT - int (mouseY/block_height+1),0,block_height);

    if (mousePressed ) {
      int block = -1;
      if ( mouseButton == LEFT ){
        block = GROUND;
      }else if (mouseButton == RIGHT ) {
        block = WATER;
        
      }
        
      if ( block != -1){
        blocks[mx][my] = block;
      }
    }
  }
  
  