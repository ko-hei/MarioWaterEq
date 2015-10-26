final int MAP_WIDTH = 16;
final int MAP_HEIGHT= 16;

final float CORNER = 0; 

// Tools to make the simulation more handy!
boolean grid = false;  // grid-variable 


int [][] blocks = new int[MAP_WIDTH+2][MAP_HEIGHT+2];  // Any reason why we should add 2?

int block_width, block_height;
//Color definition
color[] block_colors = {
color(255),color(160,82,45)};  // For now, only the color of the ground is defined

//// [ Setting up the environment] ////
void setup(){
  size(599,600);
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

void draw_rect(int x, int y, color c, float filled, float corner){
  float screen_x = screen_x( x - 0 );
  float screen_y = screen_y( y - -1 );
  float block_y = screen_y(y-1+filled);
  
  fill(c);
  
  rect( screen_x, block_y, block_width, screen_y-block_y,CORNER);
  
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
  
       blocks[x][y] = int(random(0,2));
   }
 }

 
 //for ( int i = 0; i<100; i++){
  
 //     int mx = int(random(1,17));
 //     int my = int(random(1,17));
 //     draw_rect(mx,my,block_colors[ 1 ],1);

//}

}


//

//void initmap(){
//  for ( int x = 0; x < MAP_HEIGHT + 2; x++ ){
//    for ( int y = 0; y < MAP_HEIGHT + 2; y++ ){
//      blocks[x][y] = int(random(0, 2));
//    }  
//  } 
  
// for (int x =0; x < MAP_WIDTH+2; x++){
//    blocks[x][0] = AIR;
//    blocks[x][MAP_HEIGHT+1] = AIR;
//  }
  
//  for (int y = 1; y < MAP_HEIGHT+1; y++){
//    blocks[0][y] = AIR;
//    blocks[MAP_WIDTH+1][y] = AIR;
//  }

//}

void draw(){
  background(255);
  
  for ( int x = 0; x < MAP_HEIGHT; x++){
   for ( int y = 0; y < MAP_WIDTH; y++){
     if ( blocks[x][y] == AIR){
       draw_rect(x,y,block_colors[0],1,CORNER);
     }else{
         draw_rect(x,y,block_colors[1],1,CORNER);
       }
     }
   }
  
      
  //background(255);
  //initmap();
  /*for ( int i = 0; i<100; i++){
    int mx = int(random(0,16));
    int my = int(random(0,16));
    draw_rect(mx,my,block_colors[0],1);
  }*/
}