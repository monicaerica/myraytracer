import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int
import java.awt.image.BufferedImage
import java.io.FileInputStream
import javax.imageio.ImageIO
import kotlin.math.PI
import kotlin.math.atan

class myRatracer(): CliktCommand(){
    override fun run() = Unit
}

class Demo: CliktCommand(name = "demo"){
    private val width: Int by option("--width", "--w", help = "Image width in pixels (default = 640px)").int().default(640)
    private val height: Int by option("--height", "--h", help = "Image height in pixels (default = 480px)").int().default(480)
    private val rotation: Float by option ("--rotation", "--r", help="Rotation of the observer around z axis in degrees").float().default(0.0f)
    private val camheight: Float by option ("--camheight", "--ch", help = "Height of the camera").float().default(0.0f)
    private val algorithm: Int by option ("--algorithm", "--a", help = "Select the algorithm you wish to use to render the image: \n 1- OnOffRender\n 2- FlatRender \n 3 - Path Tracing").int().default(1)
    private val fname by option("--fname", "-f", help = "Filename").required()
    private val numray: Int by option("--numray", "--nr", help = "NUmber of rays used in the pathtracing algorithm").int().default(1)
    private val maxd: Int by option("--maxdepth", "--md", help = "Number of times rays can bounce used in the pathtracing algorithm (2 by default)").int().default(2)
    private val sPS: Int by option("--samplse", "--sps", help = "Number of samples (1 by default)").int().default(1)
    private val sc: Float by option("--scale", "--sc", help = "Scale of sphere").float().default(1.0f)
    override fun run() {
        val image: HDRImage = HDRImage(width, height)
        val ar: Float = image.width.toFloat() / image.height.toFloat()
        var camera: PerpectiveCamera = PerpectiveCamera(AspectRatio = ar,trans = Transformation().RotationZ(rotation) * Transformation().Translation(VecX * (-3.0f) + VecZ * camheight))
        var tracer: ImageTracer = ImageTracer(image = image, camera = camera, samplesPerSide = sPS)
        var world: World = World()
        val scale: Vec =  Vec(1.0f, 1.0f, 1.0f) * sc
        var render: Renderer = OnOffRender()
        if (algorithm == 1) {
            render = OnOffRender(world)
        } else if (algorithm == 2) {
            render = FlatRender(world)
        } else if (algorithm == 3){
            render = PathTracer(world, maxDepth = maxd, numberOfRays = numray, russianRouletteLimit = maxd + 5)
        }


//        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, 5.0f, 5.0f)) * Scaling(scale)))
//        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, 5.0f, -5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = CheckredPigment(WHITE, FUCHSIA, 4)), emitted_radiance = UniformPigment( WHITE))))
//       world.AddShape(Sphere(transformation = Transformation().Translation(Vec(20.0f, -15.0f, -5.0f)) * Transformation().Scaling(scale * 0.25f), Material(brdf = DiffuseBRDF(pigment = CheckredPigment(BLUE, RED, 4)), emitted_radiance = UniformPigment( BLACK))))

//         world.AddShape(Sphere(transformation = Translation(Vec(0.0f, 10.0f, 0.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(RED)), emitted_radiance = UniformPigment( BLACK))))
//         world.AddShape(Sphere(transformation = Translation(Vec(-10.0f, 0.0f, 0.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(AQUA)), emitted_radiance = UniformPigment( BLACK))))
//         world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, -5.0f, 5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(RED)), emitted_radiance = UniformPigment( BLACK))))
//         world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, 5.0f, -5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(AQUA)), emitted_radiance = UniformPigment( BLACK))))
//         world.AddShape(Sphere(transformation = Translation(Vec(10.0f, 0.0f, -10.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(KHAKI)), emitted_radiance = UniformPigment( WHITE))))
         world.AddShape(Sphere(transformation = Transformation().Translation(Vec(-30.0f, 5.0f, -10.0f)) * Transformation().Scaling(scale * 1.5f), Material(brdf = DiffuseBRDF(pigment = UniformPigment(AQUA)), emitted_radiance = UniformPigment( BLACK))))
          world.AddShape(Sphere(transformation = Transformation().Translation(Vec(-30.0f, 5.0f, -5.0f)) *Transformation().Scaling(scale ), Material(brdf = DiffuseBRDF(pigment = UniformPigment(NAVY)), emitted_radiance = UniformPigment( BLACK))))
         world.AddShape(Sphere(transformation = Transformation().Translation(Vec(20.0f, 0.0f, -10.0f)) * Transformation().Scaling(scale), Material(brdf = SpecularBRDF(pigment = UniformPigment(WHITE)), emitted_radiance = UniformPigment( BLACK))))
//         world.AddShape(Sphere(transformation = Translation(Vec(7.0f, 1.0f, -10.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(RED)), emitted_radiance = UniformPigment( WHITE))))
//  //
      world.AddShape(Sphere(transformation = Transformation().Translation(Vec(10.0f, 15.0f, -10.0f)) * Transformation().Scaling(scale * 0.5f), material = Material(brdf = DiffuseBRDF(pigment = UniformPigment(NAVY)), emitted_radiance = UniformPigment( BLACK))))

         world.AddShape(Plane(transformation = Transformation().Translation(Vec(0.0f,0.0f, -10.0f)), material = Material(brdf = DiffuseBRDF(pigment = CheckredPigment(WHITE, BLACK, 1)), emitted_radiance = UniformPigment(BLACK))))
         world.AddShape(Plane(transformation = Transformation().Translation(Vec(0.0f,0.0f, 15.0f)), material = Material(brdf = DiffuseBRDF(pigment = UniformPigment(GRAY)), emitted_radiance = UniformPigment(GRAY * 1.2f))))
        
//        world.AddShape(Triangle(A = Point(30.0f, -10.0f, -10.0f), B = Point(30.0f, 10.0f, -10.0f), C = Point(30.0f, 0.0f, 5.0f), transformation = Transformation(), Material(brdf = DiffuseBRDF(pigment = UniformPigment(KHAKI)), emitted_radiance = UniformPigment( BLACK))))
//         world.AddShape(Sphere(transformation = Translation(Vec(10.0f,0.0f, -10.0f)), material = Material(brdf = SpecularBRDF(pigment = UniformPigment(RED)))))

        tracer.FireAllRays {render.Render(it)}
        image.SaveLDR(fname, "PNG", 1.0f)




    }

    
}

fun main(args: Array<String>) = myRatracer().subcommands(Demo()).main(args)
