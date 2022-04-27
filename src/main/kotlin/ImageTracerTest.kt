import org.junit.Assert.*
import org.junit.Test

internal class ImageTracerTest {
    var image = HDRImage(width = 4, height = 2)
    var camera = PerpectiveCamera(AspectRatio = 2.0f, trans = Transformation())
    var tracer = ImageTracer(image = image, camera = camera)

   @Test
    fun ImagetracerRay() {
       val ray1: Ray = tracer.FireRay(0, 0, uPixel = 2.5f, vPixel = 1.5f)
       val ray2: Ray = tracer.FireRay(2, 1, uPixel = 0.5f, vPixel = 0.5f)
       assertTrue(ray1.IsClose(ray2))
   }
    @Test
    fun ImagetracerCoverage(){
       tracer.FireAllRays { Color(1.0f, 2.0f, 3.0f) }
       for (row in 0 until image.height){
           for (col in 0 until image.width){
               assertTrue(image.GetPixel(col, row) == Color(1.0f, 2.0f, 3.0f))
           }
       }
    }
}