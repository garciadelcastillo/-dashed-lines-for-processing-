Node n1, n2, n3, n4;

float angle = 0;

void setup() {
  size(800, 600);
  strokeCap(SQUARE);


  fill(255, 0, 0, 50);
  strokeWeight(1);

  n1 = new Node(width / 2, height / 2, 5);
  n2 = new Node(width / 2 + 200, height / 2, 5);
  n3 = new Node(width / 2, height / 2 + 100, 5);
  n4 = new Node(width / 2 + 200, height / 2 + 100, 5);

}

void draw() {
  background(255);

  n1.render();
  n2.render();
  n3.render();
  n4.render();

  float a = n2.x - n1.x;
  float b = n3.y - n1.y;
  pushStyle();
  ellipseMode(RADIUS);
  ellipse(n1.x, n1.y, a, b);
  popStyle();
  
  PVector p = pointOnEllipseAtAngle(a, b, angle);
  ellipse(n1.x + p.x, n1.y + p.y, 5, 5);
  angle += 0.01;
  
}