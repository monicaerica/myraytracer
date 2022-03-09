public data class Color (var r: Float = 0.0, var g: Float = 0.0, var b: Float = 0.0) {

    operator fun times(this, other: Float): Color{
        return Color (this.r + other, this.g + other, this.b + other)
    }

}