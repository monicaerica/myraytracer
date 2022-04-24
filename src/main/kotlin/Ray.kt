import kotlin.Float.Companion.POSITIVE_INFINITY

data class Ray(
    val Origin: Point = Point(),
    val Dir: Vec = Vec(),
    val tmin: Float = 1e-5f,
    val tmax: Float = POSITIVE_INFINITY,
    val Depth: Int = 0

) {

    fun IsClose(other: Ray, epsilon: Float = 1e-5f): Boolean{
        return Origin.is_close(other.Origin, epsilon = epsilon) and Dir.IsClose(other.Dir, epsilon = epsilon)
    }

    fun At(t: Float): Point{
        return Origin + Dir * t
    }

    fun transform(trans: Transformation): Ray {
        return Ray(
            trans * this.Origin,
            trans * this.Dir,
            this.tmin,
            this.tmax,
            this.Depth
        ) 
    }


}