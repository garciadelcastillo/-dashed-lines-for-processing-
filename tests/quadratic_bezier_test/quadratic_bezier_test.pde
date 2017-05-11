
Node n1, n2, n3;

void setup() {
  size(800, 600);
  strokeCap(SQUARE);



  n1 = new Node(width / 2, height / 2, 5);
  n2 = new Node(3 * width / 4, 3 * height / 4, 5);
  n3 = new Node(width / 2 + 200, height / 2, 5);
}

void draw() {
  background(255);

  n1.render();
  n2.render();
  n3.render();

  fill(255, 0, 0, 50);
  //stroke(0);
  noStroke();
  strokeWeight(1);
  beginShape();
  vertex(n1.x, n1.y);
  quadraticVertex(n2.x, n2.y, n3.x, n3.y);
  endShape();

  noFill();
  int segments = 20;
  float dt = 1 / (float) segments;
  for (int i = 0; i < segments; i+=2) {
    float a = i * dt;
    float b = i * dt + dt;
    PVector[] pts = splitQuadraticBezier(a, b, n1, n2, n3);
    stroke(0);
    strokeWeight(1);
    //for (int j = 0; j < pts.length; j++) {
    //  ellipse(pts[j].x, pts[j].y, 5, 5);
    //}
    strokeWeight(5);
    stroke(0, 63);
    beginShape();
    vertex(pts[0].x, pts[0].y);
    quadraticVertex(pts[1].x, pts[1].y, pts[2].x, pts[2].y);
    endShape();
  }

  println(frameRate);
}



void mousePressed() {
  if (n1.inside(mouseX, mouseY)) {
    n1.dragged = true;
  } else if (n2.inside(mouseX, mouseY)) {
    n2.dragged = true;
  } else if (n3.inside(mouseX, mouseY)) {
    n3.dragged = true;
  }
}

void mouseReleased() {
  n1.dragged = false;
  n2.dragged = false;
  n3.dragged = false;
}


PVector pointOnQuadraticBezier(float t, Node start, Node controlPoint, Node end) {
  float t2 = t * t;
  float mt = 1 - t;
  float mt2 = mt * mt;

  float x = start.x * mt2 + 2 * controlPoint.x * mt * t + end.x * t2;
  float y = start.y * mt2 + 2 * controlPoint.y * mt * t + end.y * t2;

  return new PVector(x, y);
}

PVector[] splitQuadraticBezier(float a, float b, Node start, Node controlPoint, Node end) {

  if (a > b) {
    float tmp = a;
    a = b;
    b = tmp;
  }

  PVector[] pts = new PVector[3];
  for (int i = 0; i < pts.length; i++) {
    pts[i] = new PVector();
  }

  float d = b - a;
  float d2 = d * d;
  float a2 = a * a;
  float ma = a - 1;
  float ma2 = ma * ma;

  pts[0].x = ma2 * start.x - 2 * a * ma * controlPoint.x + a2 * end.x;
  pts[0].y = ma2 * start.y - 2 * a * ma * controlPoint.y + a2 * end.y;

  pts[1].x = (ma2 + d * ma) * start.x
    + (-2 * a * ma + d - 2 * d * a) * controlPoint.x
    + (a2 + a * d) * end.x;
  pts[1].y = (ma2 + d * ma) * start.y
    + (-2 * a * ma + d - 2 * d * a) * controlPoint.y
    + (a2 + a * d) * end.y;

  pts[2].x = (ma2 + 2 * d * ma + d2) * start.x 
    + (-2 * a * ma + 2 * d - 4 * d * a - 2 * d2) * controlPoint.x
    + (a2 + 2 * a * d + d2) * end.x;
  pts[2].y = (ma2 + 2 * d * ma + d2) * start.y 
    + (-2 * a * ma + 2 * d - 4 * d * a - 2 * d2) * controlPoint.y
    + (a2 + 2 * a * d + d2) * end.y;

  return pts;
}