package dashedlines;

import processing.core.*;
import processing.data.*;

public class Dasher {

	public final static String VERSION = "##library.prettyVersion##";

	PApplet p;

	/**
	 * Main constructor, pass a reference to the current PApplet
	 * 
	 * @param theParent
	 */
	public Dasher(PApplet theParent) {
		p = theParent;
	}

	////// PRIVATE PROPS //////
	private float CIRCLE_EPSILON = 0.1f;
	private float[] dashPattern = { 10, 10 };

	////// PUBLIC API //////
	public void pattern(float d1) {
		dashPattern = new float[2];
		dashPattern[0] = d1;
		dashPattern[1] = d1;
	}

	public void pattern(float d1, float d2) {
		dashPattern = new float[2];
		dashPattern[0] = d1;
		dashPattern[1] = d2;
	}

	public void pattern(float d1, float d2, float d3, float d4) {
		dashPattern = new float[4];
		dashPattern[0] = d1;
		dashPattern[1] = d2;
		dashPattern[2] = d3;
		dashPattern[3] = d4;
	}

	public void pattern(float[] ds) {
		if (ds.length % 2 == 1) {
			throw new RuntimeException("Please provide an even number of dash-gap lengths on dash(float[])");
		}
		dashPattern = ds;
	}

	public void line(float x1, float y1, float x2, float y2) {
		// Compute theta parameters for start-ends of dashes and gaps
		// TODO: precompute the size of the array and create it as an array
		// directly
		FloatList ts = new FloatList();
		int id = 0;
		float run = 0;
		float t = 0;
		float len = PApplet.dist(x1, y1, x2, y2);

		while (run < len) {
			t = run / len;
			ts.append(t);
			run += dashPattern[id % dashPattern.length];
			id++;
		}

		// If last t was the startpoint of a dash, close it at the end of the
		// line
		if (id % 2 == 1) {
			ts.append(1);
		}

		// DEV
		if (ts.size() % 2 == 1) {
			throw new RuntimeException("t array is size " + ts.size());
		}

		float[] tsA = ts.array(); // TODO: improve the list-array situation

		// Draw dashes
		p.pushStyle();
		float dx = x2 - x1;
		float dy = y2 - y1;
		for (int i = 0; i < tsA.length; i += 2) {
			p.line(x1 + tsA[i] * dx, y1 + tsA[i] * dy, x1 + tsA[i + 1] * dx, y1 + tsA[i + 1] * dy);
		}
		p.popStyle();
	}

	public void rect(float a, float b, float c, float d) {
		int rectMode = p.getGraphics().rectMode;

		// From Processing's core
		float hradius, vradius;
		switch (rectMode) {
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

		// Draw the underlying fill props
		p.pushStyle();
		p.noStroke();
		p.quad(a, b, c, b, c, d, a, d); // since we already did the
										// calculations, quad is faster than
										// rect()
		p.popStyle();

		// Draw rect lines (quick and dirty)
		this.quad(a, b, c, b, c, d, a, d);
	}

	public void quad(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		p.pushStyle();
		p.noStroke();
		p.quad(x1, y1, x2, y2, x3, y3, x4, y4);
		p.popStyle();

		this.line(x1, y1, x2, y2);
		this.line(x2, y2, x3, y3);
		this.line(x3, y3, x4, y4);
		this.line(x4, y4, x1, y1);
	}

	public void triangle(float x1, float y1, float x2, float y2, float x3, float y3) {
		p.pushStyle();
		p.noStroke();
		p.triangle(x1, y1, x2, y2, x3, y3);
		p.popStyle();

		this.line(x1, y1, x2, y2);
		this.line(x2, y2, x3, y3);
		this.line(x3, y3, x1, y1);
	}

	public void ellipse(float a, float b, float c, float d) {
		int ellipseMode = p.getGraphics().ellipseMode;

		// From Processing's core, CORNER-oriented vars
		float x = a;
		float y = b;
		float w = c;
		float h = d;

		if (ellipseMode == PApplet.CORNERS) {
			w = c - a;
			h = d - b;
		} else if (ellipseMode == PApplet.RADIUS) {
			x = a - c;
			y = b - d;
			w = c * 2;
			h = d * 2;
		} else if (ellipseMode == PApplet.DIAMETER) { // == CENTER
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

		// Compute theta parameters for start-ends of dashes and gaps
		FloatList ts = new FloatList(); // TODO: precompute the size of the t
										// array and create it as an array
										// directly
		int id = 0;
		float run = 0;
		float t = 0;
		float dt = 0.01f;
		float samples = Math.round(PApplet.TAU / dt);
		float len = ellipseCircumference(w2, h2, 0, dt);
		float nextL = 0;

		// println("start: " + millis());
		for (int i = 0; i < samples; i++) {
			run += ellipseArcDifferential(w2, h2, t, dt);
			if ((int) run >= nextL) {
				ts.append(t);
				nextL += dashPattern[id % dashPattern.length];
				id++;
			}
			t += dt;
		}
		// println("end: " + millis());
		float[] tsA = ts.array(); // see TODO above

		// Draw the fill part
		p.pushStyle();
		p.noStroke();
		p.ellipseMode(PApplet.CORNER); // all correct vars are already
										// calculated, so why not use them...?
										// :)
		p.ellipse(x, y, w, h);
		p.popStyle();

		// Draw dashes
		p.pushStyle();
		p.noFill();
		p.ellipseMode(PApplet.CORNER);
		for (int i = 0; i < tsA.length; i += 2) {
			if (i == tsA.length - 1) {
				p.arc(x, y, w, h, tsA[i], PApplet.TAU);
			} else {
				p.arc(x, y, w, h, tsA[i], tsA[i + 1]);
			}
		}
		p.popStyle();
	}

	// Create a dashed arc using Processing same function signature
	// (note that start/stop here refer to the THETA parameter, NOT THE POLAR
	// ANGLE)
	public void arc(float a, float b, float c, float d, float start, float stop) {
		this.arc(a, b, c, d, start, stop, 0);
	}

	// Create a dashed arc using Processing same function signature
	// (note that start/stop here refer to the THETA parameter, NOT THE POLAR
	// ANGLE)
	public void arc(float a, float b, float c, float d, float start, float stop, int mode) {
		int ellipseMode = p.getGraphics().ellipseMode;

		// From Processing's core, CORNER-oriented vars
		float x = a;
		float y = b;
		float w = c;
		float h = d;

		if (ellipseMode == PApplet.CORNERS) {
			w = c - a;
			h = d - b;
		} else if (ellipseMode == PApplet.RADIUS) {
			x = a - c;
			y = b - d;
			w = c * 2;
			h = d * 2;
		} else if (ellipseMode == PApplet.DIAMETER) { // == CENTER
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

		// make sure the loop will exit before starting while
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

				// TODO: implement modes: CHORD, PIE

				// Compute theta parameters for start-ends of dashes and gaps
				FloatList ts = new FloatList(); // TODO: precompute the size of
												// the t array and create it as
												// an array directly
				int id = 0;
				float run = 0;
				float t = start;
				float dt = 0.01f;
				float samples = Math.round((stop - start) / dt);
				float len = ellipseCircumference(w2, h2, 0, dt);
				float nextL = 0;

				// println("start: " + millis());
				for (int i = 0; i < samples; i++) {
					run += ellipseArcDifferential(w2, h2, t, dt);
					if ((int) run >= nextL) {
						ts.append(t);
						nextL += dashPattern[id % dashPattern.length];
						id++;
					}
					t += dt;
				}
				// println("end: " + millis());
				float[] tsA = ts.array(); // see TODO above

				// Draw the fill part
				p.pushStyle();
				p.noStroke();
				p.ellipseMode(PApplet.CORNER); // all correct vars are already
												// calculated,
				// so why not use them...? :)
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

	////// PRIVATE METHODS //////
	// Given a & b as the major and minor semi-axes,
	// return a fast approximation to the circumference of the ellipse.
	private float ellipseCircumference(float a, float b) {
		// By default, just rely on Ramanujan's implementation
		return ellipseCircumference(a, b, 0, 0.001f);
	}

	// Given a & b as the major and minor semi-axes,
	// return an approximation to the circumference of the ellipse.
	// Use mode 0 for Ramanujan's fast approximation, or for very low
	// b/a ratios use mode 1 for a more precise (albeit) expensive recursive
	// approximation,
	// or mode 2 for a differential approximation.
	// Precision is the dt increments in theta used for differential
	// approximation.
	// Turns out, ellipses are a little more complicated than circles!
	// There is no closed form equation to find the length (solution is a double
	// elliptic integral),
	// nor the arc length given an angle...
	private float ellipseCircumference(float a, float b, int mode, float precision) {
		// Turns out the exact solution has no closed form, and can only be
		// obtained solving
		// for the complete elliptical integral of the second kind:
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
		// errors
		// ranging from the 6th to the 4th significant digit of the mantissa
		// depending on
		// the 1 > b/a > 0 ratio) or rely on an expensive yet preciser recursive
		// approximation:
		switch (mode) {
		// Recursive approximation
		// https://math.stackexchange.com/a/73504/440507
		case 1:
			float f = 1, s, v = 0.5F * (1 + b / a), w;
			w = (1 - (b / a) * (b / a)) / (4 * v);
			s = v * v;
			// int it = 0;
			while (true) {
				v = 0.5f * (v + (float) Math.sqrt((v - w) * (v + w)));
				w = (0.5f * w) * (0.5f * w) / v;
				f *= 2;
				s -= f * w * w;
				// if (abs(w) < EPSILON || it > 100) {
				if (Math.abs(w) < PApplet.EPSILON) {
					// println("it: " + it);
					break;
				}
				// it++;
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

	// Given a & b as the major and minor semi-axes,
	// startT and endT as theta parameters (not angle) to measure between,
	// and precision and the dt (theta increment) to be used in calculations,
	// returns a lower-bound approximation to the arc length based on
	// differential approximation.
	private float ellipseArcLength(float a, float b, float startT, float endT, float precision) {
		// Similarly to the circumference, tThe exact solution to the arc length
		// of an ellipse
		// can only be obtained solving for the complete elliptical integral of
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

	// Calculate the differential arc length at a given parameter t with given
	// dt
	private double ellipseArcDifferential(double a, double b, double t, double dt) {
		double as2 = Math.pow(a * Math.sin(t), 2.0);
		double bc2 = Math.pow(b * Math.cos(t), 2.0);
		return dt * Math.sqrt(as2 + bc2);
	}

	/////////////////// UNUSED BUT POTENTIALLY USEFUL? ////////////////////

	// Given a & b as the major and minor semi-axes,
	// and t [0, TAU] as the parameter along the ellipse (not the polar angle of
	// the point),
	// returns the corresponding PVector point on the ellipse.
	private PVector pointOnEllipseAtParameter(float x, float y, float a, float b, float t) {
		float px = a * (float)Math.cos(t);
		float py = b * (float)Math.sin(t);
		return new PVector(x + px, y + py);
	}

	// Given a & b as the major and minor semi-axes,
	// and alpha [0, TAU] as the polar angle of the point,
	// returns the corresponding PVector point on the ellipse.
	private PVector pointOnEllipseAtAngle(float x, float y, float a, float b, float alpha) {
//		// Constrain angle
//		alpha %= PApplet.TAU;
//
//		// Convert polar angle to parameter
//		float t = (float)Math.atan(Math.tan(alpha) * a / b);
//
//		// Adjust atan limits to map t to (0, TAU)
//		if (alpha >= PApplet.HALF_PI && alpha <= 1.5 * PApplet.PI) {
//			t += PApplet.PI;
//		} else if (alpha > 1.5 * PApplet.PI) {
//			t = PApplet.TAU + t;
//		}

		return pointOnEllipseAtParameter(x, y, a, b, ellipsePolarToTheta(a, b, alpha));
	}

	// Returns the parameters resulting of diving the ellipse in n equal-length
	// arcs.
	private float[] divideEllipse(float a, float b, int n, float precision) {
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

		// println("start: " + millis());
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
		// println("end: " + millis());

		return ts;
	}

	private float ellipsePolarToTheta(float a, float b, float alpha) {
		boolean neg = alpha < 0;
		if (neg)
			alpha = -alpha;
		float rho = alpha % PApplet.TAU;

		float t = (float)Math.atan(Math.tan(rho) * a / b);

		// Adjust atan limits to map t to (0, TAU)
		if (rho >= PApplet.HALF_PI && rho <= 1.5 * PApplet.PI) {
			t += PApplet.PI;
		} else if (rho > 1.5 * PApplet.PI) {
			t = PApplet.TAU + t;
		}

		// fix quadrants
		if (alpha > PApplet.TAU) {
			t += alpha - rho;
		}
		if (neg) {
			t = -t;
		}

		return t;
	}

}
