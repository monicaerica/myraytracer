public data class Color (var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f) {

    operator fun plus(other: Color): Color {
        return Color(this.r + other.r, this.b + other.b, this.g + other.g)
    }

    operator fun times(other: Float): Color{
        return Color (this.r + other, this.g + other, this.b + other)
    }

    fun is_close(other: Color): Boolean{
        var isclose: Boolean = false
        if ((this.r - other.r) < 1e-5f && (this.g - other.g) < 1e-5f && (this.b - other.b) < 1e-5f) {
            isclose = true
        }
        return isclose
    }
}