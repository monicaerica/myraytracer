public data class Color (var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f) {

    operator fun plus(other: Color): Color {
        return Color(this.r + other.r, this.g + other.g, this.b + other.b)
    }

    operator fun times(other: Float): Color{
        return Color (this.r * other, this.g * other, this.b * other)
    }

    fun is_close(other: Color): Boolean{
        var isclose: Boolean = false
        if ((this.r - other.r) < 1e-5f && (this.g - other.g) < 1e-5f && (this.b - other.b) < 1e-5f) {
            isclose = true
        }
        return isclose
    }
}

/**
 * This class implements a data type for a color with the sRGB encoding
 * @param: R: Red, value between 0 and 255
 * @param: G: Green, value between 0 and 255
 * @param: B: Blue, value between 0 and 255
 */
public data class SColor (var R: Int = 0, var G: Int = 0, var B: Int = 0) {
    /**
     * Sums two sRGB colors, gives another sRGB color
     */
    operator fun plus(other: SColor): SColor {
        return SColor(this.R + other.R, this.G + other.G, this.B + other.B)
    }
}