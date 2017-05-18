/**
 * DASHED SHAPES
 * From beginShape() to endShape(), and everything in between.
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
  noFill();
  strokeWeight(5);
  strokeCap(SQUARE);
  strokeJoin(BEVEL);
  textAlign(CENTER);

  dash = new DashedLines(this);
  dash.pattern(30, 10);

  initializeNodes();
}

void draw() {
  background(255);
  renderNodes();
  
  // Start the shape with the .beginShape() method
  dash.beginShape();
  // Add vertices like you would in Processing
  for (int i = 0; i < n.length; i++) {
    dash.vertex(n[i].x, n[i].y);
  }
  // Finish drawing the shape
  dash.endShape();
  
  //// Shapes accept all the same modes as Processing's native implementation:
  //fill(255, 0, 0, 100);
  //dash.beginShape(TRIANGLES);
  //for (int i = 0; i < n.length; i++) {
  //  dash.vertex(n[i].x, n[i].y);
  //}
  //dash.endShape(CLOSE);


  // Animate dashes with 'walking ants' effect 
  dash.offset(dist);
  dist += 1;

  pushStyle();
  fill(0);
  text("Drag nodes around", 0.5 * width, height - 25);
  popStyle();
}




void initializeNodes() {
  n = new Node[12];
  float dx = width / 7.0;
  for (int i = 0; i < 6; i++) {
    n[2 * i] = new Node(dx + i * dx, 0.25 * height, 5);
    n[2 * i + 1] = new Node(dx + i * dx, 0.75 * height, 5);
  }
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