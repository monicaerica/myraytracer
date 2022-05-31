import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class PathTracerTest {

    @Test
    fun render() {
        val pcg = PCG()

        for (i in 0 until 5){
            val emittedRadiance = pcg.RandomFloat()
            val reflectance = pcg.RandomFloat() * 0.9f

            val world = World()

            val enclosureMaterial = Material(
                brdf = DiffuseBRDF(pigment = UniformPigment(WHITE * reflectance)),
                emitted_radiance = UniformPigment(WHITE * emittedRadiance)
            )

            world.AddShape(Sphere(material = enclosureMaterial))

            val tracer = PathTracer(pcg = pcg, numberOfRays = 1, world = world, maxDepth = 100, russianRouletteLimit = 101)

            val ray = Ray(Origin = Point(0.0f, 0.0f, 0.0f), Dir = Vec(1.0f, 0.0f, 0.0f))
            val color = tracer.Render(ray)

            val expected = emittedRadiance / (1.0f - reflectance)

            assert(IsClose(expected, color.r, 1e-3f))
            assert(IsClose(expected, color.g, 1e-3f))
            assert(IsClose(expected, color.b, 1e-3f))



        }

    }
}