# TODO LIST

## IMMEDIATE
- [x] Implement variable dash pattern on all methods
- [x] Rename dashes to dashPattern
- [x] Implement CHORD + PIE modes on arcs...

- [x] Develop this into an actual library

- [s] Implement dash.offset()
    - [x] line
    - [x] rect
    - [x] quad
    - [x] triangle
    - [x] ellipse
    - [x] arc
    - [x] arcPolar

- [ ] dash.beginShape(), dash.endShape()
    - [x] dash.vertex()
    - [x] properly continue dashes over kinks with native corners
    - [x] Add CLOSE to endShape
    - [x] When CLOSING a POLYGON, the first/last vertex doesn't form a corner if the dash is continuous. It shouldn't be hard to do. 
    - [x] Add working MODES to beginShape() (take them from PGraphicsFX2D implementation...)
    - [x] Migrate all linear geometry to shape + vertices (it gets to Processing's core faster, and allows for dashes to bend on corners).
    - [x] Since most dashed lines won't likely be filled, add "if (g.fill == true)" to prefill on all shapes 

- Good Bezier refs:
    + http://www.planetclegg.com/projects/WarpingTextToSplines.html
    + https://pomax.github.io/bezierinfo/
    + https://gamedev.stackexchange.com/questions/5373/moving-ships-between-two-planets-along-a-bezier-missing-some-equations-for-acce/5427#5427

- [ ] dash.bezier()
    - [ ] Try out Pomax' Gauss-Lagrande approximation for arc length
    - [ ] dash.bezierVertex()


- [ ] dash.curve()
    - [ ] dash.curveVertex()


- [ ] Implement dash.mode()
    - [ ] Design which modes are there and how they work  



## SOON
- [x] At some point, implement 'offset' or something so that animations like this are possible: https://www.youtube.com/watch?v=8uZgU3f8p9A
- [ ] Respond to this: https://forum.processing.org/two/discussion/comment/93993/#Comment_93993
- [ ] Expand methods to 3D
- [ ] Add rounded corners to rect() (need quadraticVertex implementation, which needs bezierVertex impl...)
- [ ] When drawing shapes with continuous primitives (TRIANGLES, QUADS, STRIPS), newer fills overlap previous strokes because of drawing order. Figure out a way to fix this by buffering styles?
- [ ] Fork https://pomax.github.io/bezierinfo/ and add:
    - [ ] General matrix solution for curve splitting between [a, b]

## LONG RUN
- [ ] Review JAVA2D's way of doing dashed lines natively: 
    * https://github.com/processing/processing/issues/4207
    * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/GraphicsContext.html#setLineDashes-double...-


## NOTES:
- Matrix multiplication notation in WAplha:
    {{1,0,0},{1,0.5,0},{1,1,1}}.{{1,x,x^2},{0,(1-x),(2*x*(1-x))},{0,0,(1-x)^2}}.{{1,0,0},{-2,2,0},{1,-2,1}}