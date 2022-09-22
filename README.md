MyRaytracer - A Kotlin Raytracer
================================
Myraytracer (temporary name) is an open source software that solves the rendering equation in order to produce photorealistic images given the description of a scene (lights, three dimensional shapes etc.)

The description of the scene is provided through a text file written in a "programming language" written in Kotlin, which, through a specific selection of keywords, related to the objects, allows the user to define a scene they want to render.

The rendering algorithm itself can be varied according to one's necessities, we suggest, given the long time required to render a whole scene using the photorealistic path tracing algorithm that you use the flat renderer to check if the scene is defined according to your needs, then, once you are ok with what you see, proceed with the path tracer.

More details on the rendering algorithms are provided in the wiki, here below is a quick explaination:
- On-Off Rendering: The simpler renderer provided, it just provide a white pixel on the screen if the ray hits an object, otherwise the pixel is left black
- Flat Rendering: A step above the flat renderer, if a ray hits an object, then the corresponding pixel is set to the color of the object specified in the material
- Path Tracer: The proper raytracer, returns a pixel with a certain color based on the light, other objects proesent on the scene, the type of material...
