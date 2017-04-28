

private float CIRCLE_EPSILON = 0.1;
private float[] dashPattern = {10, 10};

void dash(float d1) {
  dashPattern = new float[2];
  dashPattern[0] = d1;
  dashPattern[1] = d1;
}

void dash(float d1, float d2) {
  dashPattern = new float[2];
  dashPattern[0] = d1;
  dashPattern[1] = d2;
}

void dash(float d1, float d2, float d3, float d4) {
  dashPattern = new float[4];
  dashPattern[0] = d1;
  dashPattern[1] = d2;
  dashPattern[2] = d3;
  dashPattern[3] = d4;
}

void dash(float[] ds) {
  if (ds.length % 2 == 1) {
    throw new RuntimeException("Please provide an even number of dash-gap lengths on dash(float[])");
  }
  dashPattern = ds;
}



void dashLine(float x1, float y1, float x2, float y2) {
  // Compute theta parameters for start-ends of dashes and gaps
  FloatList ts = new FloatList();  // TODO: precompute the size of the t array and create it as an array directly
  int id = 0;
  float run = 0;
  float t = 0;
  float len = dist(x1, y1, x2, y2);

  while (run < len) {
    t = run / len;
    ts.append(t);
    run += dashPattern[id % dashPattern.length];
    id++;
  }

  // If last t was the startpoint of a dash, close it at the end of the line
  if (id % 2 == 1) {
    ts.append(1);
  }

  // DEV
  if (ts.size() % 2 == 1) {
    throw new RuntimeException("t array is size " + ts.size());
  }

  float[] tsA = ts.array();  // TODO: improve the list-array situation

  // Draw dashes
  pushStyle();
  float dx = x2 - x1;
  float dy = y2 - y1;
  for (int i = 0; i < tsA.length; i += 2) {
    line(x1 + tsA[i] * dx, y1 + tsA[i] * dy, x1 + tsA[i + 1] * dx, y1 + tsA[i + 1] * dy);
  }
  popStyle();
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
  dashQuad(a, b, c, b, c, d, a, d);
}

void dashQuad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
  pushStyle();
  noStroke();
  quad(x1, y1, x2, y2, x3, y3, x4, y4);
  popStyle();

  dashLine(x1, y1, x2, y2);
  dashLine(x2, y2, x3, y3);
  dashLine(x3, y3, x4, y4);
  dashLine(x4, y4, x1, y1);
}

void dashTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
  pushStyle();
  noStroke();
  triangle(x1, y1, x2, y2, x3, y3);
  popStyle();

  dashLine(x1, y1, x2, y2);
  dashLine(x2, y2, x3, y3);
  dashLine(x3, y3, x1, y1);
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
  float w2 = 0.5 * w, h2 = 0.5 * h;

  // Compute theta parameters for start-ends of dashes and gaps
  FloatList ts = new FloatList();  // TODO: precompute the size of the t array and create it as an array directly
  int id = 0;
  float run = 0;
  float t = 0;
  float dt = 0.01;
  float samples = Math.round(TAU / dt);
  float len = ellipseCircumference(w2, h2, 0, dt);
  float nextL = 0;

  //println("start: " + millis());
  for (int i = 0; i < samples; i++) {
    run += ellipseArcDifferential(w2, h2, t, dt);
    if ((int) run >= nextL) {
      ts.append(t);
      nextL += dashPattern[id % dashPattern.length];
      id++;
    }
    t += dt;
  }
  //println("end: " + millis());
  float[] tsA = ts.array();  // see TODO above

  // Draw the fill part
  pushStyle();
  noStroke();
  ellipseMode(CORNER);  // all correct vars are already calculated, so why not use them...? :)
  ellipse(x, y, w, h);  
  popStyle();

  // Draw dashes
  pushStyle();
  noFill();
  ellipseMode(CORNER);
  for (int i = 0; i < tsA.length; i += 2) {
    if (i == tsA.length - 1) {
      arc(x, y, w, h, tsA[i], TAU);  // TODO: does this account for 2+ dash/gaps?
    } else {
      arc(x, y, w, h, tsA[i], tsA[i+1]);
    }
  }
  popStyle();
}

// Create a dashed arc using Processing same function signature
// (note that start/stop here refer to the THETA parameter, NOT THE POLAR ANGLE)
void dashArc(float a, float b, float c, float d, float start, float stop) {
  dashArc(a, b, c, d, start, stop, 0);
}


// Create a dashed arc using Processing same function signature
// (note that start/stop here refer to the THETA parameter, NOT THE POLAR ANGLE)
void dashArc(float a, float b, float c, float d, float start, float stop, int mode) {
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
  float w2 = 0.5 * w, h2 = 0.5 * h;

  // make sure the loop will exit before starting while
  if (!Float.isInfinite(start) && !Float.isInfinite(stop)) {
    // ignore equal and degenerate cases
    if (stop > start) {
      // make sure that we're starting at a useful point
      while (start < 0) {
        start += TWO_PI;
        stop += TWO_PI;
      }

      if (stop - start > TWO_PI) {
        // don't change start, it is visible in PIE mode
        stop = start + TWO_PI;
      }

      // TODO: implement modes: CHORD, PIE

      // Compute theta parameters for start-ends of dashes and gaps
      FloatList ts = new FloatList();  // TODO: precompute the size of the t array and create it as an array directly
      int id = 0;
      float run = 0;
      float t = start;
      float dt = 0.01;
      float samples = Math.round((stop - start) / dt);
      float len = ellipseCircumference(w2, h2, 0, dt);
      float nextL = 0;

      //println("start: " + millis());
      for (int i = 0; i < samples; i++) {
        run += ellipseArcDifferential(w2, h2, t, dt);
        if ((int) run >= nextL) {
          ts.append(t);
          nextL += dashPattern[id % dashPattern.length];
          id++;
        }
        t += dt;
      }
      //println("end: " + millis());
      float[] tsA = ts.array();  // see TODO above

      // Draw the fill part
      pushStyle();
      noStroke();
      ellipseMode(CORNER);  // all correct vars are already calculated, so why not use them...? :)
      arc(x, y, w, h, start, stop, mode);  
      popStyle();


      // Draw dashes
      pushStyle();
      noFill();
      ellipseMode(CORNER);

      // If PIE mode, draw center-start line
      if (mode == PIE) {
        dashLine(x + w2, y + h2, x + w2 + w2 * cos(start), y + h2 + h2 * sin(start));
      }

      // Arc dashes
      for (int i = 0; i < tsA.length; i += 2) {
        if (i == tsA.length - 1) {
          arc(x, y, w, h, tsA[i], stop);  // TODO: does this account for 2+ dash/gaps?
        } else {
          arc(x, y, w, h, tsA[i], tsA[i+1]);
        }
      }

      // If PIE, draw end-center line,
      // else if CHORD draw end-start line.
      if (mode == PIE) {
        dashLine(x + w2 + w2 * cos(stop), y + h2 + h2 * sin(stop), x + w2, y + h2);
      } else if (mode == CHORD) {
        dashLine(x + w2 + w2 * cos(stop), y + h2 + h2 * sin(stop), x + w2 + w2 * cos(start), y + h2 + h2 * sin(start));
      }

      popStyle();
    }
  }
}


// Create a dashed arc using Processing same function signature,
// however using start/stop as POLAR ANGLES, not THETA parameters.
// This is not consistent with Processing's implementation, 
// but just feels right geometrically... ;)
void dashArcPolar(float a, float b, float c, float d, float start, float stop, int mode) {

  int ellipseMode = getGraphics().ellipseMode;
  float w = c;
  float h = d;
  if (ellipseMode == CORNERS) {
    w = c - a;
    h = d - b;
  } else if (ellipseMode == RADIUS) {
    w = c * 2;
    h = d * 2;
  } 
  if (w < 0) {  // undo negative width
    w = -w;
  }
  if (h < 0) {  // undo negative height
    h = -h;
  }
  float w2 = 0.5 * w;
  float h2 = 0.5 * h;

  float thetaStart = ellipsePolarToTheta(w2, h2, start);
  float thetaStop = ellipsePolarToTheta(w2, h2, stop);
  dashArc(a, b, c, d, thetaStart, thetaStop, mode);
}