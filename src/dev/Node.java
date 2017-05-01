package dev;

import processing.core.*;

class Node {

	PApplet p;
	float x, y;
	float r;
	boolean dragged;

	Node(PApplet p_, float x_, float y_, float r_) {
		p = p_;
		x = x_;
		y = y_;
		r = r_;
	}

	void render() {
		p.pushStyle();
		p.noStroke();
		p.fill(127, 50);
		p.ellipse(x, y, 2 * r, 2 * r);
		p.popStyle();
		if (dragged) {
			x = p.mouseX;
			y = p.mouseY;
		}
	}

	boolean inside(float xpos, float ypos) {
		return PApplet.dist(x, y, xpos, ypos) < r;
	}
}