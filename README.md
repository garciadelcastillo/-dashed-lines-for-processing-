# Dashed Lines for Processing

![Dashed Line](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/dashed_line.gif "Dashed Line")

Couldn't be any simpler, just a [Processing](http://processing.org) library to draw geometry with dashed strokes!

### Installation
You can install the library from Processing's Contribution Manager.

Alternatively, you can extract the distribution file on your Processing's sketchbook. Download `dashedlines.zip` from the `dist` folder. Now go to your sketchbook folder (in Windows it will be something like `C:\Users\JohnDoe\Documents\Processing`), go inside `libraries`, and extract the contents of the `.zip` file to a folder called `dashedlines`. Once finished, your library should be found under: `C:\Users\JohnDoe\Documents\Processing\libraries\dashedlines\library\dashedlines.jar`.

Still having trouble? [Read this](https://github.com/processing/processing/wiki/How-to-Install-a-Contributed-Library).

## Hello Dash
Let's take a look at a basic example on how to draw a simple dashed line now:

```java
// Import the library
import garciadelcastillo.dashedlines.*;

// Declare the main DashedLines object
DashedLines dash;

void setup() {
  // Initialize it, passing a reference to the current PApplet
  dash = new DashedLines(this);

  // Set the dash-gap pattern in pixels
  dash.pattern(10, 5);
}

void draw() {
  background(127);

  // Call the line method of the 'dash' object,
  // as if it was Processing's native
  dash.line(10, 10, 90, 90);
  dash.line(10, 90, 90, 10);
}

```

And voilà!

![Hello Dash!](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/hello_dash.png "Hello Dash!")


## Features
Dashed Lines for Processing provides a **Processing-like API** to draw the same basic or complex shapes you would natively, but with **dashed strokes**. It computes stroke segments based on your `pattern` choice, and adapts the drawing to a best-fit situation. This is specially useful for animation:

```java
dash.pattern(20, 10);
dash.line(n1.x, n1.y, n2.x, n2.y);
```

![Dashed Line](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/dashed_line.gif "Dashed Line")

Dashed Lines contains methods to draw ~~all~~ _(under development, gaps here and there)_ types of geometry that you would normally do in Processing. They **inherit Processing's styles**, such as `stroke()`, `fill()`, `strokeWeight()` and shape modes like `rectMode()`. Additionally, it provides some options to **customize the dash-gap `pattern()`** or to add `offset()` to the pattern for **'walking ants' effect** on animations.

For example, for **2D primitives**:

```java
dash.pattern(30, 10, 15, 10);

// Dashed objects inherit Processing's style properties, including shape modes.
fill(255, 0, 0, 100);
rectMode(CORNERS);
dash.rect(n[0].x, n[0].y, n[1].x, n[1].y);

fill(0, 255, 0, 100);
ellipseMode(CORNERS);
dash.ellipse(n[2].x, n[2].y, n[3].x, n[3].y);

fill(0, 0, 255, 100);
dash.triangle(n[4].x, n[4].y, n[5].x, n[5].y, n[6].x, n[6].y);

fill(255, 0, 255, 100);
dash.quad(n[7].x, n[7].y, n[8].x, n[8].y, n[9].x, n[9].y, n[10].x, n[10].y);

// Animate dashes with 'walking ants' effect
dash.offset(dist);
dist += 1;
```
![2D Primitives](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/2d_primitives.gif "2D Primitives")

For **Bézier curves**:

```java
dash.pattern(30, 10);

noFill();
dash.bezier(n[0].x, n[0].y, n[1].x, n[1].y, n[2].x, n[2].y, n[3].x, n[3].y);

dash.offset(dist);
dist += 1;
```
![Bezier Curve](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/bezier_curve.gif "Bezier Curve")

And for more **complex shapes**, you can use the `.beginShape()`, `.vertex()` and `.endShape()` methods:

```java
strokeCap(SQUARE);
strokeJoin(BEVEL);
dash.pattern(30, 10);

// Start the shape with the .beginShape() method
dash.beginShape();
// Add vertices like you would in Processing
for (int i = 0; i < n.length; i++) {
  dash.vertex(n[i].x, n[i].y);
}
// Finish drawing the shape
dash.endShape();

dash.offset(dist);
dist += 1;
```
![Open Shape](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/shape_open.gif "Open Shape")

Or using any of Processing's **shape modes**:

```java
// Shapes accept all the same modes as Processing's native implementation:
fill(255, 0, 0, 100);
dash.beginShape(TRIANGLES);
for (int i = 0; i < n.length; i++) {
  dash.vertex(n[i].x, n[i].y);
}
dash.endShape(CLOSE);
```
![TRIANGLES Shape](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/assets/shape_triangles.gif "TRIANGLES Shape")

## Contribute
There is still [a lot to do](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/blob/master/TODO.md), so if you have some time, and are excited about computational geometry, feel free to fork and contribute, [report bugs](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/issues) or [submit feature requests](https://github.com/garciadelcastillo/-dashed-lines-for-processing-/issues).

Also, if you found this library useful and did something cool with it, send it my way! I am always happy to hear about cool projects people are working on.

## Acknowledgments
My deepest gratitude to all the folks at the [Processing Foundation](https://processing.org/) and the great [community](https://processing.org/reference/libraries/) that make this project so special and awesome.

Thanks to [Nono](https://github.com/nonoesp) for having pushed me to stick my head out of the books and make something useful, for once!

Kuddos to [Pomax](https://github.com/Pomax) for his amazing [Primer on Bézier Curves](https://pomax.github.io/bezierinfo/), it was incredibly helpful for the math behind these curves.
