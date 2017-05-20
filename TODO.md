# TODO LIST

## REV 0003
- [x] Create a protected array for curve parameters, just like the vertices one used for beginShape()
- [x] Change bezier() to the above
- [x] Change arc() to the above
- [x] Add linear interpolation of parameters for arcs/ellipses --> they can now be computed faster! :)
- [x] For default 10-10 pattern, there are a lot of glitches in large beziers. Adjust dt precision based on a super rough approximated length, and/or how small the minimum dash/gap param is?
- [x] Negative values in `pattern()` lead to infinite loops and badness. Prevent.

## REV 0002
- [x] Remove dev packages from project


## REV 00001
- [x] Implement variable dash pattern on all methods
- [x] Rename dashes to dashPattern
- [x] Implement CHORD + PIE modes on arcs...

- [x] Develop this into an actual library

- [x] Implement dash.offset()
    - [x] line
    - [x] rect
    - [x] quad
    - [x] triangle
    - [x] ellipse
    - [x] arc
    - [x] arcPolar

- [x] At some point, implement 'offset' or something so that animations like this are possible: https://www.youtube.com/watch?v=8uZgU3f8p9A

- [x] dash.beginShape(), dash.endShape()
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

- [x] dash.bezier()

- [x] Publication
    - [x] Document methods a la javadoc
    - [x] Rethink package name
    - [x] Write examples
    - [x] Decide where I am going to host this... 
    - [x] Remove dev package from javadoc exports 
    - [ ] and compilation
    - [x] Create a dashedlines.zip + dashedlines.txt (copy of library.properties) 
    - [x] Go over library.properties and make sure things are in the right place
    - [x] Write a nice Readme for the github page
    - [x] Licensing --> MIT


---
## SOON
- [ ] Try out Pomax' Gauss-Lagrande approximation for arc length
- [ ] dash.curve()
- [ ] dash.bezierVertex()
- [ ] dash.curveVertex()
- [ ] Implement dash.mode()
- [ ] Design which modes are there and how they work  
- [ ] Dashes are not continuous in arcs with modes CHORD or PIE. Must figure out how to link growing parameters, and hopefuly the corner stroke into a continuous one with the kink.

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
