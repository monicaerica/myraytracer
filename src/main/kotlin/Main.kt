import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.*
import com.github.ajalt.clikt.parameters.types.float
import com.github.ajalt.clikt.parameters.types.int
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

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

        world.AddShape(Sphere(transformation = Transformation().Translation(Vec(-30.0f, 5.0f, -10.0f)) * Transformation().Scaling(scale * 1.5f), Material(brdf = DiffuseBRDF(pigment = UniformPigment(AQUA)), emitted_radiance = UniformPigment( BLACK))))
        world.AddShape(Sphere(transformation = Transformation().Translation(Vec(-30.0f, 5.0f, -5.0f)) *Transformation().Scaling(scale ), Material(brdf = DiffuseBRDF(pigment = UniformPigment(NAVY)), emitted_radiance = UniformPigment( BLACK))))
        world.AddShape(Sphere(transformation = Transformation().Translation(Vec(20.0f, 0.0f, -10.0f)) * Transformation().Scaling(scale), Material(brdf = SpecularBRDF(pigment = UniformPigment(WHITE)), emitted_radiance = UniformPigment( BLACK))))

        world.AddShape(Sphere(transformation = Transformation().Translation(Vec(10.0f, 15.0f, -10.0f)) * Transformation().Scaling(scale * 0.5f), material = Material(brdf = DiffuseBRDF(pigment = UniformPigment(NAVY)), emitted_radiance = UniformPigment( BLACK))))

        world.AddShape(Plane(transformation = Transformation().Translation(Vec(0.0f,0.0f, -10.0f)), material = Material(brdf = DiffuseBRDF(pigment = CheckredPigment(WHITE, BLACK, 1)), emitted_radiance = UniformPigment(BLACK))))
        world.AddShape(Plane(transformation = Transformation().Translation(Vec(0.0f,0.0f, 15.0f)), material = Material(brdf = DiffuseBRDF(pigment = UniformPigment(GRAY)), emitted_radiance = UniformPigment(GRAY * 1.2f))))

        tracer.FireAllRays {render.Render(it)}
        image.SaveLDR(fname, "PNG", 1.0f)
    }
}




class Render: CliktCommand(name = "render") {
    private val filename: String by option("--infile", "-inf", help = "Name of the file containing the description of the scene to be rendered").required()
    private val algorithm: Int by option("--algorithm", "-a", help = "The algorithm used to render the scene, use 1 for on off rendering (only for quick positioning checks), 2 for the flat renderer, 3 for the path tracer. Default: 3").int().default(3)
    private val fname by option("--fname", "-f", help = "Filename into which to save the resulting image").required()
    private val gamma: Float by option("--gamma", "-g", help = "Value of the gamma for RGB -> sRGB, default = 1").float().default(1.0f)
    private val width: Int by option("--width", "--w", help = "Image width in pixels (default = 640px)").int()
        .default(640)
    private val height: Int by option("--height", "--h", help = "Image height in pixels (default = 480px)").int()
        .default(480)
    private val numray: Int by option(
        "--numray",
        "--nr",
        help = "NUmber of rays used in the pathtracing algorithm"
    ).int().default(1)
    private val sps: Int by option(
        "--samperside",
        "--sps",
        help = "Number of samples per pixel"
    ).int().default(1) 
    private val maxDepth: Int by option("--maxdepht", "--md", help = "Max number of times the ray has been scattered").int()
        .default(10)

    override fun run() {
        val image: HDRImage = HDRImage(width, height)
        val ar: Float = image.width.toFloat() / image.height.toFloat()

        var file_byte = FileInputStream(filename)
        var InFile_char = InputStreamReader(file_byte)
        var inFile = InputStream(InFile_char, file_name = filename)

        val scene = parseScene(inFile)
        var camera = scene.camera
        if(camera != null){
            var tracer: ImageTracer = ImageTracer(image = image, camera = camera, samplesPerSide = sps)
            var world = scene.world
            var render: Renderer = OnOffRender()
            if (algorithm == 1){
                render = OnOffRender(world)
            }
            else if (algorithm == 2){
                render = FlatRender(world)
            }
            else if (algorithm == 3){
                render = PathTracer(world, maxDepth = maxDepth, numberOfRays = numray, russianRouletteLimit = maxDepth - 1)
            }
            tracer.FireAllRays {render.Render(it)}
            image.SaveLDR(fname, "PNG", gamma)
        }
        else{
            error("A camera must be provided in the scene file")
            return
        }
    }
}

fun main(args: Array<String>) = myRatracer().subcommands(Render()).main(args)
