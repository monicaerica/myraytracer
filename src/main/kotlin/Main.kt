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
    private val algorithm: Int by option ("--algorithm", "--a", help = "Select the algorithm you wish to use to render the image: \n 1- OnOffRender\n 2- FlatRender").int().default(1)
    private val fname by option("--fname", "-f", help = "Filename").required()
    override fun run() {
        val image: HDRImage = HDRImage(width, height)
        val ar: Float = image.width.toFloat() / image.height.toFloat()
        var camera: PerpectiveCamera = PerpectiveCamera(AspectRatio = ar,trans = RotationZ(rotation) * Translation(VecX * (-1.0f) + VecZ * camheight))
        var tracer: ImageTracer = ImageTracer(image = image, camera = camera)
        var world: World = World()
        val scale: Vec = Vec(0.1f, 0.1f, 0.1f)
        var render: Renderer = OnOffRender()
        if (algorithm == 1) {
            render = OnOffRender(world)
        } else if (algorithm == 2) {
            render = FlatRender(world)
        } else if (algorithm == 3){
            render = PathTracer(world, maxDepth = 5, numberOfRays = 10, russianRouletteLimit = 5)
        }


        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, 5.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, 5.0f, -5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = CheckredPigment(WHITE, FUCHSIA, 4)))))
        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, -5.0f, 5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = CheckredPigment(BLUE, KHAKI, 4)))))
        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, -5.0f, -5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(RED)))))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, 5.0f, 5.0f)) * Scaling(scale), Material(brdf = DiffuseBRDF(pigment = UniformPigment(AQUA)))))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, -5.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, 5.0f, -5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, -5.0f, -5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(0.0f, 5.0f, 0.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(0.0f, 0.0f, 5.0f)) * Scaling(scale)))



        tracer.FireAllRays {render.Render(it)}
        image.SaveLDR(fname, "PNG", 1.0f)




    }

    
}

fun main(args: Array<String>) = myRatracer().subcommands(Demo()).main(args)
