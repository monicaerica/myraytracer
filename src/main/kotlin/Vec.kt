import kotlin.math.abs
import kotlin.math.sqrt

data class Vec(
    var x: Float = 0.0f,
    var y: Float = 0.0f,
    var z: Float = 0.0f
) {

    /**
     * Returns the sum of two vectors
     * @param other: The other vector to be summed with the original
     * @return A Vec given by the sum of the original and the other vector
     */
    operator fun plus(other: Vec): Vec {
        return Vec(x + other.x, y + other.y, z + other.z)
    }

    /**
     * Performs the product between a vector and a scalar
     * @param scalar: A float to be multiplied with the vector
     * @return The vector times the scalar
     */
    operator fun times(scalar: Float): Vec {
        return Vec(x * scalar, y * scalar, z * scalar)
    }

    /**
     * Negates a vector
     * @return -vec
     */
    fun Neg(): Vec{
        return this * -1.0f
    }

    operator fun minus(other: Vec): Vec{
        return this + other.Neg()
    }

    operator fun times(other: Vec): Float {
        return x * other.x + y * other.y + z * other.z
    }

    fun SquaredNorm(): Float {
        return this * this
    }

    fun Norm(): Float {
        return sqrt(SquaredNorm())
    }

    fun Cross(other: Vec): Vec {
        return Vec(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    fun Normalize(): Vec {
        val norm: Float = Norm()
        return Vec(x / norm, y / norm, z / norm)
    }
    fun toNormal(): Normal{
        return Normal(x, y, z)
    }

    fun IsClose(other: Vec, epsilon: Float = 1e-5f): Boolean{
        var IsClose: Boolean = false
        if (abs(x - other.x) < epsilon && abs(y - other.y) < epsilon && abs(z - other.z) < epsilon) {
            IsClose = true
        }
        return IsClose
    }
}
