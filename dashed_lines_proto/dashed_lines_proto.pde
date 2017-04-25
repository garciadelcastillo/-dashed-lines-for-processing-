

Node n1, n2, n3, n4;


void setup() {
  size(800, 600);
  strokeCap(SQUARE);
  
  fill(255, 0, 0, 50);
  strokeWeight(3);

  n1 = new Node(100, 100, 5);
  n2 = new Node(200, 200, 5);
  n3 = new Node(300, 100, 5);
  n4 = new Node(200, 50, 4);

  dash(20, 10);  // sets dash size and spacing in pixels
}

void draw() {
  background(255);

  n1.render();
  n2.render();
  n3.render();
  n4.render();

  //drawDashedLine();
  //drawDashedRectangle();
  //drawDashedEllipse();
  //drawDashedQuad();
  drawDashedTriangle();
  
  
}



void mousePressed() {
  if (n1.inside(mouseX, mouseY)) {
    n1.dragged = true;
  } else if (n2.inside(mouseX, mouseY)) {
    n2.dragged = true;
  } else if (n3.inside(mouseX, mouseY)) {
    n3.dragged = true;
  } else if (n4.inside(mouseX, mouseY)) {
    n4.dragged = true;
  }
}

void mouseReleased() {
  n1.dragged = false;
  n2.dragged = false;
  n3.dragged = false;
  n4.dragged = false;
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

void drawDashedEllipse() {
  pushStyle();
  ellipseMode(CORNERS);
  dashEllipse(n1.x, n1.y, n2.x - n1.x, n2.y - n1.y);
  popStyle();
}

void drawDashedQuad() {
  //noFill();
  dashQuad(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);
}

void drawDashedTriangle() {
  dashTriangle(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y);
}



/*
  TODO:
 - At some point, implement offset or something so that animations like this are possible:
 https://www.youtube.com/watch?v=8uZgU3f8p9A
 
 
 */