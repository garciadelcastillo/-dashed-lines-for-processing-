package dev;

import processing.core.*;
import dashedlines.*;

public class dev extends PApplet {

	Dasher dash;

	Node n1, n2, n3, n4;

	Node[] nodes;

	float off = 0;

	public void settings() {
		size(1200, 800, JAVA2D);
	}

	// Settings pretty much acts as setup()
	public void setup() {
		strokeCap(SQUARE);
		//				strokeCap(ROUND);
		//				strokeJoin(ROUND);

		fill(255, 0, 0, 50);
		stroke(0);
		strokeWeight(5);

		n1 = new Node(this, width / 3, height / 3, 5);
		n2 = new Node(this, 2 * width / 3, height / 3, 5);
		n3 = new Node(this, 2 * width / 3, 2 * height / 3, 5);
		n4 = new Node(this, width / 3, 2 * height / 3, 5);
		
//		n1 = new Node(this, 220, 40, 5);
//		n2 = new Node(this, 220, 260, 5);
//		n3 = new Node(this, 35, 200, 5);
//		n4 = new Node(this, 120, 160, 5);
		
		
		nodes = new Node[12];
		float dx = width / 7;
		for (int i = 0; i < 6; i++) {
			nodes[2 * i] = new Node(this, dx + i * dx, 0.25f * height, 5);
			nodes[2 * i + 1] = new Node(this, dx + i * dx, 0.75f * height, 5);
		}

		dash = new Dasher(this);
//		dash.pattern(50, 10, 25, 10); // sets dash size and spacing in pixels
//		dash.pattern(new float[] {});
//		dash.pattern(50);
		
		
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

		for (int i = 0; i < nodes.length; i++) {
			nodes[i].render();
		}

		dash.offset(off);
		off += 1f;
//		dash.offset(0);

		//		drawDashedLine();
		//		drawDashedRectangle();
		//		drawDashedQuad();
		//		drawDashedTriangle();
		//		drawDashedEllipse();

		//		noFill();
		//		drawPolygon();

		//		drawShapeOpen();
		//		drawShapeClosed();
		//		drawShapePoints();
		//		drawShapeLines();
		//		drawShapeTriangles();
		//		drawShapeTriangleStrip();
		//		drawShapeTriangleFan();
		//		drawShapeQuads();
		//		drawShapeQuadStrip();
		//		drawShapeWithStyleChanges();

//		testQuadraticBezierCurves();
//		testCubicBezierCurves();
//		dash.cubicBezierArcLength(1f, n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);
		
		drawDashedBezier();
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

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i].inside(mouseX, mouseY)) {
				nodes[i].dragged = true;
			}
		}
	}

	public void mouseReleased() {
		n1.dragged = false;
		n2.dragged = false;
		n3.dragged = false;
		n4.dragged = false;

		for (int i = 0; i < nodes.length; i++) {
			nodes[i].dragged = false;
		}
	}







	///////////////////////////////////////

	void drawDashedLine() {
		dash.line(n1.x, n1.y, n2.x, n2.y);
	}

	void drawDashedRectangle() {
		rectMode(CORNERS);
		dash.rect(n1.x, n1.y, n3.x, n3.y);
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
		dash.quad(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);
	}

	void drawDashedTriangle() {
		dash.triangle(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y);
	}

	void drawPolygon() {
		dash.beginShape();
		dash.vertex(n1.x, n1.y);
		dash.vertex(n2.x, n2.y);
		dash.vertex(n3.x, n3.y);
		dash.vertex(n4.x, n4.y);
		dash.endShape(CLOSE);
	}

	void drawShapeOpen() {
		dash.beginShape();
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape();
	}

	void drawShapeClosed() {
		dash.beginShape();
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapePoints() {
		dash.beginShape(POINTS);
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeLines() {
		dash.beginShape(LINES);
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeTriangles() {
		dash.beginShape(TRIANGLES);
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeTriangleStrip() {
		dash.beginShape(TRIANGLE_STRIP);
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeTriangleFan() {
		Node[] fanNodes = new Node[17];
		fanNodes[0] = new Node(this, width / 2, height / 2, 5);

		float alpha = TAU / (fanNodes.length - 1);
		for (int i = 1; i < fanNodes.length; i++) {
			fanNodes[i] = new Node(this, width / 2 + 200 * cos(alpha * (i - 1)),
					height / 2 + 200 * sin(alpha * (i - 1)), 5);
		}

		dash.beginShape(TRIANGLE_FAN);
		for (int i = 0; i < fanNodes.length; i++) {
			dash.vertex(fanNodes[i].x, fanNodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeQuads() {
		dash.beginShape(QUADS);
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeQuadStrip() {
		dash.beginShape(QUAD_STRIP);
		for (int i = 0; i < nodes.length; i++) {
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void drawShapeWithStyleChanges() {
		dash.beginShape(QUAD_STRIP);
		for (int i = 0; i < nodes.length; i++) {
			fill(i * 255f / nodes.length, 0, 0, 200);
			dash.vertex(nodes[i].x, nodes[i].y);
		}
		dash.endShape(CLOSE);
	}

	void testQuadraticBezierCurves() {
		pushStyle();
		fill(255, 0, 0, 50);
		//stroke(0);
		noStroke();
		strokeWeight(1);
		beginShape();
		vertex(n1.x, n1.y);
		quadraticVertex(n2.x, n2.y, n3.x, n3.y);
		endShape();

		noFill();
		int segments = 10;
		strokeWeight(5);
		stroke(0);
		float dt = 1 / (float) segments;
		for (int i = 0; i < segments; i += 2) {
			float a = i * dt;
			float b = i * dt + dt;
			//dash.subQuadraticBezier(a, b, n1.x, n1.y, n2.x, n2.y, n3.x, n3.y);  // protected method
		}
		popStyle();
	}
	
	void testCubicBezierCurves() {
		pushStyle();
		fill(255, 0, 0, 50);
		noStroke();
		strokeWeight(1);
		bezier(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);

		noFill();
		int segments = 20;
		strokeWeight(5);
		stroke(0);
		float dt = 1 / (float) segments;
		for (int i = 0; i < segments; i += 2) {
			float a = i * dt;
			float b = i * dt + dt;
//			dash.subCubicBezier(a, b, n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);  // protected method
		}
		popStyle();
		
	}
	
	
	void drawDashedBezier() {
		pushStyle();
		strokeWeight(5);
		stroke(0);
		dash.bezier(n1.x, n1.y, n2.x, n2.y, n3.x, n3.y, n4.x, n4.y);
		popStyle();
	}



	////////////////////////////////////////
	// Def. static init
	////////////////////////////////////////
	public static void main(String[] args) {
		PApplet.main("dev.dev");
	}

}
