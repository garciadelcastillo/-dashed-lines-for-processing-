
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