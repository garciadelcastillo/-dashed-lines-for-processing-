import dashedlines.*;

Dasher dash;

void setup() {
  size(800, 600);
  
  dash = new Dasher(this);
  
  dash.pattern(10, 5, 2, 5);
}

void draw() {
  background(127);
  
  dash.line(100, 100, 300, 100);
  dash.ellipse(width / 2, height / 2, width / 4, height / 4);
  
  
  pushStyle();
  noFill();
  dash.arc(width / 2, height / 2, width / 2, height / 2, 0, HALF_PI);
  popStyle();
}