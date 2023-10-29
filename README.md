MyRaytracer - A Kotlin Raytracer
================================
Myraytracer (temporary name) is an open source software that solves the rendering equation in order to produce photorealistic images given the description of a scene (lights, three dimensional shapes etc.)

The description of the scene is provided through a text file written in a "programming language" written in Kotlin, which, through a specific selection of keywords, related to the objects, allows the user to define a scene they want to render.

An example of what can be done, with the help of a python script to automate some passages, is in the animation below, where 20 steps from a molecular dynamics simulation of a gold nanoparticle (done in [LAMMPS](https://github.com/lammps/lammps)) was rendered using the path tracing algorithm.

![Animation](https://github.com/monicaerica/myraytracer/assets/54890365/4ce85c70-e4b8-4652-bff0-f66857604f37)


The rendering algorithm itself can be varied according to one's necessities, we suggest, given the long time required to render a whole scene using the photorealistic path tracing algorithm that you use the flat renderer to check if the scene is defined according to your needs, then, once you are ok with what you see, proceed with the path tracer.

The available algorithms for rendering are the following: 
- On-Off Rendering: The simpler renderer provided, it just provide a white pixel on the screen if the ray hits an object, otherwise the pixel is left black
- Flat Rendering: A step above the flat renderer, if a ray hits an object, then the corresponding pixel is set to the color of the object specified in the material
- Path Tracer: The proper raytracer, returns a pixel with a certain color based on the light, other objects proesent on the scene, the type of material...
More on how to use them with the scripting language can be found in the sections below.

## BRDF
The main ingredient of an object, apart from the transformation applied to its size, position and rotation, is the **B**idirectional **R**eflectance **D**istribution **F**unction, the BRDF for short. This function describes the light leaving an object in a direction given a ray hitting an object from a given direction, basically determining the reflctive properties of the material.
As of right now there are three BRDFs in this raytracer:
* Specular
* Diffusive
* Metal
The first two BRDFs only take a pigment as an argument, the third one also takes a float, the fuzzyness.
### Specular
A simple reflective material, takes a ray coming from a direction and reflects it perfectly using the reflection formula from geometrical optics.
### DIffusive
A diffusive material which, upon being hit by a ray, scatters that ray in a random direction in an emisphere
### Metal
Simulates a behaviour changing between a perfect specular and a diffusive material through the fuzzyness, basically a ray is reflected (as in the specular BRDF) and the reflected is deflected in a random direction, the random "deviation" being multiplied by the fuzzyness, thus if **fuzzy = 0** the system behaves as a perfect reflective material, if **fuzzy = 1** it behaves as a diffusive BRDF. A comparison of the same scene with a sphere with a metal BRDF for increasing fuzzyness is shown below.
![compare_fuzz](https://github.com/monicaerica/myraytracer/assets/54890365/557df9f0-3cba-4c70-ad81-fb23185e7835)


## Scene Descriptor
The description of a scene, as mentioned above, is given in the form of a script. In this script the user can insert inside the scene a geometric object, which by default is inserted at the origin, and then operate some transformations on it, such as translations, rotations and scaling.
This operations can be done around one or more of the 3 axes. The user can also create a material which can then be applied to an object.
For instance the following line in the script

sphere(light_sphere, translation([-10, 4, 0])*scaling([0.2, 0.2, 0.2]))

will create a sphere with the light_sphere material atteched to it (to be previously defined), then the sphere is translated -10 units along the x axis, 4 along the y axis and 0 along the z axis and will also be scaled down to 20% of its original size along all axes.
### Cameras
In order to display a scene a camera is required. A camera is initiated with the **camera** keyword, then, between paranthesis the specifics of the camera can be provided. A camera cn either be perspective or orthogonal, after specifying the type of camera transformations can be applied to it to move it from the origin and change its orientation. Then the aspect ratio is specified, note that, as of the current version (1.0.0) the aspect ratio is not automatically linked to the resolution of the image, thus you have to specify it having in mind what the resolution is going to be, for instance:
> The standard HD resolution: 1280 x 720 requires an AR of 1.777, as with a 1920 x 1080 etc

> A square image requires an AR of 1.0

> If an arbitrary resolution is specified use AR = width / height

Then the next parameter is related to the filed of view.



