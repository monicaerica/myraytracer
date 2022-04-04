import kotlin.math.sqrt

data class Vec(
    var x: Float,
    var y: Float,
    var z: Float
) {
    operator fun plus(other: Vec): Vec{
      return Vec(x + other.x, y + other.y, z + other.z)
    }
    operator fun times(scalar: Float): Vec{
        return Vec(x * scalar, y * scalar, z * scalar)
    }

    operator fun times(other: Vec): Float{
        return x * other.x + y * other.y + z * other.z
    }

    fun SquaredNorm (): Float{
        return this * this
    }

    fun Norm(): Float{
        return sqrt(SquaredNorm())
    }
}
