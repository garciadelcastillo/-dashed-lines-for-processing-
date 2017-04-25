

Node n1, n2;


void setup() {
  size(800, 600);
  
  n1 = new Node(100, 100, 5);
  n2 = new Node(200, 200, 5);
  
  dash(15, 15);  // sets dash size and spacing in pixels
}

void draw() {
  background(255);
  
  n1.render();
  n2.render();
  
  stroke(125, 50);
  strokeWeight(1);
  line(n1.x, n1.y, n2.x, n2.y);
  
  stroke(0);
  strokeWeight(1);
  dashLine(n1.x, n1.y, n2.x, n2.y);
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