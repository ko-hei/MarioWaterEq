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

public class water_sim extends PApplet {

/*
 * A simple water simulation based on cellular automata
 *
 * by Janis Elsts / W-Shadow
 *
 * Info : http://w-shadow.com/blog/2009/09/01/simple-fluid-simulation/
 */

//Block types
final int AIR = 0;
final int GROUND = 1;
final int WATER = 2;

//Water properties
final float MaxMass = 1.0f;
final float MaxCompress = 0.02f;
final float MinMass = 0.0001f;

final float MinDraw = 0.01f;
final float MaxDraw = 1.1f;

final float 
  MaxSpeed = 1;   //max units of water moved out of one block to another, per timestep

final float MinFlow = 0.01f;

//Define map dimensions and data structures
final int map_width = 128;   
final int map_height = 128;

int[][] blocks = new int[map_width+2][map_height+2];

float[][] mass = new float[map_width+2][map_height+2],
          new_mass = new float[map_width+2][map_height+2];

//Window size
final int display_width = 600;
final int display_height = 600;
//Set the size of the top panel
final int panel_height = 25;
int       panel_width = display_width;

//Block size will be automatically calculated based on the above settings.
int block_width, block_height, hblock_width, hblock_height;
          
//Define colors
int[] block_colors = {
  color(255),        //air
  color(200,200,100) //ground
};

//Mouse highlight
int highlight = color(180, 250, 180, 200);

//..and the font
PFont font;

//Control variables
boolean stepping = false;   //run the simulation in stepping mode (press Space to run a single step)
boolean grid = false;       //show the grid
boolean draw_depth = true; //draw partially filled cells differently, simulating more
                            //fine-grained water depth display (imperfect)
                            
final int SHOW_NOTHING = 0;                            
final int SHOW_MASS = 1;
int show_state = SHOW_NOTHING; //whether to show the mass of each block

public void setup(){
  
  
  background(180);
  
  block_width = width / map_width;
  block_height = (height - panel_height) / map_height;
    
  hblock_width = block_width / 2;
  hblock_height = block_height / 2;
  
  panel_width = block_width * map_width;
  
  //create a random map
  initmap();
  
  //load the font
  font = loadFont("FreeSans-24.vlw"); 
  textFont(font, 12); 
  textAlign(CENTER, CENTER);
  
  if ( grid ){
    stroke(0);
  } else {
    noStroke();
  }
}

public void simulate(){
  simulate_compression();
}

public void draw(){
  background(255);
  
  int mx = constrain( PApplet.parseInt(mouseX / block_width)+1, 1, map_width );
  int my = constrain( map_height - PApplet.parseInt( (mouseY - panel_height) / block_height), 1, map_height );
  boolean shift = keyPressed && (key == CODED) && (keyCode == SHIFT);
  
  //Edit the map if the mouse is pressed 
  if ( mousePressed ){
    int block = -1;
    
    if (mouseButton == LEFT){
      block = GROUND;
    } else if (mouseButton == RIGHT){
      block = WATER;
    }
    
    if (shift) {
      if ( blocks[mx][my] == block ){
        block = AIR;
      } else {
        block = -1;
      }
    }
    
    if ( block != -1 ){
      blocks[mx][my] = block;
      mass[mx][my] = new_mass[mx][my] = block == WATER ? MaxMass : 0;
    }
  }
  
  //Run the water simulation (unless we're in the step-by-step mode)
  if (!stepping){
    simulate();
  }
  
  //Draw the map
  float h = 0;
  int c;
  
  textSize( 12 );
  textAlign( CENTER, CENTER );
  
  if ( grid ){
    stroke(0);
  } else {
    noStroke();
  }
  
  for ( int x = 1; x <= map_height; x++ ){
    for ( int y = 1; y <= map_height; y++ ){
      if ( blocks[x][y] == WATER ){
        
        //Skip cells that contain very little water
        if (mass[x][y] < MinDraw) continue;
        
        //Draw water
        if ( draw_depth && ( mass[x][y] < MaxMass ) ){
          //Draw a half-full block. Block size is dependent on the amount of water in it.
          if ( mass[x][y+1] >= MinDraw ){
            draw_block( x, y, waterColor(mass[x][y+1]), 1);
          }
          draw_block( x, y, waterColor(mass[x][y]), mass[x][y]);
        } else {
          //Draw a full block
          h = 1;
          c = waterColor( mass[x][y] );
          draw_block( x, y, c, h);
        }
        
      } else {
        //Draw any other block
        draw_block( x, y, block_colors[ blocks[x][y] ], 1 );
      }
      
    }  
  }
  
  //draw_marching(); //Marching squares. Doesn't look very good.
  
  //Draw the highlight under the mouse
  noStroke();
  draw_block( mx , my, highlight, 1);
  
  draw_panel();
}

public void draw_block( int x, int y, int c, float filled ){
  float screen_x = screen_x( x - 1 );
  float screen_y = screen_y( y - 1 );
  float block_y = screen_y(y - 1 + filled);
  
  fill(c);
  rect( screen_x, block_y, block_width, screen_y - block_y );
  
  if ( (show_state == SHOW_MASS) && ( mass[x][y] >= MinMass )){
    fill( 0, 0, 0, 255);
    text( mass[x][y] , screen_x + hblock_width, screen_y - hblock_height);
  }
}

//Calculates an RGB water color based on the amount of water in the cell
public int waterColor( float m ){
  m = constrain( m, MinDraw, MaxDraw );
  
  int r = 50, g = 50;
  int b;
  
  if (m < 1){
    b = PApplet.parseInt(map(m, 0.01f, 1, 255, 200));
    r = PApplet.parseInt(map(m, 0.01f, 1, 240, 50));
    r = constrain(r, 50, 240);
    g = r;
  } else {
    b = PApplet.parseInt(map(m, 1, 1.1f, 190, 140));
  }

  b = constrain(b, 140, 255);
  
  return color( r, g, b );
}

public void draw_panel(){
  fill(120, 250, 100);
  rect(0, 0, panel_width, panel_height);
  int offset = width - panel_width;
  
  fill(0);
  textSize( 16 );
  //Display FPS
  textAlign( RIGHT, CENTER );
  text( round(frameRate) + " FPS", panel_width - 5 - offset, panel_height/2 )  ;

  if ( stepping ) {
    textAlign( LEFT, CENTER );
    text( "Press [Space] to step the simulation", 5, panel_height/2 )  ;
  }
  
  if ( show_state == SHOW_MASS ){
    textAlign( CENTER, CENTER );
    text( "[Mass]", panel_width - 100 - offset, panel_height/2 )  ;
  } 
  
}

//A basic marching squares implementation. For testing only, doesn't animate very well.
public void draw_marching(){
  textAlign(CENTER, CENTER); fill(0);
  stroke(50);
  textSize(14);
  float screen_x;
  float screen_y;
  
  for ( int x = 1; x <= map_height; x++ ){
    for ( int y = 1; y <= map_height; y++ ){
      //march!        
      
      float mass_f = 0.5f;
      float 
        mass_bl = mass[x][y]*mass_f,          //bottom-left
        mass_br = mass[x+1][y]*mass_f,      //bottom-right
        mass_tl = mass[x][y+1]*mass_f,      //top-left
        mass_tr = mass[x+1][y+1]*mass_f;  //top-right
      
      int state;
      
      state = (
        ( (mass_bl >= MinDraw) ? 1 : 0) + 
        ( (mass_br >= MinDraw) ? 2 : 0) +
        ( (mass_tr >= MinDraw) ? 4 : 0) +
        ( (mass_tl >= MinDraw) ? 8 : 0)
      );
      
      float zx = x - 1 + 0.5f;
      float zy = y - 1 ;
      
      float px1, py1, px2, py2;
      
      switch (state) {
        case 1 : 
         line(
           screen_x( zx ),
           screen_y( zy + mass_bl),
           screen_x( zx + mass_bl),
           screen_y( zy  ) 
         );
         break;
         
       case 2 : 
         line(
           screen_x( zx + 1 - mass_br),
           screen_y( zy ),
           screen_x( zx + 1 ),
           screen_y( zy + mass_br) 
         );
         break;
         
       case 3 : 
         line(
           screen_x( zx ),
           screen_y( zy + mass_bl),
           screen_x( zx + 1 ),
           screen_y( zy + mass_br ) 
         );
         break;
       
       case 4 : 
         line(
           screen_x( zx + 1 - mass_tr),
           screen_y( zy + 1),
           screen_x( zx + 1 ),
           screen_y( zy + 1 - mass_tr ) 
         );
         break;
         
       case 5 : 
         line(
           screen_x( zx ),
           screen_y( zy + mass_bl),
           screen_x( zx + mass_bl ),
           screen_y( zy ) 
         );
         
         line(
           screen_x( zx + 1 - mass_tr),
           screen_y( zy + 1),
           screen_x( zx + 1 ),
           screen_y( zy + 1 - mass_tr) 
         );         
         break;
         
      case 6 : 
         line(
           screen_x( zx + 1 - mass_tr),
           screen_y( zy + 1),
           screen_x( zx + 1 - mass_br ),
           screen_y( zy ) 
         );
         break; 
         
       case 7 : 
         line(
           screen_x( zx ),
           screen_y( zy + mass_bl ),
           screen_x( zx + 1 - mass_tr ),
           screen_y( zy + 1) 
         );
         break;
         
       case 8 : 
         line(
           screen_x( zx ),
           screen_y( zy + 1 - mass_tl ),
           screen_x( zx + mass_tl ),
           screen_y( zy + 1 ) 
         );
         break;
         
       case 9 : 
         line(
           screen_x( zx + mass_tl),
           screen_y( zy + 1),
           screen_x( zx + mass_bl ),
           screen_y( zy ) 
         );
         break;   
         
       case 10 : 
         line(
           screen_x( zx + mass_tl ),
           screen_y( zy + 1 ),
           screen_x( zx ),
           screen_y( zy + 1 - mass_tl) 
         );
       
        line(
          screen_x( zx + 1 - mass_br),
          screen_y( zy ),
          screen_x( zx + 1 ),
          screen_y( zy + mass_br) 
        );         
        break;
        
       case 11 : 
         line(
           screen_x( zx + mass_tl),
           screen_y( zy + 1 ),
           screen_x( zx + 1 ),
           screen_y( zy + mass_br ) 
         );
         break;
         
       case 12 : 
         line(
           screen_x( zx ),
           screen_y( zy + 1 - mass_tl),
           screen_x( zx + 1 ),
           screen_y( zy + 1 - mass_tr ) 
         );
         break;
         
       case 13 : 
         line(
           screen_x( zx + mass_bl ),
           screen_y( zy ),
           screen_x( zx + 1 ),
           screen_y( zy + 1 - mass_tr ) 
         );
         break;
      
       case 14 : 
         line(
           screen_x( zx ),
           screen_y( zy + 1 - mass_tl),
           screen_x( zx + 1 - mass_br),
           screen_y( zy) 
         );
         break;
         
      }
      
      screen_x = (x - 1)*block_width;
      screen_y = (map_height - y)*block_height + panel_height;
      //text( x + "/" + y, screen_x + block_width, screen_y );
      text( state , screen_x + hblock_width, screen_y + hblock_height);
    }  
  }  
}

public float screen_x( float x ){
  return x  * block_width;
}

public float screen_y( float y ){
  return (map_height - y)*block_height + panel_height;
}

//Fill the map with random blocks
public void initmap(){
  for ( int x = 0; x < map_height + 2; x++ ){
    for ( int y = 0; y < map_height + 2; y++ ){
      blocks[x][y] = PApplet.parseInt(random(0, 3));
      mass[x][y] = blocks[x][y] == WATER ? MaxMass : 0.0f;
      new_mass[x][y] = blocks[x][y] == WATER ? MaxMass : 0.0f;
    }  
  } 
  
 for (int x =0; x < map_width+2; x++){
    blocks[x][0] = AIR;
    blocks[x][map_height+1] = AIR;
  }
  
  for (int y = 1; y < map_height+1; y++){
    blocks[0][y] = AIR;
    blocks[map_width+1][y] = AIR;
  }

}

//Clear the map
public void clearmap(){
  for ( int x = 1; x <= map_height; x++ ){
    for ( int y = 1; y <= map_height; y++ ){
      blocks[x][y] = AIR;
      mass[x][y] = 0;
      new_mass[x][y] = 0;
    }  
  }
  
  for (int x =0; x < map_width+2; x++){
    blocks[x][0] = AIR;
    blocks[x][map_height+1] = AIR;
  }
  
  for (int y = 1; y < map_height+1; y++){
    blocks[0][y] = AIR;
    blocks[map_width+1][y] = AIR;
  }
}

public void keyPressed(){
  if (key == CODED) return;
  
  switch(key){
    case 's':  //Toggle step-by-step simulation
    case 'S': 
      stepping = !stepping;
      break;
    
    case 'c':  //Clear the map
    case 'C':
      clearmap();
      break;
      
    case 'r':  //Reinitialize the map with random cells
    case 'R':
      initmap();
      break;
      
    case 'n':  //Toggle mass display
    case 'N':
      show_state++;
      if ( show_state > 1 ) show_state = SHOW_NOTHING;
      break;
      
    case 'g': //Toggle grid
    case 'G':
      grid = !grid;
      if ( grid ){
        stroke(0);
      } else {
        noStroke();
      }
      break;
      
    case 'd':  //Toggle between basic water rendering and depth-based (=mass-based) rendering
    case 'D':
      draw_depth = !draw_depth;
      break;
      
    default: 
      if (stepping) {
        simulate();
      };
  }

}
/* Compression-based fluid dynamic simulator.
 * uses map_width, map_height, mass, new_mass, blocks, MaxMass, MaxCompression
 *
 * Blocks can contain more than MaxMass water.
 */
public void simulate_compression(){
  float Flow = 0;
  float remaining_mass;
  
  //Calculate and apply flow for each block
  for (int x = 1; x <= map_width; x++){
     for(int y = 1; y <= map_height; y++){
       //Skip inert ground blocks
       if ( blocks[x][y] == GROUND) continue;
       
       //Custom push-only flow
       Flow = 0;
       remaining_mass = mass[x][y];
       if ( remaining_mass <= 0 ) continue;
       
       //The block below this one
       if ( (blocks[x][y-1] != GROUND) ){
         Flow = get_stable_state_b( remaining_mass + mass[x][y-1] ) - mass[x][y-1];
         if ( Flow > MinFlow ){
           Flow *= 0.5f; //leads to smoother flow
         }
         Flow = constrain( Flow, 0, min(MaxSpeed, remaining_mass) );
         
         new_mass[x][y] -= Flow;
         new_mass[x][y-1] += Flow;   
         remaining_mass -= Flow;
       }
       
       if ( remaining_mass <= 0 ) continue;
       
       //Left
       if ( blocks[x-1][y] != GROUND ){
         //Equalize the amount of water in this block and it's neighbour
         Flow = (mass[x][y] - mass[x-1][y])/4;
         if ( Flow > MinFlow ){ Flow *= 0.5f; }
         Flow = constrain(Flow, 0, remaining_mass);
          
         new_mass[x][y] -= Flow;
         new_mass[x-1][y] += Flow;
         remaining_mass -= Flow;
       }
       
       if ( remaining_mass <= 0 ) continue;

       //Right
       if ( blocks[x+1][y] != GROUND ){
         //Equalize the amount of water in this block and it's neighbour
         Flow = (mass[x][y] - mass[x+1][y])/4;
         if ( Flow > MinFlow ){ Flow *= 0.5f; }
         Flow = constrain(Flow, 0, remaining_mass);
          
         new_mass[x][y] -= Flow;
         new_mass[x+1][y] += Flow;
         remaining_mass -= Flow;
       }
       
       if ( remaining_mass <= 0 ) continue;
       
       //Up. Only compressed water flows upwards.
       if ( blocks[x][y+1] != GROUND ){
         Flow = remaining_mass - get_stable_state_b( remaining_mass + mass[x][y+1] );
         if ( Flow > MinFlow ){ Flow *= 0.5f; }
         Flow = constrain( Flow, 0, min(MaxSpeed, remaining_mass) );
         
         new_mass[x][y] -= Flow;
         new_mass[x][y+1] += Flow;   
         remaining_mass -= Flow;
       }

       
     }
  }
  
  //Copy the new mass values to the mass array
  for (int x = 0; x < map_width + 2; x++){
    for (int y = 0; y < map_height + 2; y++){
      mass[x][y] = new_mass[x][y];
    }
  }
  
  for (int x = 1; x <= map_width; x++){
     for(int y = 1; y <= map_height; y++){
       //Skip ground blocks
       if(blocks[x][y] == GROUND) continue;
       //Flag/unflag water blocks
       if (mass[x][y] > MinMass){
         blocks[x][y] = WATER;
       } else {
         blocks[x][y] = AIR;
       }
     }
  }
  
  //Remove any water that has left the map
  for (int x =0; x < map_width+2; x++){
    mass[x][0] = 0;
    mass[x][map_height+1] = 0;
  }
  for (int y = 1; y < map_height+1; y++){
    mass[0][y] = 0;
    mass[map_width+1][y] = 0;
  }

}
 //Take an amount of water and calculate how it should be split among two //vertically adjacent cells. Returns the amount of water that should be in
//the bottom cell.
public float get_stable_state_b ( float total_mass ){
  if ( total_mass <= 1 ){
    return 1;
  } else if ( total_mass < 2*MaxMass + MaxCompress ){
    return (MaxMass*MaxMass + total_mass*MaxCompress)/(MaxMass + MaxCompress);
  } else {
    return (total_mass + MaxCompress)/2;
  }
}
  
/*
Explanation of get_stable_state_b (well, kind-of) : 

if x <= 1, all water goes to the lower cell
	* a = 0
	* b = 1
	
if x > 1 & x < 2*MaxMass + MaxCompress, the lower cell should have MaxMass + (upper_cell/MaxMass) * MaxCompress
	b = MaxMass + (a/MaxMass)*MaxCompress
	a = x - b
	
	->
	
	b = MaxMass + ((x - b)/MaxMass)*MaxCompress ->
        b = MaxMass + (x*MaxCompress - b*MaxCompress)/MaxMass
        b*MaxMass = MaxMass^2 + (x*MaxCompress - b*MaxCompress)
        b*(MaxMass + MaxCompress) = MaxMass*MaxMass + x*MaxCompress
        
        * b = (MaxMass*MaxMass + x*MaxCompress)/(MaxMass + MaxCompress)
	* a = x - b;
	
if x >= 2 * MaxMass + MaxCompress, the lower cell should have upper+MaxCompress
	  
	b = a + MaxCompress
	a = x - b 
	
	->
	
	b = x - b + MaxCompress ->
	2b = x + MaxCompress ->
	
	* b = (x + MaxCompress)/2
	* a = x - b
  */

  public void settings() {  size(601, 626);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "water_sim" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
