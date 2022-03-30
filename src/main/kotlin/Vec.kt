import kotlin.math.sqrt

public data class Vec (val x: Float, val y: Float, val z: Float) {
    override fun toString(): String {
        return "<$x, $y, $z>"
    }

    /**
     * Performs a product between the vector and a float
     * @param scalar: a float
     *@return the original vector scaled by a scalar
     */
    operator fun times(scalar: Float): Vec{
        return Vec(this.x * scalar, this.y * scalar, this.z * scalar)
    }

    /**Perform a scalar (dot) product between two vector
     * @param other: a vector
     * @return a scalar, result of the dot product bewteen the vectors
     */
    operator fun times(other: Vec): Float{
        return this.x * other.x + this.y * other.y + this.z * other.z
    }

    /**
     * Returns the squared norm of a vector
     */
    fun SquaredNorm(): Float{
        return this * this
    }

    /**
     * Returns the norm of a vector
     */
    fun Norm(): Float {
        return sqrt(SquaredNorm())
    }

    /**
     * Performs a cross product between two vecotr
     * @param other: The other vector (this X other)
     * @return a vector which is the result of this X other
     */
    fun Cross(other: Vec): Vec{
        return Vec(
            this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.z
        )
    }
}