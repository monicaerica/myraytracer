val VecX: Vec = Vec(1.0f, 0.0f, 0.0f) //Implement this in geometry after merging!

interface Camera {
    val distance: Float
    val AspectRatio: Float
    val trans: Transformation
    fun FireRay(u: Float, v: Float): Ray
}


class OrthogonalCamera (override val AspectRatio: Float = 1.0f, override val trans: Transformation,
                        override val distance: Float = 0.0f): Camera{

    override fun FireRay(u: Float, v: Float): Ray{
        val origin: Point = Point(-1.0f, (1.0f - 2.0f * u) * AspectRatio, 2.0f * v - 1.0f)
        val direction: Vec = VecX
        return Ray(origin, direction, tmin = 1.0f).transform(this.trans)
    }
}

class PerpectiveCamera ( override val AspectRatio: Float = 1.0f, override val trans: Transformation,
                         override val distance: Float = 1.0f): Camera {

    override fun FireRay(u: Float, v: Float): Ray {
        val origin: Point = Point(-this.distance, 0.0f, 0.0f)
        val direction: Vec = Vec(this.distance, (1.0f - 2.0f * u) * AspectRatio, 2.0f * v - 1.0f)
        return Ray(origin, direction, tmin = 1.0f).transform(this.trans)
    }
}

