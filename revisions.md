## DASHED LINES 0.0.3 (REV 003) - 20 May 2017

Some beautiful curve refinements.

+ `arc()` end parameters are now linearly interpolated, resulting in smoother and nicer arcs.
+ Fixed glitch in `bezier()` that caused it to go crazy on small resolution queries. 
+ Prevent users from entering negative values in `pattern()` and crashing the whole thing. 
+ A bunch of internal optimizations, resulting in faster and cleaner dashes ;)

## DASHED LINES 0.0.2 (REV 002) - 20 May 2017

Fixing noob mistakes.

+ Removed all the annoying development packages from the project.


## DASHED LINES 0.0.1 (REV 001) - 19 May 2017

First version of Dashed Lines for Processing is out! https://github.com/garciadelcastillo/-dashed-lines-for-processing-

Thanks to the Fathom folks for hosting the release party! <3 https://www.instagram.com/p/BUSiL5HAlcC/

+ Where to start? Well, dashed strokes for all 2D primitives, including `arc()`, `ellipse()`, `line()`, `quad()`, `rect()` and `triangle()`. 
+ Also `bezier()` curves are in! This was sooo cool to implement... Thanks [Pomax](https://github.com/Pomax) for the fantastic [Primer](https://pomax.github.io/bezierinfo/). Catmull-Rom curves, aka `curve()` still ont he works. 
+ `beingShape()`, `vertex()` and `endShape()`, including `OPEN`/`CLOSE` modes and shape modes. 
+ `pattern()` allows configuration of dash-gap pattern.
+ `offset()` adds initial offset to dash pattern, allowing for "walking ants" effect on animations. 



