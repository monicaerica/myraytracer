import kotlin.Float.Companion.POSITIVE_INFINITY

/**
 * This class implements the ray data type used in the ray tracing algorithm
 * @param Origin: A point in space (x, y, z), is the origin of the ray
 * @param Dir: A vector, gives the direction of the ray
 * @param tmin: A float which gives the minimum "time"
 * @param tmax: A float which gives the maximum "time"
 * @param Depth:
 */
data class Ray(
    val Origin: Point = Point(),
    val Dir: Vec = Vec(),
    val tmin: Float = 1e-3f,
    val tmax: Float = POSITIVE_INFINITY,
    val Depth: Int = 0

) {
    /**
     * Checks whether two rays have the same values for origin and direction whithin a given epsilon to account for float approximation
     * @param other: The other ray used in the comparison
     * @param epsilon: The parameter for the approximation
     * @return IsClose: True if they are close
     */
    fun IsClose(other: Ray, epsilon: Float = 1e-5f): Boolean {
        return Origin.IsClose(other.Origin, epsilon = epsilon) and Dir.IsClose(other.Dir, epsilon = epsilon)
    }

    /**
     * Gives the point in 3d space on the ray after a given "time" t
     * @param t: The "time" at which you want to know the location along the ray
     * @return a point in space
     */
    fun At(t: Float): Point {
        return Origin + Dir * t
    }

    /**
     * Trasform a ray using the possible transformations: scaling, rotate around x, y or z axis, translate
     * @param trans: the transformation you want to apply to the ray
     * @return the transformed ray
     */
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