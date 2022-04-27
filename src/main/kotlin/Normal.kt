import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.math.pow
//import org.jetbrains.numkt.linalg

data class Normal(var x: Float = 0.0f, var y: Float = 0.0f, var z: Float = 0.0f) {

    /**
     * This is in order to print the string and see information about the point.
     *
     * @return a String with the description of the point
     */
    fun NormalToString(): String {
        return "Normal: (x:" + this.x + ", y:" + this.y + ", z:" + this.z + ")"
    }
    /**
     * This is in order to test if two normals are similar.
     *
     * @return a Boolean value true if these two normals are similar.
     * @param Normal another variable of type Normal
     * @param epsilon the max difference between every single component of the two Normal variables in order to be close together
     */
    fun IsClose(other: Normal, epsilon: Float = 1e-5f): Boolean{
        var isclose: Boolean = false
        if (abs(this.x - other.x) < epsilon && abs(this.y - other.y) < epsilon && abs(this.z - other.z) < epsilon) {
            isclose = true
        }
        return isclose
    }

    fun neg(): Normal{
        return (this.times(-1.0f))
    }

    operator fun times(other: Float): Normal{
        return Normal(this.x * other, this.y * other, this.z * other)
    }

    operator fun times(other: Int): Normal{
        return Normal(this.x * other, this.y * other, this.z * other)
    }

    fun Dot(other: Vec): Float{
        var scalar : Float = this.x * other.x + this.y * other.y + this.z * other.z
        return scalar
    }

    fun Cross(other: Vec): Vec{
        return Vec(this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x)
    }

    fun Cross(other: Normal): Vec{
        return Vec(this.y * other.z - this.z * other.y,
            this.z * other.x - this.x * other.z,
            this.x * other.y - this.y * other.x)
    }

    fun SquaredNorm(): Float{
//        val kotlin_array: FloatArray = floatArrayOf(this.x,this.y, this.z)
        var scalar : Float = x.pow(2) + y.pow(2) + z.pow(2)
        return scalar
    }

    fun Norm(): Float{
        var sq_norm: Float = this.SquaredNorm()
//        var sq_norm: Double = this.squared_norm().toDouble()
        return sqrt(sq_norm)
    }

    fun Normalize(): Unit{
        var norm = this.Norm()
        x /= norm
        y /= norm
        z /= norm
    }

    operator fun times(other: Vec): Float{
        var scalar : Float = x * other.x + y * other.y + z * other.z
        return scalar
    }
}
