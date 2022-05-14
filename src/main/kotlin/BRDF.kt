import kotlin.math.PI

/**
 * This is an abstract class
 * This returns a variable of type Color instead of a variable of scalar type
 * Float in order to show the wavelength dependence of the BRDF
 */
abstract class BRDF (val pigment: Pigment = UniformPigment(WHITE)){
    abstract fun Eval(nor: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2d): Color
//    abstract fun ScatterRay(pcg: PCG, incoming_dir: Vec, interaction_point: Point, normal: Normal, depht: Int)
}

class DiffuseBRDF(pigment: Pigment = UniformPigment(WHITE)): BRDF(pigment){
    override fun Eval(nor: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2d):Color{
        return this.pigment.GetColor(uv) * (1f/PI.toFloat())
    }
}