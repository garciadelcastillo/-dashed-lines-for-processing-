Node n1, n2, n3, n4;

float angle = 0;

void setup() {
  size(800, 600);
  strokeCap(SQUARE);


  fill(255, 0, 0, 50);
  strokeWeight(4);

  n1 = new Node(width / 2, height / 2, 5);
  n2 = new Node(width / 2 + 200, height / 2, 5);
  n3 = new Node(width / 2, height / 2 + 100, 5);
  n4 = new Node(width / 2 + 200, height / 2 + 100, 5);

  println(ellipseCircumference(200, 100));
  println(ellipseCircumference(200, 100, 1));
  println(ellipseArc(200, 100, 0.0 * PI, 1.0 * PI, 0.05));
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
  fill(255, 0, 0, 50);
  ellipseMode(RADIUS);
  //ellipse(n1.x, n1.y, a, b);
  popStyle();

  //PVector p = pointOnEllipseAtAngle(a, b, angle);
  //fill(0, 255, 0);
  //ellipse(n1.x + p.x, n1.y + p.y, 5, 5);

  //PVector pt = pointOnEllipseAtParameter(a, b, angle);
  //fill(0, 0, 255);
  //ellipse(n1.x + pt.x, n1.y + pt.y, 5, 5);

  //line(n1.x, n1.y, n1.x + 200 * cos(angle), n1.y + 200 * sin(angle));
  //angle += 0.01;

  //fill(0);
  //text(degrees(angle)%360, 10, 10);

  //float[] ts = divideEllipse(a, b, 64, 0.001);
  //for (float t : ts) {
  //  PVector p = pointOnEllipseAtParameter(a, b, t);
  //  ellipse(n1.x + p.x, n1.y + p.y, 5, 5);
  //}
  
  dashEllipse(n1.x, n1.y, a, b, 0.01);
  println(a + " " + b);
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