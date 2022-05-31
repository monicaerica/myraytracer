import kotlin.Float.Companion.POSITIVE_INFINITY
import kotlin.math.*

/**
 * This is an abstract class
 * This returns a variable of type Color instead of a variable of scalar type
 * Float in order to show the wavelength dependence of the BRDF
 */
abstract class BRDF (val pigment: Pigment = UniformPigment(WHITE)){
    abstract fun Eval(nor: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2d): Color
    abstract fun ScatterRay(pcg: PCG, incoming_dir: Vec, interaction_point: Point, normal: Normal, depth: Int): Ray
}

class DiffuseBRDF(pigment: Pigment = UniformPigment(WHITE)): BRDF(pigment){
    override fun Eval(nor: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2d):Color{
        return this.pigment.GetColor(uv) * (1f/PI.toFloat())
    }
    override fun ScatterRay(pcg: PCG, incoming_dir: Vec, interaction_point: Point, normal: Normal, depth: Int): Ray{
        var normal_normalized = normal
        if (normal.Norm() != 1f) normal_normalized = normal.Normalize()
        val base : Triple<Vec, Vec, Vec> = createOnbFromZ(normal_normalized)
        var cos_theta_sq = pcg.RandomFloat()
        var cos_theta = sqrt(cos_theta_sq)
        var sin_theta = sqrt(1.0f - cos_theta_sq)
        var phi = 2.0f * PI.toFloat() * pcg.RandomFloat()

        return Ray(interaction_point, base.first * cos(phi).toFloat() + base.second * sin(phi).toFloat() * cos_theta + base.third * sin_theta, 1.0e-3f, POSITIVE_INFINITY, depth)
    }
}

class SpecularBRDF(pigment: Pigment = UniformPigment(WHITE), val threshold_angle_rad : Float = PI.toFloat()/1800.0f): BRDF(pigment){
    override fun Eval(nor: Normal, in_dir: Vec, out_dir: Vec, uv: Vec2d):Color{
        val theta_in = acos(nor.ToVec() * in_dir)
        val theta_out = acos(nor.ToVec() * out_dir)
        if(abs(theta_in - theta_out) < threshold_angle_rad) return this.pigment.GetColor(uv)
        else return Color(0.0f, 0.0f, 0.0f)
    }

    override fun ScatterRay(pcg: PCG, incoming_dir: Vec, interaction_point: Point, normal: Normal, depth: Int): Ray{
        var ray_dir = Vec(incoming_dir.x, incoming_dir.y, incoming_dir.z).Normalize()
        var normal_normalized = normal.Normalize()

        return Ray(interaction_point, ray_dir - normal_normalized.ToVec() * 2 * normal_normalized.Dot(ray_dir), 1.0e-3f, POSITIVE_INFINITY, depth)
    }
}