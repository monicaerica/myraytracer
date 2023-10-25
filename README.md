MyRaytracer - A Kotlin Raytracer
================================
Myraytracer (temporary name) is an open source software that solves the rendering equation in order to produce photorealistic images given the description of a scene (lights, three dimensional shapes etc.)

The description of the scene is provided through a text file written in a "programming language" written in Kotlin, which, through a specific selection of keywords, related to the objects, allows the user to define a scene they want to render.

The rendering algorithm itself can be varied according to one's necessities, we suggest, given the long time required to render a whole scene using the photorealistic path tracing algorithm that you use the flat renderer to check if the scene is defined according to your needs, then, once you are ok with what you see, proceed with the path tracer.

The available algorithms for rendering are the following: 
- On-Off Rendering: The simpler renderer provided, it just provide a white pixel on the screen if the ray hits an object, otherwise the pixel is left black
- Flat Rendering: A step above the flat renderer, if a ray hits an object, then the corresponding pixel is set to the color of the object specified in the material
- Path Tracer: The proper raytracer, returns a pixel with a certain color based on the light, other objects proesent on the scene, the type of material...
More on how to use them with the scripting language can be found in the sections below.

## Scene Descriptor
The description of a scene, as mentioned above, is given in the form of a script. In this script the user can insert inside the scene a geometric object, which by default is inserted at the origin, and then operate some transformations on it, such as translations, rotations and scaling.
This operations can be done around one or more of the 3 axes. The user can also create a material which can then be applied to an object.
For instance the following line in the script

sphere(light_sphere, translation([-10, 4, 0])*scaling([0.2, 0.2, 0.2]))

will create a sphere with the light_sphere material atteched to it (to be previously defined), then the sphere is translated -10 units along the x axis, 4 along the y axis and 0 along the z axis and will also be scaled down to 20% of its original size along all axes.

The material is defined