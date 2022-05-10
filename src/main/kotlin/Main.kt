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
    private val fname by option("--fname", "-f", help = "Filename").required()
    override fun run() {
        val image: HDRImage = HDRImage(width, height)
        val ar: Float = image.width.toFloat() / image.height.toFloat()
        var camera: PerpectiveCamera = PerpectiveCamera(AspectRatio = ar,trans = RotationZ(rotation) * Translation(VecX * (-2.0f) + VecZ * camheight) * RotationY(
            atan(camheight / 3.0f) * 180.0f / PI.toFloat()))
        var tracer: ImageTracer = ImageTracer(image = image, camera = camera)
        var world: World = World()
        val scale: Vec = Vec(0.1f, 0.1f, 0.1f)

        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, 5.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, 5.0f, -5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, -5.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(5.0f, -5.0f, -5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, 5.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, -5.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, 5.0f, -5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(-5.0f, -5.0f, -5.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(0.0f, 5.0f, 0.0f)) * Scaling(scale)))
        world.AddShape(Sphere(transformation = Translation(Vec(0.0f, 0.0f, 5.0f)) * Scaling(scale)))
        world.AddShape(Plane(transformation = Translation(Vec(0.0f, 0.0f, 10f))))
        tracer.FireAllRays { if (world.rayIntersection(it) != null)  NAVY else BLACK }
        image.SaveLDR(fname, "PNG", 1.0f)



    }

    
}

fun main(args: Array<String>) = myRatracer().subcommands(Demo()).main(args)
