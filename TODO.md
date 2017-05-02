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
    - [ ] dash.vertex()
    - [ ] properly continue dashes over kinks with native corners

- [ ] dash.bezierVertex()
- [ ] dash.curveVertex()

- [ ] Implement dash.mode()
    - [ ] Design which modes are there and how they work  

- [ ] dash.bezier()
- [ ] dash.curve()


## SOON
- [ ] At some point, implement 'offset' or something so that animations like this are possible: https://www.youtube.com/watch?v=8uZgU3f8p9A
- [ ] Respond to this: https://forum.processing.org/two/discussion/comment/93993/#Comment_93993

## LONG RUN
- [ ] Review JAVA2D's way of doing dashed lines natively: 
    * https://github.com/processing/processing/issues/4207
    * https://docs.oracle.com/javase/8/javafx/api/javafx/scene/canvas/GraphicsContext.html#setLineDashes-double...-

