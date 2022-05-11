import kotlin.math.abs
import kotlin.math.sqrt

data class Vec(
    var x: Float = 0.0f,
    var y: Float = 0.0f,
    var z: Float = 0.0f
) {
    /**
     * Returns a boolean meaning if two vector are close together
     * @param other: The other vector to be compared
     * @param epsilon: tolerance
     * @return A booleam
     */
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
     * Changes the signs of all the components of the vector
     * @return The vector in the opposite direction
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

    /**
     * Squared norm of the vector
     * @return Float
     */
    fun SquaredNorm(): Float {
        return this * this
    }

    /**
     * Norm of the vector
     * @return Float
     */
    fun Norm(): Float {
        return sqrt(SquaredNorm())
    }

    /**
     * Cross product between two vectors
     * @param Vec the other vector
     * @return Vec, cross product between the two vectors
     */
    fun Cross(other: Vec): Vec {
        return Vec(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }

    /**
     * Normalizes the vector
     * @return The Vector divided by its norm, the vector with unitary norm
     */
    fun Normalize(): Vec {
        return Vec(x / Norm(), y / Norm(), z / Norm())
    }

    /**
     * Turns the vector into a variable of type Normal
     * @return a Normal
     */
    fun toNormal(): Normal{
        return Normal(x, y, z)
    }
}
