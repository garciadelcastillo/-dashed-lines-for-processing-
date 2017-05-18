/**
 * DASHED LINES
 * Basic dashed lines, and the things you can do with them. 
 * 
 * Dashed Lines for Processing, by Jose Luis Garcia del Castillo 2017
 * https://github.com/garciadelcastillo/-dashed-lines-for-processing- 
 */

import garciadelcastillo.dashedlines.*;

DashedLines dash;
Node n1, n2;  // some draggable nodes for added interactivity
float dist = 0;

void setup() {
  size(800, 600);
  noFill();
  strokeWeight(5);
  strokeCap(SQUARE);
  textAlign(CENTER);

  dash = new DashedLines(this);  
  dash.pattern(20, 10);

  n1 = new Node(0.25 * width, 0.5 * height, 5);
  n2 = new Node(0.75 * width, 0.5 * height, 5);
}

void draw() {
  background(255);

  n1.render();
  n2.render();

  dash.line(n1.x, n1.y, n2.x, n2.y);

  // Animate dashes with 'walking ants' effect 
  dash.offset(dist);
  dist += 1;

  pushStyle();
  fill(0);
  text("Drag nodes around", 0.5 * width, height - 25);
  popStyle();
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
  if (n1.inside(mouseX, mouseY)) {
    n1.dragged = true;
  } else if (n2.inside(mouseX, mouseY)) {
    n2.dragged = true;
  }
}

void mouseReleased() {
  n1.dragged = false;
  n2.dragged = false;
}