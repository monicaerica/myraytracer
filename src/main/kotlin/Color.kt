public data class Color (var r: Float, var g: Float, var b: Float){

    operator fun plus(this, other: Color): Color{
        return Color(this.r + other.r, this.b + other.b, this.g + other.g)
    }
}