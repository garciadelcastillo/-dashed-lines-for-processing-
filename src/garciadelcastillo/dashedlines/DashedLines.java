package garciadelcastillo.dashedlines;

import processing.core.*;
import processing.data.*;

public class DashedLines {

	protected final static String VERSION = "##library.prettyVersion##";
	protected PApplet p;
	protected PGraphics g;

	//	██████╗ ██╗   ██╗██████╗ ██╗     ██╗ ██████╗     █████╗ ██████╗ ██╗
	//	██╔══██╗██║   ██║██╔══██╗██║     ██║██╔════╝    ██╔══██╗██╔══██╗██║
	//	██████╔╝██║   ██║██████╔╝██║     ██║██║         ███████║██████╔╝██║
	//	██╔═══╝ ██║   ██║██╔══██╗██║     ██║██║         ██╔══██║██╔═══╝ ██║
	//	██║     ╚██████╔╝██████╔╝███████╗██║╚██████╗    ██║  ██║██║     ██║
	//	╚═╝      ╚═════╝ ╚═════╝ ╚══════╝╚═╝ ╚═════╝    ╚═╝  ╚═╝╚═╝     ╚═╝

	/**
	 * Main constructor, pass a reference to the current PApplet
	 * 
	 * @param theParent
	 */
	public DashedLines(PApplet theParent) {
		p = theParent;
		g = p.getGraphics();
		updateDashPatternLength();
	}

	/**
	 * Sets the lengths of the dash pattern in pixels.
	 * 
	 * @param d1
	 *            Dash length (cloned as gap length)
	 */
	public void pattern(float d1) {
		dashPattern = new float[2];
		dashPattern[0] = d1;
		dashPattern[1] = d1;
		updateDashPatternLength();
	}

	/**
	 * Sets the lengths of the dash pattern in pixels.
	 * 
	 * @param d1
	 *            Dash length
	 * @param d2
	 *            Gap length
	 */
	public void pattern(float d1, float d2) {
		dashPattern = new float[2];
		dashPattern[0] = d1;
		dashPattern[1] = d2;
		updateDashPatternLength();
	}

	/**
	 * Sets the lengths of the dash pattern in pixels.
	 * 
	 * @param d1
	 *            Dash length
	 * @param d2
	 *            Gap length
	 * @param d3
	 *            Dash length
	 * @param d4
	 *            Gap length
	 */
	public void pattern(float d1, float d2, float d3, float d4) {
		dashPattern = new float[4];
		dashPattern[0] = d1;
		dashPattern[1] = d2;
		dashPattern[2] = d3;
		dashPattern[3] = d4;
		updateDashPatternLength();
	}

	/**
	 * Sets the lengths of the dash pattern in pixels.
	 * 
	 * @param ds
	 *            Array of dash-gap-dash... lengths
	 */
	public void pattern(float[] ds) {
		int len = ds.length;
		if (len == 0)
			return;

		if (ds.length % 2 == 1) {
			len++;
		}

		// Avoid references
		float[] temp = new float[len];
		System.arraycopy(ds, 0, temp, 0, ds.length);

		if (ds.length % 2 == 1) {
			ds[len - 1] = ds[len - 2];
		}
		dashPattern = ds;
		updateDashPatternLength();
	}

	/**
	 * Sets the initial offset along the curve at which the pattern will start.
	 * 
	 * @param off
	 *            Offset distance in pixels
	 */
	public void offset(float off) {
		offset = off;
	}

	/**
	 * Draws a dashed line between two points.
	 * 
	 * @param x1
	 *            X-coordinate of the first point
	 * @param y1
	 *            Y-coordinate of the first point
	 * @param x2
	 *            X-coordinate of the second point
	 * @param y2
	 *            Y-coordinate of the second point
	 */
	public void line(float x1, float y1, float x2, float y2) {

		//		// Compute theta parameters for start-ends of dashes and gaps
		//		// TODO: precompute the size of the array and create it as an array right away
		//		FloatList ts = new FloatList();
		//		int id = 0;
		//		float run = 0;
		//		float t = 0;
		//		float len = PApplet.dist(x1, y1, x2, y2);
		//
		//		// If there is ofsset, precompute first t
		//		if (offset != 0) {
		//			// p.println("testing offset");
		//			run += offset;
		//
		//			// Adjust run to be less than one dashPatternLength behind 0
		//			if (run > 0) {
		//				run -= dashPatternLength * (1 + (int) (offset / dashPatternLength));
		//			} else {
		//				// note offset is negative, so adding positive increment
		//				run -= dashPatternLength * (int) (offset / dashPatternLength);
		//			}
		//
		//			// Now process the chunk before t = 0
		//			while (run < 0) {
		//				run += dashPattern[id % dashPattern.length];
		//				id++;
		//				// if past t = 0 and at the end point of a dash, add t = 0
		//				if (run >= 0 && id % 2 == 1) {
		//					ts.append(0);
		//				}
		//			}
		//		}
		//
		//		while (run < len) {
		//			t = run / len;
		//			ts.append(t);
		//			run += dashPattern[id % dashPattern.length];
		//			id++;
		//		}
		//
		//		// If last t was the startpoint of a dash, close it at the end of the line
		//		if (id % 2 == 1) {
		//			ts.append(1);
		//		}
		//
		//		float[] tsA = ts.array(); // TODO: improve the list-array situation
		//
		//		// Draw dashes
		//		p.pushStyle();
		//		float dx = x2 - x1;
		//		float dy = y2 - y1;
		//		for (int i = 0; i < tsA.length; i += 2) {
		//			p.line(x1 + tsA[i] * dx, y1 + tsA[i] * dy, x1 + tsA[i + 1] * dx, y1 + tsA[i + 1] * dy);
		//		}
		//		p.popStyle();

		// Let's try for a while relying on polyshape
		this.beginShapeImpl();
		this.vertexImpl(x1, y1);
		this.vertexImpl(x2, y2);
		this.endShapeImpl(PApplet.OPEN);
	}

	/**
	 * Draws a dashed rectangle. Uses the same signature for a, b, c, d as
	 * Processing's native implementation, and is sensible to rectMode() status.
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public void rect(float a, float b, float c, float d) {
		// From Processing's core
		float hradius, vradius;
		switch (g.rectMode) {
		case PApplet.CORNERS:
			break;
		case PApplet.CORNER:
			c += a;
			d += b;
			break;
		case PApplet.RADIUS:
			hradius = c;
			vradius = d;
			c = a + hradius;
			d = b + vradius;
			a -= hradius;
			b -= vradius;
			break;
		case PApplet.CENTER:
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

		// Using core's B+V+E for proper dash continuity calcs
		this.beginShapeImpl();
		this.vertexImpl(a, b);
		this.vertexImpl(c, b);
		this.vertexImpl(c, d);
		this.vertexImpl(a, d);
		this.endShapeImpl(PApplet.CLOSE);

	}

	/**
	 * Draws a dashed triangle from three points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		// Using core's B+V+E for proper dash continuity calcs
		this.beginShapeImpl();
		this.vertexImpl(x1, y1);
		this.vertexImpl(x2, y2);
		this.vertexImpl(x3, y3);
		this.endShapeImpl(PApplet.CLOSE);
	}

	/**
	 * Draws a dashed quad from four points.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		// Using core's B+V+E for proper dash continuity calcs
		this.beginShapeImpl();
		this.vertexImpl(x1, y1);
		this.vertexImpl(x2, y2);
		this.vertexImpl(x3, y3);
		this.vertexImpl(x4, y4);
		this.endShapeImpl(PApplet.CLOSE);
	}


	/**
	 * Draws a dashed ellipse. Uses the same signature for a, b, c, d as
	 * Processing's native implementation, and is sensible to ellipseMode()
	 * status. BEWARE! Dashed ellipses are expensive to compute, use with
	 * caution ;)
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public void ellipse(float a, float b, float c, float d) {
		this.arc(a, b, c, d, 0, PApplet.TAU, 0);
	}

	/**
	 * Draws a dashed arc. Uses the same signature for a, b, c, d, start, stop,
	 * as Processing's native implementation, and is sensible to ellipseMode()
	 * status. BEWARE! Dashed arcs are expensive to compute, use with caution ;)
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param start
	 * @param stop
	 */
	public void arc(float a, float b, float c, float d, float start, float stop) {
		this.arc(a, b, c, d, start, stop, 0);
	}

	/**
	 * Draws a dashed arc. Uses the same signature for a, b, c, d, start, stop,
	 * mode as Processing's native implementation, and is sensible to
	 * ellipseMode() status. BEWARE! Dashed arcs are expensive to compute, use
	 * with caution ;)
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param start
	 * @param stop
	 * @param mode
	 */
	public void arc(float a, float b, float c, float d, float start, float stop, int mode) {
		// From Processing's core, CORNER-oriented vars
		float x = a;
		float y = b;
		float w = c;
		float h = d;

		if (g.ellipseMode == PApplet.CORNERS) {
			w = c - a;
			h = d - b;
		} else if (g.ellipseMode == PApplet.RADIUS) {
			x = a - c;
			y = b - d;
			w = c * 2;
			h = d * 2;
		} else if (g.ellipseMode == PApplet.DIAMETER) { // == CENTER
			x = a - c / 2f;
			y = b - d / 2f;
		}

		if (w < 0) { // undo negative width
			x += w;
			w = -w;
		}

		if (h < 0) { // undo negative height
			y += h;
			h = -h;
		}
		float w2 = 0.5f * w, h2 = 0.5f * h;

		// Make sure the loop will exit before starting while
		if (!Float.isInfinite(start) && !Float.isInfinite(stop)) {
			// ignore equal and degenerate cases
			if (stop > start) {
				// make sure that we're starting at a useful point
				while (start < 0) {
					start += PApplet.TAU;
					stop += PApplet.TAU;
				}

				if (stop - start > PApplet.TAU) {
					// don't change start, it is visible in PIE mode
					stop = start + PApplet.TAU;
				}

				// Compute theta parameters for start-ends of dashes and gaps
				FloatList ts = new FloatList(); // TODO: precompute the size of the t array and create it as an array directly
				int id = 0;
				float run = 0;
				float t = start;
				float dt = ARC_DIFFERENTIAL_PRECISION;
				float len = ellipseArcLength(w2, h2, start, stop, dt);
				float nextL = 0;
				float nextT = 0;

				// If there is ofsset, precompute first t
				if (offset != 0) {
					nextL += offset;

					// Adjust run to be less than one dashPatternLength behind 0
					if (nextL > 0) {
						nextL -= dashPatternLength * (1 + (int) (offset / dashPatternLength));
					} else {
						// note offset is negative, so adding positive increment
						nextL -= dashPatternLength * (int) (offset / dashPatternLength);
					}
					nextT = ellipseThetaFromArcLength(w2, h2, start, nextL, dt);

					// Process the chunk before t = start
					// This method is not very optimal, but oh well, stupid ellipse geometry... :P
					while (nextT <= start) {
						nextL += dashPattern[id % dashPattern.length];
						id++;
						nextT = ellipseThetaFromArcLength(w2, h2, start, nextL, dt); // compute from start to avoid accumulated imprecision
					}
					if (id % 2 == 1) {
						ts.append(start);
					}

					// Set the params off to run regular dashing
					run = nextL;
					t = nextT;
				}

				// Compute dash t params
				while (run < len) {
					run += ellipseArcDifferential(w2, h2, t, dt);
					if ((int) run >= nextL) {
						ts.append(t);
						nextL += dashPattern[id % dashPattern.length];
						id++;
					}
					t += dt;
				}

				// This should be optimized...
				float[] tsA = ts.array();

				// Draw the fill part
				p.pushStyle();
				p.noStroke();
				p.ellipseMode(PApplet.CORNER); // all correct vars are already calculated, so why not use them...? :)
				p.arc(x, y, w, h, start, stop, mode);
				p.popStyle();

				// Draw dashes
				p.pushStyle();
				p.noFill();
				p.ellipseMode(PApplet.CORNER);

				// If PIE mode, draw center-start line
				if (mode == PApplet.PIE) {
					this.line(x + w2, y + h2, x + w2 + w2 * (float) Math.cos(start),
							y + h2 + h2 * (float) Math.sin(start));
				}

				// Arc dashes
				for (int i = 0; i < tsA.length; i += 2) {
					if (i == tsA.length - 1) {
						p.arc(x, y, w, h, tsA[i], stop);
					} else {
						p.arc(x, y, w, h, tsA[i], tsA[i + 1]);
					}
				}

				// If PIE, draw end-center line,
				// else if CHORD draw end-start line.
				if (mode == PApplet.PIE) {
					this.line(x + w2 + w2 * (float) Math.cos(stop), y + h2 + h2 * (float) Math.sin(stop), x + w2,
							y + h2);
				} else if (mode == PApplet.CHORD) {
					this.line(x + w2 + w2 * (float) Math.cos(stop), y + h2 + h2 * (float) Math.sin(stop),
							x + w2 + w2 * (float) Math.cos(start), y + h2 + h2 * (float) Math.sin(start));
				}

				p.popStyle();
			}
		}
	}

	// Create a dashed arc using Processing same function signature,
	// however using start/stop as POLAR ANGLES, not THETA parameters.
	// This is not consistent with Processing's implementation,
	// but just feels right geometrically... ;)

	/**
	 * Draws a dashed arc. As opposed to Processing's native implementation,
	 * which uses start and stop as the parameter along the arc, this method
	 * uses start and stop as the polar angles that define the boundaries of the
	 * arc. This is not consistent with Processing's implementation, but just
	 * feels right geometrically... ;)
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param start
	 * @param stop
	 * @param mode
	 */
	public void arcPolar(float a, float b, float c, float d, float start, float stop, int mode) {
		float w = c;
		float h = d;
		if (g.ellipseMode == PApplet.CORNERS) {
			w = c - a;
			h = d - b;
		} else if (g.ellipseMode == PApplet.RADIUS) {
			w = c * 2;
			h = d * 2;
		}
		if (w < 0) { // undo negative width
			w = -w;
		}
		if (h < 0) { // undo negative height
			h = -h;
		}
		float w2 = 0.5f * w;
		float h2 = 0.5f * h;

		float thetaStart = ellipsePolarToTheta(w2, h2, start);
		float thetaStop = ellipsePolarToTheta(w2, h2, stop);

		log(thetaStart + " " + thetaStop);
		this.arc(a, b, c, d, thetaStart, thetaStop, mode);
	}

	/**
	 * Starts a dashed shape. Same as Processing's native beginShape().
	 */
	public void beginShape() {
		this.beginShape(PApplet.POLYGON);
	}

	/**
	 * Starts a dashed shape. Same as Processing's native beginShape().
	 * 
	 * @param kind
	 */
	public void beginShape(int kind) {
		this.beganShape = true;
		this.vertexCount = 0;
		this.shape = kind;
	}

	/**
	 * Adds a vertex to the current shape.
	 * 
	 * @param x
	 * @param y
	 */
	public void vertex(float x, float y) {

		if (!this.beganShape) {
			PApplet.println("Must call beginShape() before adding any vertex()");
			return;
		}

		// curveVertexCount = 0;  // from Processing's implementation, not necessary yet

		// Ran out of vertices in the buffer?
		if (vertexCount == vertices.length) {
			float temp[][] = new float[vertexCount << 1][VERTEX_FIELD_COUNT];
			System.arraycopy(vertices, 0, temp, 0, vertexCount);
			vertices = temp;
		}

		vertices[vertexCount][PApplet.X] = x;
		vertices[vertexCount][PApplet.Y] = y;
		//		vertices[vertexCount][PApplet.Z] = 0;  // start easy with 2D
		vertexCount++;

		// Depending on the shape mode, shapes are drawn on the fly. 
		// This is useful in case the user is changing style properties
		// while adding vertices before endShape.
		// @TOTHINK: it also means the fill of a shape overlaos with the prev stroke... :(
		// Inspired by the PGraphicsJava2D implementation.
		switch (this.shape) {

		case PApplet.POINTS:
			p.point(x, y);
			break;

		case PApplet.LINES:
			if ((vertexCount % 2) == 0) {
				this.line(vertices[vertexCount - 2][PApplet.X], vertices[vertexCount - 2][PApplet.Y], x, y);
			}
			break;

		case PApplet.TRIANGLES:
			if ((vertexCount % 3) == 0) {
				this.triangle(vertices[vertexCount - 3][PApplet.X], vertices[vertexCount - 3][PApplet.Y],
						vertices[vertexCount - 2][PApplet.X], vertices[vertexCount - 2][PApplet.Y], x, y);
			}
			break;

		case PApplet.TRIANGLE_STRIP:
			if (vertexCount >= 3) {
				this.beginShapeImpl();
				this.vertexImpl(vertices[vertexCount - 2][PApplet.X], vertices[vertexCount - 2][PApplet.Y]);
				this.vertexImpl(vertices[vertexCount - 1][PApplet.X], vertices[vertexCount - 1][PApplet.Y]);
				this.vertexImpl(vertices[vertexCount - 3][PApplet.X], vertices[vertexCount - 3][PApplet.Y]);
				this.endShapeImpl(vertexCount - 3 == 0 ? PApplet.CLOSE : PApplet.OPEN); // avoid overlapping lines, make Alykhan happy ;)
			}
			break;

		case PApplet.TRIANGLE_FAN:
			if (vertexCount >= 3) {
				this.beginShapeImpl();
				this.vertexImpl(vertices[vertexCount - 2][PApplet.X], vertices[vertexCount - 2][PApplet.Y]);
				this.vertexImpl(vertices[vertexCount - 1][PApplet.X], vertices[vertexCount - 1][PApplet.Y]);
				this.vertexImpl(vertices[0][PApplet.X], vertices[0][PApplet.Y]);
				this.endShapeImpl(vertexCount - 3 == 0 ? PApplet.CLOSE : PApplet.OPEN); // avoid overlapping lines, make Alykhan happy ;)
			}
			break;

		case PApplet.QUAD:
		case PApplet.QUADS:
			if ((vertexCount % 4) == 0) {
				this.quad(vertices[vertexCount - 4][PApplet.X], vertices[vertexCount - 4][PApplet.Y],
						vertices[vertexCount - 3][PApplet.X], vertices[vertexCount - 3][PApplet.Y],
						vertices[vertexCount - 2][PApplet.X], vertices[vertexCount - 2][PApplet.Y], x, y);
			}
			break;

		case PApplet.QUAD_STRIP:
			// 0---2---4
			// |   |   |
			// 1---3---5
			if ((vertexCount >= 4) && ((vertexCount % 2) == 0)) {
				this.beginShapeImpl();
				this.vertexImpl(vertices[vertexCount - 3][PApplet.X], vertices[vertexCount - 3][PApplet.Y]);
				this.vertexImpl(vertices[vertexCount - 1][PApplet.X], vertices[vertexCount - 1][PApplet.Y]);
				this.vertexImpl(vertices[vertexCount - 2][PApplet.X], vertices[vertexCount - 2][PApplet.Y]);
				this.vertexImpl(vertices[vertexCount - 4][PApplet.X], vertices[vertexCount - 4][PApplet.Y]);
				this.endShapeImpl(vertexCount - 4 == 0 ? PApplet.CLOSE : PApplet.OPEN); // avoid overlapping lines, make Alykhan happy ;)
			}
			break;

		case PApplet.POLYGON:
			// do nothing and wait for endShape() to resolve this
			break;
		}


	}


	// @TODO: Implement vertex(x, y, z) and vertex(float[] v)

	/**
	 * End current dashed shape.
	 */
	public void endShape() {
		this.endShape(PApplet.OPEN);
	}

	/**
	 * End current dashed shape.
	 * 
	 * @param mode
	 */
	public void endShape(int mode) {
		if (this.shape == PApplet.POLYGON) {
			this.beginShapeImpl();
			for (int i = 0; i < vertexCount; i++) {
				this.vertexImpl(vertices[i][PApplet.X], vertices[i][PApplet.Y]);
			}
			this.endShapeImpl(mode);
		}

		// Prevent from further calls to vertex()
		this.beganShape = false;
	}



	/**
	 * Draws a dashed cubic Bézier curve. BEWARE! Dashed curves are expensive to
	 * compute, use with caution ;)
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	public void bezier(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {

		FloatList ts = new FloatList();
		int id = 0;
		float t = 0;
		float run = 0;
		float dt = BEZIER_DIFFERENTIAL_PRECISION;
		PVector p0 = new PVector(x1, y1);
		PVector pt = new PVector();
		float nextL = 0;
		boolean dash = false;

		if (g.fill == true) {
			p.pushStyle();
			p.noStroke();
			p.bezier(x1, y1, x2, y2, x3, y3, x4, y4);
			p.popStyle();
		}

		// If there is offset, precompute first nextL
		if (offset != 0) {
			nextL += offset;

			// Adjust run to be less than one dashPatternLength behind 0
			if (nextL > 0) {
				nextL -= dashPatternLength * (1 + (int) (offset / dashPatternLength));
			} else {
				// note offset is negative, so adding positive increment
				nextL -= dashPatternLength * (int) (offset / dashPatternLength);
			}

			// Adjust nextL to first position after zero
			while (nextL < 0) {
				nextL += dashPattern[id % dashPattern.length];
				id++;
				dash = !dash;
			}
		}

		if (nextL <= 0) {
			nextL += dashPattern[id % dashPattern.length];
			id++;
			dash = !dash;
		}


		// If at this point dash == true, then start drawing and add a vertex
		if (dash) {
			ts.append(0);
		}

		float d;
		while (t < 1) {
			t += dt;
			if (t > 1)
				t = 1;
			pt = this.pointOnCubicBezier(t, x1, y1, x2, y2, x3, y3, x4, y4);
			d = PApplet.dist(p0.x, p0.y, pt.x, pt.y);
			run += d;
			p0 = pt;

			if (run >= nextL) {
				// Just changed dash state, adjust accordingly
				dash = !dash;

				// Interpolate for a better approximation
				float nt = (nextL + d - run) / d;
				ts.append(t - dt + nt * dt);

				// Move on to next pattern segment
				nextL += dashPattern[id % dashPattern.length];
				id++;
			}
		}

		// Close unfinished dash
		if (dash) {
			ts.append(1);
		}

		// Draw bezier curves between those parameters
		p.pushStyle();
		p.noFill();
		int len = ts.size();
		for (int i = 0; i < len; i += 2) {
			this.subCubicBezier(ts.get(i), ts.get(i + 1), x1, y1, x2, y2, x3, y3, x4, y4);
		}
		p.popStyle();
	}



	//	██████╗ ██████╗ ██╗██╗   ██╗ █████╗ ████████╗███████╗
	//	██╔══██╗██╔══██╗██║██║   ██║██╔══██╗╚══██╔══╝██╔════╝
	//	██████╔╝██████╔╝██║██║   ██║███████║   ██║   █████╗  
	//	██╔═══╝ ██╔══██╗██║╚██╗ ██╔╝██╔══██║   ██║   ██╔══╝  
	//	██║     ██║  ██║██║ ╚████╔╝ ██║  ██║   ██║   ███████╗
	//	╚═╝     ╚═╝  ╚═╝╚═╝  ╚═══╝  ╚═╝  ╚═╝   ╚═╝   ╚══════╝
	/**
	 * Parameter increment at which the arc will be sampled. A value of 0.005f
	 * results in TAU/0.02f ~ 314 samples.
	 */
	protected float ARC_DIFFERENTIAL_PRECISION = 0.02f;

	/**
	 * Parameter increment at which Bézier curves will be sampled. A value of
	 * 0.005f results in about 200 samples per curve.
	 */
	protected float BEZIER_DIFFERENTIAL_PRECISION = 0.005f;

	/**
	 * The pattern of dash-gap-dash-gap... to be used to render dashed lines, in
	 * pixels. For example: { 10, 10 } corresponds to 10px dash + 10px gap
	 */
	protected float[] dashPattern = { 10, 10 };

	/**
	 * Keeps track of the accumulated length of the complete dash pattern. Eg:
	 * for 10-5-2-5 it is 22.
	 */
	protected float dashPatternLength = 0;

	/**
	 * How far along the curve to start the dashed pattern.
	 */
	protected float offset = 0;

	/**
	 * Type of shape passed to beginShape(), zero if no shape is currently being
	 * drawn. Based off Processing's core implementation for begin/endShape() +
	 * vertex().
	 */
	protected int shape;

	/**
	 * Is a shape currently being drawn?
	 */
	protected boolean beganShape = false;


	/**
	 * Default number of vertices to initialize the buffers.
	 */
	protected static final int DEFAULT_VERTICES = 512;

	/**
	 * Default number of members per vertex.
	 */
	protected static final int VERTEX_FIELD_COUNT = 2; // let's start simple with 2D

	// Vertices
	// Two lists will be maintained: 
	// - a primary one, used as a first layer to store vertices from the public API (beginShape+vertex+endShape)
	// - a secondary one, used by the internal beginShape+vertex+endShape implementation and subsidiary methods
	/**
	 * Array of vertex member values. This one is used for the public method.
	 */
	protected float vertices[][] = new float[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];

	/**
	 * Amount of stored vertices. This one is used for the public method.
	 */
	protected int vertexCount;

	/**
	 * Array of vertex member values. This one is used for the private
	 * implementation.
	 */
	protected float verticesImpl[][] = new float[DEFAULT_VERTICES][VERTEX_FIELD_COUNT];

	/**
	 * Amount of stored vertices. This one is used for the private
	 * implementation.
	 */
	protected int vertexCountImpl;


	/**
	 * A shortcut for dev purposes
	 * 
	 * @param foo
	 */
	protected void log(Object foo) {
		PApplet.println(foo);
	}

	/**
	 * Computes dashPatternLength
	 */
	protected void updateDashPatternLength() {
		dashPatternLength = 0;
		for (int i = 0; i < dashPattern.length; i++) {
			dashPatternLength += dashPattern[i];
		}
	}

	/**
	 * Given a & b as the major and minor semi-axes, return an approximation to
	 * the circumference of the ellipse. Use mode 0 for Ramanujan's fast
	 * approximation. For very low b/a ratios use mode 1 for a more precise
	 * (albeit) expensive recursive approximation. Use mode 2 for a differential
	 * approximation, with precision as the dt increments in theta.
	 * 
	 * @param a
	 * @param b
	 * @param mode
	 * @param precision
	 * @return
	 */
	protected float ellipseCircumference(float a, float b, int mode, float precision) {
		// Turns out, ellipses are a little more complicated than circles!
		// There is no closed form equation to find the length, nor the arc
		// length given an angle...
		// The exact solution can only be obtained solving for the complete
		// elliptical integral of the second kind:
		// C = 4 a E(1 - b^2 / a^2);
		// For example, for a = 200 and b = 100:
		// C = 800 E(3/4) ≈ 968.844822054...
		// See:
		// http://www.wolframalpha.com/input/?i=4+200+EllipticE%5B1+-+100%5E2+%2F200%5E2%5D
		// Or better yet:
		// https://www.wolframalpha.com/input/?i=integrate+sqrt(a%5E2*sin%5E2(t)+%2B+b%5E2*cos%5E2(t))+from+0+to+2*pi+where+a+%3D+200+and+b+%3D+100

		// In this method, we will default to Ramanujan's approximation
		// (quite good for common Processing ellipse size ranges, with precision
		// errors ranging from the 6th to the 4th significant digit of the mantissa
		// depending on the 1 > b/a > 0 ratio) or rely on an expensive yet preciser recursive
		// approximation:
		switch (mode) {
		// Recursive approximation
		// https://math.stackexchange.com/a/73504/440507
		case 1:
			float f = 1, s, v = 0.5F * (1 + b / a), w;
			w = (1 - (b / a) * (b / a)) / (4 * v);
			s = v * v;
			while (Math.abs(w) > PApplet.EPSILON) {
				v = 0.5f * (v + (float) Math.sqrt((v - w) * (v + w)));
				w = (0.5f * w) * (0.5f * w) / v;
				f *= 2;
				s -= f * w * w;
			}
			return 2 * a * PApplet.PI * s / v;

		// Internal recursive differential approximation
		case 2:
			return ellipseArcLength(a, b, 0, PApplet.TAU, precision);

		// Ramanujan's approximation
		// Ramanujan, S. "Modular Equations and Approximations to pi." Quart. J.
		// Pure. Appl. Math. 45, 350-372, 1913-1914. Section 16, Eq. 49
		// https://books.google.com/books?id=oSioAM4wORMC&pg=PA39#v=onepage&q&f=false
		case 0:
		default:
			return PApplet.PI * (3 * (a + b) - (float) Math.sqrt((a + 3 * b) * (3 * a + b)));
		}
	}

	/**
	 * Given a & b as the major and minor semi-axes, startT and endT as theta
	 * parameters (not angle) to measure between, and precision as the dt (theta
	 * increment) to be used in calculations, returns a lower-bound
	 * approximation to the arc length based on differential approximation.
	 * 
	 * @param a
	 * @param b
	 * @param startT
	 * @param endT
	 * @param precision
	 * @return
	 */
	protected float ellipseArcLength(float a, float b, float startT, float endT, float precision) {
		// Similarly to the circumference, tThe exact solution to the arc length
		// of an ellipse can only be obtained solving for the complete elliptical integral of
		// the second kind.
		// For example, for a = 200 and b = 100, between TAU / 8 and TAU / 4:
		// https://www.wolframalpha.com/input/?i=integrate+sqrt(a%5E2*sin%5E2(t)+%2B+b%5E2*cos%5E2(t))+from+0.25+*+pi+to+0.5+*+pi+where+a+%3D+200+and+b+%3D+100

		// Housekeeping
		if (a < 0)
			a = -a;
		if (b < 0)
			b = -b;
		if (startT > endT) {
			float tmp = startT;
			startT = endT;
			endT = tmp;
		}

		double len = 0;
		int samples = Math.round(PApplet.TAU / precision);
		float dt = (endT - startT) / samples;
		for (int i = 0; i < samples; i++) {
			len += ellipseArcDifferential(a, b, startT + i * dt, dt);
		}
		return (float) len;
	}

	/**
	 * Given a & b the major and minor semi-axes, t as the starting theta
	 * parameter, and the arc length of the ellipse (can be negative), return
	 * the approximate theta along the ellipse after adding length.
	 * 
	 * @param a
	 * @param b
	 * @param t
	 * @param length
	 * @param precision
	 * @return
	 */
	protected float ellipseThetaFromArcLength(float a, float b, float t, float length, float precision) {
		// Housekeeping
		if (a < 0)
			a = -a;
		if (b < 0)
			b = -b;

		double len = 0;
		float dt = precision; // if len is neg, will get the sign
		float T = t;

		if (length < 0) {
			while (len >= length) {
				len -= ellipseArcDifferential(a, b, T, dt);
				T -= dt;
			}
		} else {
			while (len <= length) {
				len += ellipseArcDifferential(a, b, T, dt);
				T += dt;
			}
		}

		return T;
	}

	/**
	 * Calculate the differential arc length at a given parameter t with given
	 * dt.
	 * 
	 * @param a
	 * @param b
	 * @param t
	 * @param dt
	 * @return
	 */
	protected double ellipseArcDifferential(double a, double b, double t, double dt) {
		double as2 = Math.pow(a * Math.sin(t), 2.0);
		double bc2 = Math.pow(b * Math.cos(t), 2.0);
		return dt * Math.sqrt(as2 + bc2);
	}

	/**
	 * Given a & b as the major and minor semi-axes, and t [0, TAU] as the
	 * parameter along the ellipse (not the polar angle of the point), returns
	 * the corresponding PVector point on the ellipse.
	 * 
	 * @param x
	 * @param y
	 * @param a
	 * @param b
	 * @param t
	 * @return
	 */
	protected PVector pointOnEllipseAtParameter(float x, float y, float a, float b, float t) {
		float px = a * (float) Math.cos(t);
		float py = b * (float) Math.sin(t);
		return new PVector(x + px, y + py);
	}

	/**
	 * Given a & b as the major and minor semi-axes, and alpha as polar angle,
	 * return the equivalent theta parameter.
	 * 
	 * @param a
	 * @param b
	 * @param alpha
	 * @return
	 */
	protected float ellipsePolarToTheta(float a, float b, float alpha) {
		boolean neg = alpha < 0;
		if (neg)
			alpha = -alpha;
		float rho = alpha % PApplet.TAU;

		float t = (float) Math.atan(Math.tan(rho) * a / b);

		// Adjust atan limits to map t to (0, TAU)
		if (rho >= PApplet.HALF_PI && rho <= 1.5 * PApplet.PI) {
			t += PApplet.PI;
		} else if (rho > 1.5 * PApplet.PI) {
			t = PApplet.TAU + t;
		}

		// Fix quadrants
		if (alpha > PApplet.TAU) {
			t += alpha - rho;
		}
		if (neg) {
			t = -t;
		}

		return t;
	}

	/**
	 * Internal implementation of beginShape+vertex+endShape. Will always draw a
	 * POLYGON, with OPEN/CLOSE option.
	 */
	protected void beginShapeImpl() {
		this.vertexCountImpl = 0;
	}

	/**
	 * Internal implementation of vertex(). Will always draw a POLYGON, with
	 * OPEN/CLOSE option.
	 * 
	 * @param x
	 * @param y
	 */
	protected void vertexImpl(float x, float y) {
		if (vertexCountImpl == verticesImpl.length) {
			float temp[][] = new float[vertexCountImpl << 1][VERTEX_FIELD_COUNT];
			System.arraycopy(verticesImpl, 0, temp, 0, vertexCountImpl);
			verticesImpl = temp;
		}

		verticesImpl[vertexCountImpl][PApplet.X] = x;
		verticesImpl[vertexCountImpl][PApplet.Y] = y;

		vertexCountImpl++;
	}

	/**
	 * Internal implementation of endShape(). Will always draw a POLYGON, with
	 * OPEN/CLOSE option.
	 * 
	 * @param mode
	 */
	protected void endShapeImpl(int mode) {
		if (mode == PApplet.CLOSE) {
			this.vertexImpl(verticesImpl[0][PApplet.X], verticesImpl[0][PApplet.Y]);
		}

		// Draw the fill according to current params
		if (g.fill == true) {
			p.pushStyle();
			p.noStroke();
			p.beginShape(PApplet.POLYGON);
			for (int i = 0; i < vertexCountImpl; i++) {
				p.vertex(verticesImpl[i][PApplet.X], verticesImpl[i][PApplet.Y]);
			}
			p.endShape(mode);
			p.popStyle();
		}

		// Some inits
		int id = 0;
		float run = 0;
		float t = 0;
		float len = 0; // the length of the segment the dash is currently on
		boolean startDash = true; // should a new dash be generated?
		float dx, dy;

		// Tests for forming a corner with the last + first dash;
		boolean corner = false;
		boolean computedCorner = false;
		float cEndX = 0, cEndY = 0;

		if (vertexCountImpl > 1) {
			p.pushStyle();
			p.noFill();

			// If there is ofsset, precompute first t
			if (offset != 0) {
				run += offset;

				// Adjust run to be less than one dashPatternLength behind 0
				if (run > 0) {
					run -= dashPatternLength * (1 + (int) (offset / dashPatternLength));
				} else {
					// Note offset is negative, so adding positive increment
					run -= dashPatternLength * (int) (offset / dashPatternLength);
				}

				// Now process the chunk before t = 0
				while (run < 0) {
					run += dashPattern[id % dashPattern.length];
					id++;
					// If past t = 0 and at the end point of a dash, add first vertex
					if (run >= 0 && id % 2 == 1) {
						if (mode == PApplet.CLOSE) {
							corner = true;
						} else {
							p.beginShape();
							p.vertex(verticesImpl[0][PApplet.X], verticesImpl[0][PApplet.Y]);
						}
						startDash = false;
					}
				}
			}

			// Go over all segments with no return
			for (int i = 0; i < vertexCountImpl - 1; i++) {
				dx = verticesImpl[i + 1][PApplet.X] - verticesImpl[i][PApplet.X];
				dy = verticesImpl[i + 1][PApplet.Y] - verticesImpl[i][PApplet.Y];

				len = PApplet.dist(verticesImpl[i][PApplet.X], verticesImpl[i][PApplet.Y],
						verticesImpl[i + 1][PApplet.X], verticesImpl[i + 1][PApplet.Y]);

				while (run < len) {
					if (startDash) {
						p.beginShape();
						startDash = false;
					}

					t = run / len;
					if (corner && !computedCorner) {
						cEndX = verticesImpl[i][PApplet.X] + t * dx;
						cEndY = verticesImpl[i][PApplet.Y] + t * dy;
						computedCorner = true;
						startDash = true;
					} else {
						p.vertex(verticesImpl[i][PApplet.X] + t * dx, verticesImpl[i][PApplet.Y] + t * dy);
					}
					run += dashPattern[id % dashPattern.length];
					id++;

					if (id % 2 == 0) {
						p.endShape();
						startDash = true;
					}

				}

				// Already past the segment length, prepare to jump to next segment:				
				// If dash was unfinished, add corner kink
				if (id % 2 == 1) {
					p.vertex(verticesImpl[i + 1][PApplet.X], verticesImpl[i + 1][PApplet.Y]);

					// If on last segment, finish drawing
					if (i == vertexCountImpl - 2) {
						// Add last to first dash if necessary
						if (corner) {
							p.vertex(cEndX, cEndY);
						}
						p.endShape();
					}

					// Don't leave me hanging with an initial dash pending...
				} else if (i == vertexCountImpl - 2 && corner) {
					p.beginShape();
					p.vertex(verticesImpl[0][PApplet.X], verticesImpl[0][PApplet.Y]);
					p.vertex(cEndX, cEndY);
					p.endShape();
				}

				// Reposition run
				run -= len;
			}

			// Done drawing! :0
			p.popStyle();
		}
	}

	/**
	 * Given the xy coordinates of the four defpoints of a cubic Bézier curve,
	 * and the parameter t of a point along it, returns the cartesian
	 * coordinates of that point.
	 * 
	 * @param t
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @return
	 */
	protected PVector pointOnCubicBezier(float t, float x1, float y1, float x2, float y2, float x3, float y3, float x4,
			float y4) {
		float t2 = t * t;
		float t3 = t2 * t;
		float mt = 1 - t;
		float mt2 = mt * mt;
		float mt3 = mt2 * mt;

		float x = mt3 * x1 + 3 * mt2 * t * x2 + 3 * mt * t2 * x3 + t3 * x4;
		float y = mt3 * y1 + 3 * mt2 * t * y2 + 3 * mt * t2 * y3 + t3 * y4;

		return new PVector(x, y);
	}


	/**
	 * Given the xy coordinates of the four defpoints of a cubic Bézier curve,
	 * and the parameters a and b along the curve at which to split it, draws a
	 * new subcurve between them.
	 * 
	 * @param a
	 * @param b
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 */
	protected void subCubicBezier(float a, float b, float x1, float y1, float x2, float y2, float x3, float y3,
			float x4, float y4) {
		if (a > b) {
			float tmp = a;
			a = b;
			b = tmp;
		}

		float a2 = a * a;
		float a3 = a2 * a;
		float ma = a - 1;
		float ma2 = ma * ma;
		float ma3 = ma2 * ma;
		float b2 = b * b;
		float b3 = b2 * b;
		float mb = b - 1;
		float mb2 = mb * mb;
		float mb3 = mb2 * mb;
		float ab = a * b;

		float sx1, sy1, sx2, sy2, sx3, sy3, sx4, sy4;

		sx1 = -ma3 * x1 + 3 * a * ma2 * x2 - 3 * a2 * ma * x3 + a3 * x4;
		sy1 = -ma3 * y1 + 3 * a * ma2 * y2 - 3 * a2 * ma * y3 + a3 * y4;

		sx2 = -1 * ma2 * mb * x1 + ma * (3 * ab - 2 * a - b) * x2 + a * (-3 * ab + a + 2 * b) * x3 + a2 * b * x4;
		sy2 = -1 * ma2 * mb * y1 + ma * (3 * ab - 2 * a - b) * y2 + a * (-3 * ab + a + 2 * b) * y3 + a2 * b * y4;

		sx3 = -1 * ma * mb2 * x1 + mb * (3 * ab - a - 2 * b) * x2 + b * (-3 * ab + 2 * a + b) * x3 + a * b2 * x4;
		sy3 = -1 * ma * mb2 * y1 + mb * (3 * ab - a - 2 * b) * y2 + b * (-3 * ab + 2 * a + b) * y3 + a * b2 * y4;

		sx4 = -mb3 * x1 + 3 * b * mb2 * x2 - 3 * b2 * mb * x3 + b3 * x4;
		sy4 = -mb3 * y1 + 3 * b * mb2 * y2 - 3 * b2 * mb * y3 + b3 * y4;

		p.bezier(sx1, sy1, sx2, sy2, sx3, sy3, sx4, sy4);
	}








	//	██╗   ██╗███╗   ██╗██╗   ██╗███████╗███████╗██████╗ ██████╗ 
	//	██║   ██║████╗  ██║██║   ██║██╔════╝██╔════╝██╔══██╗╚════██╗
	//	██║   ██║██╔██╗ ██║██║   ██║███████╗█████╗  ██║  ██║  ▄███╔╝
	//	██║   ██║██║╚██╗██║██║   ██║╚════██║██╔══╝  ██║  ██║  ▀▀══╝ 
	//	╚██████╔╝██║ ╚████║╚██████╔╝███████║███████╗██████╔╝  ██╗   
	//	 ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝╚══════╝╚═════╝   ╚═╝   
	/**
	 * Given a & b as the major and minor semi-axes, and alpha [0, TAU] as the
	 * polar angle of the point, returns the corresponding PVector point on the
	 * ellipse.
	 * 
	 * @param x
	 * @param y
	 * @param a
	 * @param b
	 * @param alpha
	 * @return
	 */
	protected PVector pointOnEllipseAtAngle(float x, float y, float a, float b, float alpha) {
		return pointOnEllipseAtParameter(x, y, a, b, this.ellipsePolarToTheta(a, b, alpha));
	}

	/**
	 * Returns the parameters resulting of diving the ellipse in n equal-length
	 * arcs.
	 * 
	 * @param a
	 * @param b
	 * @param n
	 * @param precision
	 * @return
	 */
	protected float[] divideEllipse(float a, float b, int n, float precision) {
		// Housekeeping
		if (a < 0)
			a = -a;
		if (b < 0)
			b = -b;

		float[] ts = new float[n];
		int id = 0;
		float run = 0;
		float t = 0;
		float dt = precision;
		float samples = Math.round(PApplet.TAU / dt);
		float dn = 0;
		float len = ellipseCircumference(a, b, 0, 0.001f);

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
		return ts;
	}

	/**
	 * Given the xy coordinates of the three defpoints of a quadratic Bézier
	 * curve, and the parameter t of a point along it, returns the cartesian
	 * coordinates of that point.
	 * 
	 * @param t
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return
	 */
	protected PVector pointOnQuadraticBezier(float t, float x1, float y1, float x2, float y2, float x3, float y3) {
		float t2 = t * t;
		float mt = 1 - t;
		float mt2 = mt * mt;

		float x = mt2 * x1 + 2 * mt * t * x2 + t2 * x3;
		float y = mt2 * y1 + 2 * mt * t * y2 + t2 * y3;

		return new PVector(x, y);
	}

	/**
	 * Given the xy coordinates of the three defpoints of a quadratic Bézier
	 * curve, and the parameters a and b along the curve at which to split it,
	 * draws a new subcurve between them.
	 * 
	 * @param a
	 * @param b
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 */
	protected void subQuadraticBezier(float a, float b, float x1, float y1, float x2, float y2, float x3, float y3) {
		if (a > b) {
			float tmp = a;
			a = b;
			b = tmp;
		}

		float a2 = a * a;
		float b2 = b * b;
		float ma = a - 1;
		float mb = b - 1;
		float ab = a * b;

		float sx1, sy1, sx2, sy2, sx3, sy3;
		sx1 = ma * ma * x1 - 2 * a * ma * x2 + a2 * x3;
		sy1 = ma * ma * y1 - 2 * a * ma * y2 + a2 * y3;
		sx2 = ma * mb * x1 + (a + b - 2 * ab) * x2 + ab * x3;
		sy2 = ma * mb * y1 + (a + b - 2 * ab) * y2 + ab * y3;
		sx3 = mb * mb * x1 - 2 * b * mb * x2 + b2 * x3;
		sy3 = mb * mb * y1 - 2 * b * mb * y2 + b2 * y3;

		p.beginShape();
		p.vertex(sx1, sy1);
		p.quadraticVertex(sx2, sy2, sx3, sy3);
		p.endShape();
	}


	/**
	 * Computes the arc length of a cubic Bézier curve.
	 * 
	 * @param t
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @param x4
	 * @param y4
	 * @return
	 */
	protected float cubicBezierArcLength(float t, float x1, float y1, float x2, float y2, float x3, float y3, float x4,
			float y4) {

		//		// Gravesen method from http://steve.hollasch.net/cgindex/curves/cbezarclen.html
		//		float l1 = p.dist(x1, y1, x2, y2) + p.dist(x2, y2, x3, y3) + p.dist(x3, y3, x4, y4);
		//		float l0 = p.dist(x1, y1, x4, y4);
		//		float l = 0.5f * l1 + 0.5f * l0;
		//		float err = l1 - l0;
		//		log("len: " + l + " err: " + err);

		//		// From paper-core.js (no idea where this derivation comes from)
		//		// Doesn't really work that well either
		//		float ax, bx, cx, ay, by, cy, dx, dy;
		//		ax = 9 * (x2 - x3) + 3 * (x4 - x1);
		//		bx = 6 * (x1 + x3) - 12 * x2;
		//		cx = 3 * (x2 - x1);
		//		ay = 9 * (y2 - y3) + 3 * (y4 - y1);
		//		by = 6 * (y1 + y3) - 12 * y2;
		//		cy = 3 * (y2 - y1);
		//		dx = (ax * t + bx) * t + cx;
		//		dy = (ay * t + by) * t + cy;
		//		float len = (float) Math.sqrt(dx * dx + dy * dy);		
		//		log(len);

		// Differential length calculation (expensive)
		float len = 0;
		float T = 0;
		float dt = t / 16;
		PVector pv = new PVector(x1, y1);
		PVector pt;

		for (int i = 0; i < 16; i++) {
			T += dt;
			pt = this.pointOnCubicBezier(T, x1, y1, x2, y2, x3, y3, x4, y4);
			len += PApplet.dist(pv.x, pv.y, pt.x, pt.y);
			pv = pt;
		}

		return len;
	}

}
