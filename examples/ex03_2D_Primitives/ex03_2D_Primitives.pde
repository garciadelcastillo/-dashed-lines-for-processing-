/**
 * DASHED 2D PRIMITIVES
 * A showcase of simple 2D primitives drawn with dashed lines. 
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
  strokeWeight(5);
  strokeCap(SQUARE);
  textAlign(CENTER);

  dash = new DashedLines(this);
  
  // Dash patterns can be created with different combinations of dash-gap lengths
  //dash.pattern(30);
  //dash.pattern(30, 10);
  dash.pattern(30, 10, 15, 10);
  //float[] decreasingPattern = { 50, 10, 40, 10, 30, 10, 20, 10, 10, 10 };
  //dash.pattern(decreasingPattern);

  initializeNodes();
}

void draw() {
  background(255);
  renderNodes();

  // Dashed objects inherit Processing's style properties, including shape modes.
  fill(255, 0, 0, 100);
  rectMode(CORNERS);
  dash.rect(n[0].x, n[0].y, n[1].x, n[1].y);
  
  fill(0, 255, 0, 100);
  ellipseMode(CORNERS);
  dash.ellipse(n[2].x, n[2].y, n[3].x, n[3].y);
  
  fill(0, 0, 255, 100);
  dash.triangle(n[4].x, n[4].y, n[5].x, n[5].y, n[6].x, n[6].y);
  
  fill(255, 0, 255, 100);
  dash.quad(n[7].x, n[7].y, n[8].x, n[8].y, n[9].x, n[9].y, n[10].x, n[10].y);
  

  // Animate dashes with 'walking ants' effect 
  dash.offset(dist);
  dist += 1;

  pushStyle();
  fill(0);
  text("Drag nodes around", 0.5 * width, height - 25);
  popStyle();
}




void initializeNodes() {
  n = new Node[11];
  n[0] = new Node(1.0/8 * width, 1.0/8 * height, 5);
  n[1] = new Node(3.0/8 * width, 3.0/8 * height, 5);
  n[2] = new Node(5.0/8 * width, 1.0/8 * height, 5);
  n[3] = new Node(7.0/8 * width, 3.0/8 * height, 5);
  n[4] = new Node(2.0/8 * width, 5.0/8 * height, 5);
  n[5] = new Node(3.0/8 * width, 7.0/8 * height, 5);
  n[6] = new Node(1.0/8 * width, 7.0/8 * height, 5);
  n[7] = new Node(5.5/8 * width, 5.0/8 * height, 5);
  n[8] = new Node(7.0/8 * width, 5.0/8 * height, 5);
  n[9] = new Node(6.5/8 * width, 7.0/8 * height, 5);
  n[10] = new Node(5.0/8 * width, 7.0/8 * height, 5);
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