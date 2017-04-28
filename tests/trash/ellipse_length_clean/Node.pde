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