
float DASH_LENGTH;
float DASH_SPACING;
float CIRCLE_EPSILON = 0.1;

void dash(float dashLength, float dashSpacing) {
  DASH_LENGTH = dashLength;
  DASH_SPACING = dashSpacing;
}

void dashLine(float x1, float y1, float x2, float y2) {
  PVector l = new PVector(x2 - x1, y2 - y1);
  PVector d = (new PVector(x2 - x1, y2 - y1)).setMag(DASH_LENGTH);
  PVector s = (new PVector(x2 - x1, y2 - y1)).setMag(DASH_SPACING);

  float dx = l.x;
  float dy = l.y;
  float ddx = d.x;
  float ddy = d.y;
  float sdx = s.x;
  float sdy = s.y;

  int spaceDashCount = abs(dx) > abs(dy) ? 
    int( dx / (ddx + sdx) ) : 
    int( dy / (ddy + sdy) );

  float x = x1, y = y1;

  // Draw full dash + spaces 
  for (int i = 0; i < spaceDashCount; i++) {
    line(x, y, x + ddx, y + ddy);
    x += ddx + sdx;
    y += ddy + sdy;
  }

  // Figure out how to end the line
  if (abs(ddx) < abs(x2 - x)) {
    line(x, y, x + ddx, y + ddy);
  } else {
    line(x, y, x2, y2);
  }
}

void dashRect(float a, float b, float c, float d) {
  int rectMode = getGraphics().rectMode;

  // From Processing's core
  float hradius, vradius;
  switch (rectMode) {
  case CORNERS:
    break;
  case CORNER:
    c += a; 
    d += b;
    break;
  case RADIUS:
    hradius = c;
    vradius = d;
    c = a + hradius;
    d = b + vradius;
    a -= hradius;
    b -= vradius;
    break;
  case CENTER:
    hradius = c / 2.0f;
    vradius = d / 2.0f;
    c = a + hradius;
    d = b + vradius;
    a -= hradius;
    b -= vradius;
  }

  if (a > c) {
    float temp = a; 
    a = c; 
    c = temp;
  }

  if (b > d) {
    float temp = b; 
    b = d; 
    d = temp;
  }

  // Draw the underlying fill props
  pushStyle();
  noStroke();
  quad(a, b, c, b, c, d, a, d);  // since we already did the calculations, quad is faster than rect()
  popStyle();

  // Draw rect lines (quick and dirty) 
  dashLine(a, b, c, b);
  dashLine(c, b, c, d);
  dashLine(c, d, a, d);
  dashLine(a, d, a, b);
}



void dashEllipse(float a, float b, float c, float d) {
  int ellipseMode = getGraphics().ellipseMode;
  
  // From Processing's core, CORNER-oriented vars
  float x = a;
  float y = b;
  float w = c;
  float h = d;

  if (ellipseMode == CORNERS) {
    w = c - a;
    h = d - b;
  } else if (ellipseMode == RADIUS) {
    x = a - c;
    y = b - d;
    w = c * 2;
    h = d * 2;
  } else if (ellipseMode == DIAMETER) {  // == CENTER
    x = a - c/2f;
    y = b - d/2f;
  }

  if (w < 0) {  // undo negative width
    x += w;
    w = -w;
  }

  if (h < 0) {  // undo negative height
    y += h;
    h = -h;
  }
  
  //// If almost a circle, save some cycles and draw one
  //if (abs(w - h) < CIRCLE_EPSILON) {
  //  dashCirc(x, y, w);
  //  return;
  //}
  
  
  // Turns out, ellipses are a little more complicated than circles!
  // There is no closed form equation to find the length (solution is a double elliptic integral), 
  // nor the arc length given an angle...
  // In this first test, one of Ramanujan's approximations to the length of the ellipse is used (16#49),
  // and then the length divided (unequally) into angle increments. 
  // (To be improved)
  float len = ellipseLength(0.5 * w, 0.5 * h);
  int spaceDashCount = int(len / (DASH_LENGTH + DASH_SPACING));
  float dang = TAU * DASH_LENGTH / len;
  float sang = TAU * DASH_SPACING / len;
  //println(a + " " + b + " " + c + " " + d);
  //println(x + " " + y + " " + w + " " + h);
  //println(len + " " + spaceDashCount + " " + dang + " " + sang);
  
  // Draw the fill part
  pushStyle();
  noStroke();
  ellipseMode(CORNER);  // all correct vars are already calculated, so why not use them...? :)
  ellipse(x, y, w, h);  
  //ellipse(a, b, c, d);  // rely on renderer implementation to do the job
  popStyle();

  // Draw dashes
  float ang = 0;
  pushStyle();
  noFill();
  // If using abcd there is something weird with the orientation of the angles
  // in the arcs, using this workaround while I figure it out.
  ellipseMode(CORNER);
  for (int i = 0; i < spaceDashCount; i++) {
    arc(x, y, w, h, ang, ang + dang, OPEN);
    ang += dang + sang;
  }
  
  // Last dash
  if (ang + dang <= TAU) {
    arc(x, y, w, h, ang, TAU, OPEN);
  } else {
    arc(x, y, w, h, ang, ang + dang, OPEN);
  }
  popStyle();
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



//// Drawing a dashed circle is WAY more optimal that doing an ellipse,
//// so added a case here.
//void dashCirc(float x, float y, float d) {
//  float len = PI * d;

//  int spaceDashCount = int( len / (DASH_LENGTH + DASH_SPACING) );

//  float dang = TAU * DASH_LENGTH / len;
//  float sang = TAU * DASH_SPACING / len;

//  // Draw the fill part
//  pushStyle();
//  noStroke();
//  ellipse(x, y, d, d);
//  popStyle();

//  // Draw dashes
//  float ang = 0;
//  pushStyle();
//  noFill();
//  for (int i = 0; i < spaceDashCount; i++) {
//    arc(x, y, d, d, ang, ang + dang, OPEN);
//    ang += dang + sang;
//  }

//  // Figure out how to end...
//  if (ang + dang <= TAU) {
//    arc(x, y, d, d, ang, TAU, OPEN);
//  } else {
//    arc(x, y, d, d, ang, ang + dang, OPEN);
//  }
//  //arc(x, y, d, d, ang, ang + dang <= TAU ? TAU : ang + dang, OPEN);
  
//  popStyle();
//}