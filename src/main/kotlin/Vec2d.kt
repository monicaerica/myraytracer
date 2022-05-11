import kotlin.math.abs

class Vec2d(
    var u: Float = 0.0f,
    var v: Float = 0.0f,
) {
    fun IsClose(other: Vec2d, epsilon: Float = 1e-5f): Boolean{
        var isclose: Boolean = false
        if (abs(this.u - other.u) < epsilon && abs(this.v - other.v) < epsilon) {
            isclose = true
        }
        return isclose
    }

    /**
     * Returns the sum of two vectors
     * @param other: The other vector to be summed with the original
     * @return A Vec2d given by the sum of the original and the other vector
     */
    operator fun plus(other: Vec2d): Vec2d {
        return Vec2d(u + other.u, u + other.u)
    }
}