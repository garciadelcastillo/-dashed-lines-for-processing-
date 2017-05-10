package dev;

import processing.core.*;
import dashedlines.*;

public class dev extends PApplet {

	Dasher dash;

	Node n1, n2, n3, n4;

	float off = 0;

	public void settings() {
		size(1200, 800);
	}

	// Settings pretty much acts as setup()
	public void setup() {
		strokeCap(SQUARE);
//		strokeCap(ROUND);
//		strokeJoin(ROUND);

		fill(255, 0, 0, 50);
		stroke(0);
		strokeWeight(5);

		n1 = new Node(this, width / 2, height / 2, 5);
		n2 = new Node(this, 3 * width / 4, 3 * height / 4, 5);
		n3 = new Node(this, width / 2 + 200, height / 2, 5);
		n4 = new Node(this, width / 2, height / 2 + 200, 4);

		dash = new Dasher(this);
		dash.pattern(50, 12.5f, 25, 12.5f); // sets dash size and spacing in pixels

		// Random set of dash lines
		// float[] pt = new float[20];
		// for (int i = 0; i < 20; i++) {
		// pt[i] = random(5, 20);
		// }
		// dash.pattern(pt);

		// Testing computing new T given starting t and arc length
		//		float startT = 0.75f * PI;
		//		float endT = 0.25f * PI;
		//		println("goal: " + endT);
		//		float d0 = dash.ellipseArcLength(200, 100, startT, endT, 0.01f);
		//		if (endT < startT)
		//			d0 *= -1;
		//		println("d0: " + d0);
		//		float t0 = dash.ellipseThetaFromArcLength(200, 100, startT, d0, 0.01f);
		//		println("t0: " + t0);


	}

	public void draw() {
		background(255);

		n1.render();
		n2.render();
		n3.render();
		n4.render();

		dash.offset(off);
		off += 1f;

//		drawDashedLine();
//		drawDashedRectangle();
//		drawDashedQuad();
		drawDashedTriangle();
//		drawDashedEllipse();
//		drawDashed-Arc();
		
//		 noFill();
//		drawPolygon();
		

	}

	public void mousePressed() {
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

	public void mouseReleased() {
		n1.dragged = false;
		n2.dragged = false;
		n3.dragged = false;
		n4.dragged = false;
	}

	///////////////////////////////////////

	void drawDashedLine() {
		dash.line(n1.x, n1.y, n2.x, n2.y);
	}

	void drawDashedRectangle() {
		rectMode(CORNERS);
		dash.rect(n1.x, n1.y, n2.x, n2.y);
	}

	void drawDashedEllipse() {
		pushStyle();
		ellipseMode(RADIUS);
		dash.ellipse(n1.x, n1.y, n2.x - n1.x, n2.y - n1.y);
		popStyle();
	}

	void drawDashedArc() {
		float alpha13 = atan2(n3.y - n1.y, n3.x - n1.x);
		float alpha14 = atan2(n4.y - n1.y, n4.x - n1.x);

		// Handle lines
		pushStyle();
		strokeWeight(1);
		stroke(127, 127);
		line(n1.x, n1.y, n3.x, n3.y);
		line(n1.x, n1.y, n4.x, n4.y);
		popStyle();

//		// Processing's native arc: uses thetas instead of polar angles
//		 pushStyle();
//		 ellipseMode(RADIUS);
//		 fill(0, 255, 0, 50);
//		 noStroke();
//		 arc(n1.x, n1.y, n2.x - n1.x, n2.y - n1.y, alpha13, alpha14);
//		 popStyle();

//		// Dashed arc implementation: uses same convention
//		pushStyle();
//		ellipseMode(RADIUS);
//		// noFill();
//		fill(255, 0, 0, 50);
//		stroke(0);
//		dash.arc(n1.x, n1.y, n2.x - n1.x, n2.y - n1.y, alpha13, alpha14, OPEN);
//		popStyle();

		// Now custom version using polar angles:
				pushStyle();
				fill(0, 255, 0, 50);
		//		noStroke();
				ellipseMode(RADIUS);
				// noFill();
				 stroke(0);
				dash.arcPolar(n1.x, n1.y, n2.x - n1.x, n2.y - n1.y, alpha13, alpha14, OPEN);
				popStyle();
	}

	void drawDashedQuad() {
		// noFill();
		dash.quad(n1.x, n1.y, n3.x, n3.y, n2.x, n2.y, n4.x, n4.y);
	}

	void drawDashedTriangle() {
		dash.triangle(n1.x, n1.y, n3.x, n3.y, n2.x, n2.y);
	}

	void drawPolygon() {
		dash.beginShape();
		dash.vertex(n1.x, n1.y);
		dash.vertex(n2.x, n2.y);
		dash.vertex(n3.x, n3.y);
		dash.vertex(n4.x, n4.y);
		dash.endShape(CLOSE);
	}
	
	
	
	////////////////////////////////////////
	// Def. static init
	public static void main(String[] args) {
		PApplet.main("dev.dev");
	}

}
