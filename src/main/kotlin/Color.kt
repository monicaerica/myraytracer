public data class Color (var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f) {

    operator fun times(other: Float): Color{
        return Color (this.r + other, this.g + other, this.b + other)
    }

    operator fun plus(other: Color): Color{
        return Color(this.r + other.r, this.b + other.b, this.g + other.g)
    }
}