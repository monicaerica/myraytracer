public data class Color(var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f) {

    operator fun plus(other: Color): Color {
        return Color(this.r + other.r, this.g + other.g, this.b + other.b)
    }

    operator fun times(other: Float): Color{
        return Color (this.r * other, this.g * other, this.b * other)
    }

    fun IsClose(other: Color, epsilon : Float = 1e-5f): Boolean{
        var isclose: Boolean = false
        if (IsClose(this.r, other.r, epsilon) && IsClose(this.g, other.g, epsilon) && IsClose(this.b, other.b, epsilon)) {
            isclose = true
        }
        return isclose
    }


    fun Luminosity(): Float{
        return (maxOf(maxOf(this.r, this.g),this.b) + minOf(minOf(this.r, this.g),this.b)) / 2
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