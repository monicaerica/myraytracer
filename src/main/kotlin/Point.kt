import kotlin.math.abs

data class Point(var x: Float = 0.0f, var y: Float = 0.0f, var z: Float = 0.0f){

    /**
     * This is in order to print the string and see information about the point.
     *
     * @return a String with the description of the point
     */
    fun PointToString(): String{
        return "Point: (x:" + this.x + ", y:" + this.y + ", z:" + this.z + ")"
    }

    /**
     * This is in order to test if two points are close together.
     *
     * @return a Boolean value true if these two points are close together.
     * @param Point another variable of type Point
     * @param epsilon the max difference between every single component of the two Point variables in order to be close together
     */
    fun IsClose(other: Point, epsilon: Float = 1e-5f): Boolean{
        var isclose: Boolean = false
        if (IsClose(this.x, other.x, epsilon) && (IsClose(this.y, other.y, epsilon)) && (IsClose(this.z, other.z, epsilon))) {
            isclose = true
        }
        return isclose
    }
    /**
     * This turns a point into a vector.
     *
     * @return a Vec with the same features of the starting Point.
     */
    fun PointToVec(): Vec{
        return Vec(this.x, this.y, this.z)
    }

    //operator overloading

    /**
     * This sums a Point to a Vec
     *
     * @return a Point.
     * @param Vec
     */
    operator fun plus(other: Vec): Point {
        return Point(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    /**
     * Difference between two points
     *
     * @return a Vec.
     * @param other another Point
     */
    operator fun minus(other: Point): Vec {
        return Vec(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    /**
     * Difference between a Point and a Vec
     *
     * @return a Point.
     * @param other a Vec
     */
    operator fun minus(other: Vec): Point {
        return Point(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    /**
     * Point times a scalar
     *
     * @return a Point.
     * @param other a Float
     */
    operator fun times(other: Float): Point{
        return Point(this.x * other, this.y * other, this.z * other)
    }

    /**
     * Point times a scalar
     *
     * @return a Point.
     * @param other an Int
     */
    operator fun times(other: Int): Point{
        return Point(this.x * other, this.y * other, this.z * other)
    }
}
