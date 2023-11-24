import javax.lang.model.type.UnionType
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.collections.union

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
 
     /**
     * Reflects a vector given a normal
     * @param normal: The normal 
     * @return Vec, the reflected vector
     */
    fun reflect(normal: Normal): Vec{
        return this - normal.ToVec() * (2.0f * dot(this, normal.ToVec()))
    }

    fun randomVec(pcg: PCG): Vec{
        return Vec(pcg.RandomFloat(), pcg.RandomFloat(), pcg.RandomFloat())
    }


}

fun reflect(vIn: Vec, normal: Normal): Vec{
    return vIn - normal.ToVec() * (2.0f * dot(vIn, normal.ToVec()))
}
fun dot(vA: Vec, vB: Vec): Float{
    return vA.x * vB.x + vA.y * vB.y + vA.y * vB.y
}

fun randomVec(pcg: PCG, min:Float, max:Float): Vec{

    return Vec(pcg.RandomFloat(min, max), pcg.RandomFloat(min, max), pcg.RandomFloat(min, max))
}

fun createOnbFromZ (normal: Normal): Triple<Vec, Vec, Vec>{
    if (normal.SquaredNorm() != 1.0f)
        normal.Normalize()
    val sign: Float =  if (normal.z > 0.0f) 1.0f else -1.0f
    val a: Float = -1.0f / (sign + normal.z)
    val b: Float = normal.x * normal.y * a

    val e1: Vec = Vec(1.0f + sign * normal.x * normal.x *a, sign * b, -sign * normal.x)
    val e2: Vec = Vec(b, sign + normal.y * normal.y * a, -normal.y)

    return Triple(e1, e2, Vec(normal.x, normal.y, normal.z))
}

fun randomInUnitSphere (pcg: PCG): Vec {
    while(true) {
        var rnd: Vec = randomVec(pcg, -1.0f, 1.0f)
        if (rnd.SquaredNorm() < 1)
            return rnd
    }
}

fun randomUnitVector(pcg: PCG): Vec {
    return randomInUnitSphere(pcg).Normalize()
}

fun randomOnEmisphere(pcg: PCG, normal: Normal): Vec {
    var rnd: Vec = randomUnitVector(pcg)
    return if (dot(rnd, normal.ToVec())> 0.0f) rnd else -rnd
}
