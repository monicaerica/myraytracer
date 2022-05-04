import kotlin.math.abs
import kotlin.math.sqrt

data class Vec(
    var x: Float = 0.0f,
    var y: Float = 0.0f,
    var z: Float = 0.0f
) {

//    fun IsClose(other: Vec, epsilon: Float = 1e-5f): Boolean{
//        var isclose: Boolean = false
//        if (abs(this.x - other.x) < epsilon && abs(this.y - other.y) < epsilon && abs(this.z - other.z) < epsilon) {
//            isclose = true
//        }
//        return isclose
//    }

    fun IsClose(other: Vec, epsilon: Float = 1e-5f): Boolean{
        var isclose: Boolean = false
        if (IsClose(this.x, other.x, epsilon) && IsClose(this.y, other.y, epsilon) && IsClose(this.z, other.z, epsilon)) {
            isclose = true
        }
        return isclose
    }

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
     * Performs the product between a vector and a scalar
     * @param scalar: A int to be multiplied with the vector
     * @return The vector times the scalar
     */
    operator fun times(scalar: Int): Vec {
        return Vec(x * scalar, y * scalar, z * scalar)
    }

    /**
     * Changes the signs of all the components of the vector
     * @return The vector in the opposite direction
     */
    operator fun unaryMinus(): Vec {
        return Vec(x * -1, y * -1, z * -1)
    }

    /**
     * Negates a vector
     */
    fun Neg(): Vec{
        return this * -1.0f
    }

    operator fun minus(other: Vec): Vec{
        return this + other.Neg()
    }
    /**
     * Default product between two vector is the dot product
     * @param Vec the other vector
     * @return Scalar product between the two vectors
     */
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
        return Vec(x / Norm(), y / Norm(), z / Norm())
    }
    fun toNormal(): Normal{
        return Normal(x, y, z)
    }
}
