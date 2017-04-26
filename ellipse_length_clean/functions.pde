
// Given a & b as the major and minor semi-axes, 
// and t (0, TAU) as the parameter along the ellipse (not the polar angle of the point),
// returns the corresponding PVector point on the ellipse.
private PVector pointOnEllipseAtParameter(float a, float b, float t) {
  float x = a * cos(t);
  float y = b * sin(t);
  return new PVector(x, y);
}

// Given a & b as the major and minor semi-axes, 
// and aplha (0, TAU) as the polar angle of the point,
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

//private float ellipseCircunference(float a, float b, int mode) {
  
//}