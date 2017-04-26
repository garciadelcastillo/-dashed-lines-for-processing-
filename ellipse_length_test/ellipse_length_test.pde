
Node n1, n2, n3, n4;
float t = 0;

void setup() {
  size(800, 600);
  strokeCap(SQUARE);


  fill(255, 0, 0, 50);
  strokeWeight(1);

  n1 = new Node(width / 2, height / 2, 5);
  n2 = new Node(width / 2 + 200, height / 2, 5);
  n3 = new Node(width / 2, height / 2 + 100, 5);
  n4 = new Node(width / 2 + 200, height / 2 + 100, 5);
  
  println(TAU * 100);
  println(ellipseLength(3, 2));
  println(ellipseLengthE(3, 2));
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

  float x = a * cos(t);
  float y = b * sin(t);
  ellipse(n1.x + x, n1.y + y, 5, 5);
  t += 0.01;
  if (t > TAU) {
    t = 0;
  }
    
  //float alpha = atan(tan(t) * b / a);
  //float alpha2 = atan2(y - n1.y, x - n1.x); 
  ////arc(n1.x, n1.y, 2 * a, 2 * b, 0, alpha);
  //arc(n1.x, n1.y, 2 * a, 2 * b, 0, t);  // IT'S NOT THE FREAKING ANGLE, IT'S THE FREAKING PARAMETER!!@#!!
  //line(n1.x, n1.y, n1.x + 200*cos(alpha), n1.y + 200*sin(alpha));

  //for (int i = 0; i <= 90; i += 10) {
  //  line(n1.x, n1.y, n1.x + 200 * cos(radians(i)), n1.y + 200 * sin(radians(i)));
  //  float T = atan(tan(radians(i)) * a / b);
  //  ellipse(n1.x + a * cos(T), n1.y + b * sin(T), 5, 5);
  //}

  //pushStyle();
  //fill(0);
  //text("t: " + t, 10, 10);
  //text("alpha: " + degrees(alpha), 10, 25);
  //text("alpha2: " + degrees(alpha2), 10, 40);
  //popStyle();
  
  for (float phi = 0; phi <= PI; phi += PI / 18) {
    ellipse(n1.x + a * cos(phi), n1.y + b * sin(phi), 5, 5);
  }
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
    fill(127, 50);
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


// Find the circunference length of an ellipse using Ramanujan's approximation.
// a & b are the lengths of the semiaxes.
// Ramanujan, S. "Modular Equations and Approximations to pi." Quart. J. Pure. Appl. Math. 45, 350-372, 1913-1914. Section 16, Eq. 49
// https://books.google.com/books?id=oSioAM4wORMC&pg=PA39#v=onepage&q&f=false
float ellipseLength(float a, float b) {
  //float k = sqrt(1 - b * b / (a * a));  // ellipse's eccentricity: http://mathworld.wolfram.com/Eccentricity.html
  //float eps = a * pow(k, 12) / 1048576;
  //println("eps: " + eps);  // the 

  return PI * ( 3 * (a + b) - sqrt((a + 3 * b) * (3 * a + b)) );
}

// https://math.stackexchange.com/a/73504/440507
// Gives same result as Ramanujan for large numbers 
// --> It's actually worse for smaller!!! Use (3, 2) and compare results to post! lol
float ellipseLengthE(float a, float b) {
  float f = 1, s, v = 0.5 * (1 + b/a), w;
  w = (1 - (b/a)*(b/a))/(a * v);
  s = v * v;
  int it = 0;
  while (true) {
    v = 0.5 * (v + sqrt((v - w) * (v + w)));
    w = (0.5 * w) * (0.5 * w) / v;
    f *= 2;
    s -= f * w * w;
    if (abs(w) < 0.00000001 || it > 1000) {
      println("it: " + it);
      break;
    }
    it++;
  }
  return 2 * a * PI * s / v;
}