
float DASH_LENGTH;
float DASH_SPACING;

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
  quad(a, b,  c, b,  c, d,  a, d);  // since we already did the calculations, quad is faster than rect()
  popStyle();

  // Draw rect lines (quick and dirty) 
  dashLine(a, b, c, b);
  dashLine(c, b, c, d);
  dashLine(c, d, a, d);
  dashLine(a, d, a, b);
}

// Drawing a dashed circle is WAY more optimal that doing an ellipse,
// so added a case here.
void dashCirc(float x, float y, float r) {
  float len = TAU * r;
  
  int spaceDashCount = int( len / (DASH_LENGTH + DASH_SPACING) );
  
  float dang = TAU * DASH_LENGTH / len;
  float sang = TAU * DASH_SPACING / len;
  
  // Draw the fill part
  pushStyle();
  noStroke();
  ellipse(x, y, 2 * r, 2 * r);
  popStyle();
  
  // Draw dashes
  float ang = 0;
  pushStyle();
  noFill();
  //ellipseMode(CENTER);  // to be improved
  for (int i = 0; i < spaceDashCount; i++) {
    arc(x, y, 2 * r, 2 * r, ang, ang + dang, OPEN);
    ang += dang + sang;    
  }
  popStyle();
   
  // Figure out how to end...
  if (ang + dang <= TAU) {
    arc(x, y, 2 * r, 2 * r, ang, TAU, OPEN);
  } else {
    arc(x, y, 2 * r, 2 * r, ang, ang + dang, OPEN);
  }
  
  //// Figure out how to end the line
  //if (abs(ddx) < abs(x2 - x)) {
  //  line(x, y, x + ddx, y + ddy);
  //} else {
  //  line(x, y, x2, y2);
  //}
  
  
}