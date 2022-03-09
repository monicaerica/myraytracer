data class HDRImage (
    val width: Int,
    val height: Int,
    var pixels: Array<Color> = Array(width * height) {Color (0.0f,0.0f,0.0f )}
        ) {
    //get pixel: obtain the 1 dimensional position of a pixel from a tuple (x, y)
    fun GetPixel(x: Int, y: Int): Color{
        val pos: Int = x * this.width + y
        val pixel: Color = this.pixels[pos]
        return pixel

    }
}
