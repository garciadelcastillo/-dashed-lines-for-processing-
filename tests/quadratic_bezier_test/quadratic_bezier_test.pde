
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
    //stroke(0);
    //strokeWeight(1);
    //for (int j = 0; j < pts.length; j++) {
    //  ellipse(pts[j].x, pts[j].y, 5, 5);
    //}
    strokeWeight(5);
    stroke(0);
    beginShape();
    vertex(pts[0].x, pts[0].y);
    quadraticVertex(pts[1].x, pts[1].y, pts[2].x, pts[2].y);
    endShape();
  }

//  PVector[] pts = splitQuadraticBezier(0.1, 0.2, n1, n2, n3);
//  noFill();
//  strokeWeight(1);
//  stroke(0);
//  for (int i = 0; i < pts.length; i++) {
//    ellipse(pts[i].x, pts[i].y, 5, 5);
//  }
//  strokeWeight(2);
//  stroke(0);
//  beginShape();
//  vertex(pts[0].x, pts[0].y);
//  quadraticVertex(pts[1].x, pts[1].y, pts[2].x, pts[2].y);
//  endShape();


  //println(frameRate);
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
  
  // LONG FORM
  //float d = b - a;
  //float d2 = d * d;
  //float a2 = a * a;
  //float ma = a - 1;
  //float ma2 = ma * ma;

  //pts[0].x = ma2 * start.x - 2 * a * ma * controlPoint.x + a2 * end.x;
  //pts[0].y = ma2 * start.y - 2 * a * ma * controlPoint.y + a2 * end.y;

  //pts[1].x = (ma2 + d * ma) * start.x
  //  + (-2 * a * ma + d - 2 * d * a) * controlPoint.x
  //  + (a2 + a * d) * end.x;
  //pts[1].y = (ma2 + d * ma) * start.y
  //  + (-2 * a * ma + d - 2 * d * a) * controlPoint.y
  //  + (a2 + a * d) * end.y;

  //pts[2].x = (ma2 + 2 * d * ma + d2) * start.x 
  //  + (-2 * a * ma + 2 * d - 4 * d * a - 2 * d2) * controlPoint.x
  //  + (a2 + 2 * a * d + d2) * end.x;
  //pts[2].y = (ma2 + 2 * d * ma + d2) * start.y 
  //  + (-2 * a * ma + 2 * d - 4 * d * a - 2 * d2) * controlPoint.y
  //  + (a2 + 2 * a * d + d2) * end.y;

  // SIMPLER
  //float a2 = a * a;
  //float b2 = b * b;
  //float ab = a * b;
  //float ma = a - 1;

  //pts[0].x = ma * ma * start.x - 2 * a * ma * controlPoint.x + a2 * end.x;
  //pts[0].y = ma * ma * start.y - 2 * a * ma * controlPoint.y + a2 * end.y;

  //pts[1].x = (ab - a - b + 1) * start.x 
  //  + (-2 * ab + a + b) * controlPoint.x 
  //  + ab * end.x;
  //pts[1].y = (ab - a - b + 1) * start.y 
  //  + (-2 * ab + a + b) * controlPoint.y 
  //  + ab * end.y;

  //pts[2].x = (b-1)*(b-1) * start.x
  //  + (2 * b - 2 * b2) * controlPoint.x
  //  + b2 * end.x;
  //pts[2].y = (b-1)*(b-1) * start.y
  //  + (2 * b - 2 * b2) * controlPoint.y
  //  + b2 * end.y;

  // SIMPLEST ;)
  float a2 = a * a;
  float b2 = b * b;
  float ma = a - 1;
  float mb = b - 1;
  float ab = a * b;
  
  pts[0].x = ma * ma * start.x - 2 * a * ma * controlPoint.x + a2 * end.x;
  pts[0].y = ma * ma * start.y - 2 * a * ma * controlPoint.y + a2 * end.y;
  
  pts[1].x = ma * mb * start.x + (a + b - 2 * ab) * controlPoint.x + ab * end.x;
  pts[1].y = ma * mb * start.y + (a + b - 2 * ab) * controlPoint.y + ab * end.y;
  
  pts[2].x = mb * mb * start.x - 2 * b * mb * controlPoint.x + b2 * end.x;
  pts[2].y = mb * mb * start.y - 2 * b * mb * controlPoint.y + b2 * end.y;

  return pts; 
}