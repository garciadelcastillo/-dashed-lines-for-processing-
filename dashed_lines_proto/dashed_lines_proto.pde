

Node n1, n2;


void setup() {
  size(800, 600);
  strokeCap(SQUARE);
  
  fill(255, 0, 0, 50);
  strokeWeight(3);

  n1 = new Node(100, 100, 5);
  n2 = new Node(200, 200, 5);

  dash(20, 10);  // sets dash size and spacing in pixels
}

void draw() {
  background(255);

  n1.render();
  n2.render();

  //drawDashedLine();
  //drawDashedRectangle();
  //drawDashedCircle();
  drawDashedEllipse();
  
  
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


///////////////////////////////////////
void drawDashedLine() {
  stroke(0);
  strokeWeight(5);
  dashLine(n1.x, n1.y, n2.x, n2.y);
}

void drawDashedRectangle() {
  rectMode(CORNERS);
  dashRect(n1.x, n1.y, n2.x, n2.y);
}

//void drawDashedCircle() {
//  pushStyle();
//  ellipseMode(CORNERS);
//  float r = dist(n1.x, n1.y, n2.x, n2.y);
//  dashCirc(n1.x, n1.y, 2 * r);
//  popStyle();
//}

void drawDashedEllipse() {
  pushStyle();
  ellipseMode(RADIUS);
  //float r = dist(n1.x, n1.y, n2.x, n2.y);
  dashEllipse(n1.x, n1.y, n2.x - n1.x, n2.y - n1.y);
  popStyle();
}




/*
  TODO:
 - At some point, implement offset or something so that animations like this are possible:
 https://www.youtube.com/watch?v=8uZgU3f8p9A
 
 
 */