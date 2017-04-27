




// Given a & b as the major and minor semi-axes, 
// and t (0, TAU) as the parameter along the ellipse (not the polar angle of the point),
// returns the corresponding PVector point on the ellipse.
private PVector pointOnEllipseAtParameter(float a, float b, float t) {
  float x = a * cos(t);
  float y = b * sin(t);
  return new PVector(x, y);
}

// Given a & b as the major and minor semi-axes, 
// and alpha (0, TAU) as the polar angle of the point,
// returns the corresponding PVector point on the ellipse.
private PVector pointOnEllipseAtAngle(float a, float b, float alpha) {
  // Constrain angle
  alpha %= TAU;

  // Convert parameter to polar angle
  float t = atan(tan(alpha) * a / b);

  // Adjust atan limits to map t to (0, TAU)
  if (alpha > HALF_PI && alpha <= 1.5 * PI) {
    t += PI;
  } else if (alpha > 1.5 * PI) {
    t = TAU + t;
  }

  return pointOnEllipseAtParameter(a, b, t);
}


// Given a & b as the major and minor semi-axes, 
// return a fast approximation to the circumference of the ellipse 
private float ellipseCircumference(float a, float b) {
  // By default, just rely on Ramanujan's implementation
  return ellipseCircumference(a, b, 0);
}

// Given a & b as the major and minor semi-axes, 
// return an approximation to the circumference of the ellipse.
// Use mode 0 for Ramanujan's fast approximation, or for very low
// b/a ratios use mode 1 for a more precise (albeit) expensive recursive approximation.
private float ellipseCircumference(float a, float b, int mode) {
  // Turns out the exact solution has no closed form, and can only be obtained solving
  // for the complete elliptical integral of the second kind:
  // C = 4 a E(1 - b^2 / a^2);
  // For example, for a = 200 and b = 100:
  // C = 800 E(3/4) ≈ 968.844822054... 
  // See: http://www.wolframalpha.com/input/?i=4+200+EllipticE%5B1+-+100%5E2+%2F200%5E2%5D
  // Or better yet: https://www.wolframalpha.com/input/?i=integrate+sqrt(a%5E2*sin%5E2(t)+%2B+b%5E2*cos%5E2(t))+from+0+to+2*pi+where+a+%3D+200+and+b+%3D+100


  // In this method, we will default to Ramanujan's approximation 
  // (quite good for common Processing ellipse size ranges, with precision errors
  // ranging from the 6th to the 4th significant digit of the mantissa depending on 
  // the 1 > b/a > 0 ratio) or rely on an expensive yet preciser recursive approximation:  
  switch(mode) {
    // Recursive approximation
    // https://math.stackexchange.com/a/73504/440507
  case 1:
    float f = 1, s, v = 0.5 * (1 + b/a), w;
    w = (1 - (b/a)*(b/a))/(4 * v);
    s = v * v;
    //int it = 0;
    while (true) {
      v = 0.5 * (v + sqrt((v - w) * (v + w)));
      w = (0.5 * w) * (0.5 * w) / v;
      f *= 2;
      s -= f * w * w;
      //if (abs(w) < EPSILON || it > 100) {
      if (abs(w) < EPSILON) {
        //println("it: " + it);
        break;
      }
      //it++;
    }
    return 2 * a * PI * s / v;
    
    // Internal recursive differential approximation
  case 2: 
    return ellipseArc(a, b, 0, TAU, 0.1);

    // Ramanujan's approximation
    // Ramanujan, S. "Modular Equations and Approximations to pi." Quart. J. Pure. Appl. Math. 45, 350-372, 1913-1914. Section 16, Eq. 49
    // https://books.google.com/books?id=oSioAM4wORMC&pg=PA39#v=onepage&q&f=false
  case 0:
  default:
    return PI * ( 3 * (a + b) - sqrt((a + 3 * b) * (3 * a + b)) );
  }
}


void dashEllipse(float x, float y, float a, float b, float precision) {
  // Placeholder for fetching these from style state
  float[] dashLengths = {20, 20, 5, 20};  // dash, space, dash, space...
  
  FloatList ts = new FloatList();
  int id = 0;
  float run = 0;
  float t = 0;
  float dt = precision;
  float samples = Math.round(TAU / dt);
  float len = ellipseCircumference(a, b, 0);
  float nextL = 0;
  
  println("start: " + millis());
  for (int i = 0; i < samples; i++) {
    run += ellipseArcDifferential(a, b, t, dt);
    if ((int) run >= nextL) {
      ts.append(t);
      nextL += dashLengths[id % dashLengths.length];
      id++;
    }
    t += dt;
  }
  println("end: " + millis());
  
  float[] tsA = ts.array();
  //for (float T : tsA) {
  //  PVector p = pointOnEllipseAtParameter(a, b, T);
  //  ellipse(x + p.x, y + p.y, 5, 5);
  //}
  
  noFill();
  for (int i = 0; i < tsA.length; i += 2) {
    if (i == tsA.length - 1) {
      arc(x, y, 2 * a, 2 * b, tsA[i], TAU);
    } else {
      arc(x, y, 2 * a, 2 * b, tsA[i], tsA[i+1]);
    }
  }
  
}




// TEST
// Returns the parameters resulting of diving the ellipse in n equal-length arcs. 
private float[] divideEllipse(float a, float b, int n, float precision) {
  // Housekeeping
  if (a < 0) a = -a;
  if (b < 0) b = -b;
  
  float[] ts = new float[n];
  int id = 0;
  float run = 0;
  float t = 0;
  float dt = precision;
  float samples = Math.round(TAU / dt);
  float dn = 0;
  float len = ellipseCircumference(a, b, 0);
  
  //println("start: " + millis());
  ts[id] = 0;
  id++;
  for (int i = 0; i < samples; i++) {
    t += dt;
    dn = n * run / len;
    if ((int) dn >= id) {
      ts[id] = t;
      id++;
      if (id >= n) {
        break;
      }
    }
    run += ellipseArcDifferential(a, b, t, dt);
  }
  //println("end: " + millis());

  return ts;
}


private float ellipseArc(float a, float b, float startT, float endT, float precision) {
  // Housekeeping
  if (a < 0) a = -a;
  if (b < 0) b = -b;
  if (startT > endT) {
    float tmp = startT;
    startT = endT;
    endT = tmp;
  }
  
  double len = 0;
  int samples = Math.round(TAU / precision);
  float dt = (endT - startT) / samples;
  for (int i = 0; i < samples; i++) {
    len += ellipseArcDifferential(a, b, startT + i * dt, dt);
  }
  println(samples);
  return (float) len;
}

private double ellipseArcDifferential(float a, float b, float t, float dt) {
  double as2 = Math.pow(a * Math.sin(t), 2.0);
  double bc2 = Math.pow(b * Math.cos(t), 2.0);
  return dt * Math.sqrt(as2 + bc2);
}