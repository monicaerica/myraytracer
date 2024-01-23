KoMoPath - A Kotlin Raytracer
================================
KoMoPath (Kotlin Montecarlo Pathtracer) is an open source software that solves the rendering equation in order to produce photorealistic images given the description of a scene (lights, three dimensional shapes etc.)

The description of the scene is provided through a text file written in a "programming language" written in Kotlin, which, through a specific selection of keywords, related to the objects, allows the user to define a scene they want to render.

An example of what can be done, with the help of a python script to automate some passages, is in the animation below, where 20 steps from a molecular dynamics simulation of a gold nanoparticle (done in [LAMMPS](https://github.com/lammps/lammps)) was rendered using the path tracing algorithm.

<img src="https://github.com/monicaerica/myraytracer/assets/54890365/4ce85c70-e4b8-4652-bff0-f66857604f37" width="200" style="float: right;">


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
### Diffusive
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

### Materials
Materials are defined before being applied to objects, you can specify the BRDF, the base pigment and the emissive pigment.
For example:
> material floor_material(diffuse(checkered(<0, 0, 0>, <1, 1, 1>, 1)), uniform(<0, 0, 0>))

defines a material called *floor_material* which has a diffusive behaviour, a checkered pigment, black <0, 0, 0> and white <1, 1, 1>, the emissivity is black, that is the material is not emissive

>material ceiling_material(diffuse(uniform(<0.5, 0.5, 0.5>)), uniform(<0.7, 0.7, 0.7>))

defines an emissive material called *ceiling_material*, the material is gray and has a gray emission.
As an example of a metal material we have:

> material sphere_ref(metal(uniform(<0.5, 0.5, 0.5>), 0.5), uniform(<0, 0, 0>))

a material which has a gray color and a fuzz of 0.5, giving it a somewhat reflective and diffusive behaviour.


 ### Objects

There are currently three different objects supported by the code: spheres, planes and triangles. Shapes can  be assigned a series of transformations to change their position, shape, size and orientation, transformations are combined by using the * symbol, for instance:
>sphere(light_sphere, translation([-1, 0, 1])*scaling([0.7, 0.7, 0.7]))

will create a sphere translated by the vector [-1, 0, 1] and scaled to 70% of its original size along all three axes.
#### Sphere
A sphere is defined as a unit sphere at the origin which can be moved, rotated and rescaled along the three axes by applying the *translation*, *scale* and *rotation* trasformations, for instance:
>sphere(light_sphere, translation([-1, 0, 1])*scaling([0.7, 0.7, 0.7]))

Note that giving different values to the three components of a scaling transformation will result in an oblong ellipsoid.
#### Plane
A plane is an infinetely spanning plane, moved in the scene from the origin using transformations. For instance, the following line:
>plane(sky_material,translation([0, 0, 20]))

creates a plane with an emissive *sky_material* moved 20 units in the positive z direction, acting thus as an emissive sky illuminating the scene
#### Triangle
Triangles differ from spheres and planes as they are not defined by trasformations of a primitive object, rather they are defined by specifying the coordinates of its verteces, for instance:
>triangle(tri_mat, <<-5, -15, -5>, <-5, -5, -5>, <-10, -10, 5>>, scaling([1, 1, 1]))

the coordinates are given between two angled braces with the components enclosed between two angled braces. The *scaling([1, 1, 1])* part is used to prevent errors while reading the input file.

## Example Scenes
A few example scenes are provided to showcase the features of the code

### Scene 1
Scene 1 is composed of a number of diffusive spheres with random scaling, displacement and color generated using a simple python code:
```python
def generate_spheres(N):
    for i in range(N):
        scale = random.uniform(0.2,4)
        trans_x = random.uniform(-10,-25)
        trans_y = random.uniform(-15,15)
        trans_z = random.uniform(-15,15)
        r = random.uniform(0,1)
        g = random.uniform(0,1)
        b = random.uniform(0,1)
        mat = "material mat_{} (diffuse(uniform(<{:.1f},{:.1f},{:.1f}>)), uniform(<0,0,0>))".format(i, r, g, b)
        print(mat)
        print("sphere(mat_{}, translation([{:.1f}, {:.1f}, {:.1f}])*scaling([{:.1f},{:.1f},{:.1f}]))".format(i,trans_x,trans_y,trans_z,scale, scale,scale))
```
plus a reflective red sphere, a big emissive sphere acting as a sky enclosing the scene and a checkered plane as a floor.
This is the input scene descriptor file
```
material plane_material(diffuse(checkered(<1, 1, 1>, <1, 0, 0>, 1)), uniform(<0.0, 0.0, 0.0>))

material sky_material(diffuse(uniform(<	67.8, 84.7, 90.2>)), uniform(<	1, 2, 1>))

material spec_sphere(specular(uniform(<0.8,0.2,0>)), uniform(<0,0,0>))
material tri_mat(metal(uniform(<0.5, 0.5, 0.5>), 0.03), uniform(<0., 0., 0.>))
material light(diffuse(uniform(<0,0,0>)), uniform(<10, 10, 10>))

plane(plane_material, translation([0, 0, -10]))

sphere(sky_material, scaling([50,50,50]))
sphere(spec_sphere, translation([-15, 3, -10])*scaling([5,5,5]))
	
material mat_0 (diffuse(uniform(<0.1,0.2,0.5>)), uniform(<0,0,0>))
sphere(mat_0, translation([-17.4, -6.5, -9.0])*scaling([3.0,3.0,3.0]))
material mat_1 (diffuse(uniform(<0.4,0.4,0.2>)), uniform(<0,0,0>))
sphere(mat_1, translation([-20.2, 8.3, -3.6])*scaling([1.8,1.8,1.8]))
material mat_2 (diffuse(uniform(<1.0,0.4,0.8>)), uniform(<0,0,0>))
sphere(mat_2, translation([-21.1, 10.0, 11.6])*scaling([3.1,3.1,3.1]))
material mat_3 (diffuse(uniform(<0.8,1.0,0.1>)), uniform(<0,0,0>))
sphere(mat_3, translation([-20.3, 13.9, -13.2])*scaling([2.2,2.2,2.2]))
material mat_4 (diffuse(uniform(<0.1,0.3,0.9>)), uniform(<0,0,0>))
sphere(mat_4, translation([-13.4, -9.5, 8.5])*scaling([2.0,2.0,2.0]))
material mat_5 (diffuse(uniform(<0.0,0.5,0.3>)), uniform(<0,0,0>))
sphere(mat_5, translation([-11.0, -1.4, 13.1])*scaling([3.3,3.3,3.3]))
material mat_6 (diffuse(uniform(<0.7,0.3,0.3>)), uniform(<0,0,0>))
sphere(mat_6, translation([-16.6, -9.2, -1.3])*scaling([3.7,3.7,3.7]))
material mat_7 (diffuse(uniform(<0.9,0.5,0.1>)), uniform(<0,0,0>))
sphere(mat_7, translation([-10.6, -12.9, 6.5])*scaling([2.4,2.4,2.4]))
material mat_8 (diffuse(uniform(<0.4,0.4,1.0>)), uniform(<0,0,0>))
sphere(mat_8, translation([-23.4, -8.0, -3.6])*scaling([1.2,1.2,1.2]))
material mat_9 (diffuse(uniform(<0.7,0.8,0.3>)), uniform(<0,0,0>))
sphere(mat_9, translation([-11.8, 11.2, -0.4])*scaling([1.9,1.9,1.9]))

camera(perspective, rotationz(225)*translation([-3, 0, 0]), 1.0, 0.9) 	
```
And this is the output

<img src="https://github.com/monicaerica/myraytracer/assets/54890365/8f7fd6f9-e9ba-4c9d-a2ae-081ffe9cb29e" width="200" style="float: right;">

The same scene with more spheres and from a different perspective

<img src="https://github.com/monicaerica/myraytracer/assets/54890365/66c11d34-b28a-4057-840e-a207faf05aa5" width="200" style="float: right;">


### Scene 2
Scene 2 contains two metal triangles with a fuzzy reflection, 6 fuzzy metal spheres with the colours of the italian flag and a checkered uniform sphere plus the same big emissive sphere and checkered floor as in scene 1:
```
material plane_material(diffuse(checkered(<1, 1, 1>, <1, 0, 0>, 1)), uniform(<0.0, 0.0, 0.0>))
material check_material(diffuse(checkered(<0, 0, 1>, <1, 0, 0>, 10)), uniform(<0.0, 0.0, 0.0>))

material big_sphere(metal(uniform(<10, 5, 0>), 0.5), uniform(<1., 1., 1.>))
material light_sphere_g(metal(uniform(<0., 10., 0.>), 0.2), uniform(<0., 0., 0.>))
material light_sphere_w(metal(uniform(<10., 10., 10.>), 0.2), uniform(<0., 0., 0.>))
material light_sphere_r(metal(uniform(<10., 0., 0.>), 0.2), uniform(<0., 0., 0.>))
material tri_mat(metal(uniform(<0.5, 0.5, 0.5>), 0.03), uniform(<0., 0., 0.>))


sphere(big_sphere, translation([-5, 0, -5])*scaling([50, 50, 50]))

sphere(light_sphere_g, translation([-5, 0, -4])*scaling([1, 1, 1]))
sphere(light_sphere_g, translation([-5, 0, -2])*scaling([1, 1, 1]))

sphere(light_sphere_w, translation([-5, 0, 0])*scaling([1, 1, 1]))
sphere(light_sphere_w, translation([-5, 0, 2])*scaling([1, 1, 1]))

sphere(light_sphere_r, translation([-5, 0, 4])*scaling([1, 1, 1]))
sphere(light_sphere_r, translation([-5, 0, 6])*scaling([1, 1, 1]))	


sphere(check_material, translation([-6, 5, 0])*scaling([0.5, 0.5, 0.5]))
sphere(check_material, translation([-2, -2, 0])*scaling([2, 2, 2]))


triangle(tri_mat, <<-15, -15, -5>, <-15, 15, -5>, <-10, 0, 10>>, scaling([1, 1, 1]))
triangle(tri_mat, <<-15, -15, -5>, <15, -15, -5>, <-10, 0, 10>>, scaling([1, 1, 1]))

plane(plane_material,translation([0, 0, -5]))

camera(perspective, rotationz(225)*translation([-3, 0, 0]), 1.0, 0.9) 	
```

And the output

<img src="https://github.com/monicaerica/myraytracer/assets/54890365/9afb16f8-c650-4e9d-820f-378d9b7efb7e" width="200" style="float: right;">

## Running The Code

The code is run via the command-line, in order to run it you require an input file as the ones in the examples above. To get a quick documentation about the different parameters you can pass the command you can run:
```bash
komopath render --h
```
This will result in the following screen being printed:
```bash
Usage: my-ratracer render [OPTIONS]

Options:
  -inf, --infile TEXT      Name of the file containing the description of the
                           scene to be rendered
  -a, --algorithm INT      The algorithm used to render the scene, use 1 for
                           on off rendering (only for quick positioning
                           checks), 2 for the flat renderer, 3 for the path
                           tracer. Default: 3
  -f, --fname TEXT         Filename into which to save the resulting image
  -g, --gamma FLOAT        Value of the gamma for RGB -> sRGB, default = 1
  --width, --w INT         Image width in pixels (default = 640px)
  --height, --h INT        Image height in pixels (default = 480px)
  --numray, --nr INT       NUmber of rays used in the pathtracing algorithm
  --samperside, --sps INT  Number of samples per pixel
  --maxdepht, --md INT     Max number of times the ray has been scattered
  -h, --help               Show this message and exit
```
Note that the only arguments required are the input file name and the output file name, the others, if not given, will resort to their default values.
For instance, to render *scene 2* using the path tracer with 10 secondary rays and 3 reflections in 1080p and saving it to a file named scene2.png using a gamma correction of 0.7:
```bash
komopath render --infile scena2 --fname scene2.png --w 1920 --h 1080 --nr 10 --md 3 --a 3 --gamma 0.7
```
