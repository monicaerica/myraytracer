public data class Point (val x: Float, val y: Float, val z: Float) {

    override fun toString(): String {
        return "<$x, $y, $z>"
    }

    operator fun plus(other: Vec): Point{
        return Point(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    operator fun minus(other: Point): Vec{
        return Vec(other.x - this.x, other.y - this.y, other.z - this.z)
    }

    operator fun minus(other: Vec): Point{
        return Point(other.x - this.x, other.y - this.y, other.z - this.z)
    }


}