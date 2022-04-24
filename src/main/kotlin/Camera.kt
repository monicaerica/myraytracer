val VecX: Vec = Vec(1.0f, 0.0f, 0.0f) //Implement this in geometry after merging!

interface Camera {
    val AspectRatio: Float
    val trans: Transformation
    fun FireRay(u: Float, v: Float): Ray
}

class OrthogonalCamera (override val AspectRatio: Float = 1.0f, override val trans: Transformation): Camera{
    override fun FireRay(u: Float, v: Float): Ray{
        val origin: Point = Point(-1.0f, (1.0f - 2.0f * u) * AspectRatio, 2.0f * v - 1.0f)
        val direction: Vec = VecX
        return Ray(origin, direction, tmin = 1.0f).transform(this.trans)
    }
}