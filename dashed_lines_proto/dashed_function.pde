
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
  
  int spaceDashCount = int( dx / (ddx + sdx) );
  
  float x = x1, y = y1;
  for (int i = 0; i < spaceDashCount; i++) {
    line(x, y, x + ddx, y + ddy);
    x += ddx + sdx;
    y += ddy + sdy;
  }
  
  if (abs(ddx) < abs(x2 - x)) {
    line(x, y, x + ddx, y + ddy);
  } else {
    line(x, y, x2, y2);
  }
  
}