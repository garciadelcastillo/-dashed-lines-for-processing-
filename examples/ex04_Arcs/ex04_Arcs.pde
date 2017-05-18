/**
 * DASHED ARCS
 * The different ways arcs can be drawn with dashed arcs. 
 * 
 * Dashed Lines for Processing, by Jose Luis Garcia del Castillo 2017
 * https://github.com/garciadelcastillo/-dashed-lines-for-processing- 
 */

import garciadelcastillo.dashedlines.*;

DashedLines dash;
Node[] n;
float dist = 0;

void setup() {
  size(800, 600);
  fill(255, 0, 0, 100);
  strokeWeight(5);
  strokeCap(SQUARE);
  textAlign(CENTER);

  dash = new DashedLines(this);
  dash.pattern(30, 10);
  
  initializeNodes();
}

void draw() {
  background(255);
  pushStyle();
  strokeWeight(1);
  stroke(127, 100);
  line(n[0].x, n[0].y, n[1].x, n[1].y);
  line(n[0].x, n[0].y, n[2].x, n[2].y);
  popStyle();
  renderNodes();
  
  // Compute angle range
  float angle1 = atan2(n[1].y - n[0].y, n[1].x - n[0].x);
  float angle2 = atan2(n[2].y - n[0].y, n[2].x - n[0].x);
  
  // Draw a dashed arc just like Processing would! :)
  dash.arc(n[0].x, n[0].y, n[1].x - n[0].x, n[2].y - n[0].y, angle1, angle2); 
  
  // Dashed arcs accept modes
  //dash.arc(n[0].x, n[0].y, n[1].x - n[0].x, n[2].y - n[0].y, angle1, angle2, OPEN); 
  //dash.arc(n[0].x, n[0].y, n[1].x - n[0].x, n[2].y - n[0].y, angle1, angle2, CHORD); 
  //dash.arc(n[0].x, n[0].y, n[1].x - n[0].x, n[2].y - n[0].y, angle1, angle2, PIE); 
    
  // Processing by default treates the range angles as the 't' parameter  
  // in the parametric definition of an ellipse (see http://www.mathopenref.com/coordparamellipse.html).
  // This may sometimes lead to unintuitive results visually. If you intend to specify the range
  // of the arc as polar angles, use dash.arcPolar() instead:
  //dash.arcPolar(n[0].x, n[0].y, n[1].x - n[0].x, n[2].y - n[0].y, angle1, angle2); 
  
  
  // Animate dashes with 'walking ants' effect 
  dash.offset(dist);
  dist += 1;

  pushStyle();
  fill(0);
  text("Drag nodes around", 0.5 * width, height - 25);
  popStyle();
}




void initializeNodes() {
  n = new Node[3];
  n[0] = new Node(0.5 * width, 0.5 * height, 5);
  n[1] = new Node(0.75 * width, 0.5 * height, 5);
  n[2] = new Node(0.5 * width, 0.75 * height, 5);
}

void renderNodes() {
  for (int i = 0; i < n.length; i++) {
    n[i].render();
  }
}


// A quick class for a draggable node
class Node {

  float x, y;
  float r;
  boolean dragged;

  Node(float x_, float y_, float r_) {
    x = x_;
    y = y_;
    r = r_;
  }

  void render() {
    pushStyle();
    ellipseMode(CENTER);
    noStroke();
    fill(127, 100);
    ellipse(x, y, 2 * r, 2 * r);
    popStyle();
    if (dragged) {
      x = mouseX;
      y = mouseY;
    }
  }

  boolean inside(float xpos, float ypos) {
    return dist(x, y, xpos, ypos) < r;
  }
}

void mousePressed() {
  for (int i = 0; i < n.length; i++) {
    if (n[i].inside(mouseX, mouseY)) {
      n[i].dragged = true;
    }
  }
}

void mouseReleased() {
  for (int i = 0; i < n.length; i++) {
    n[i].dragged = false;
  }
}