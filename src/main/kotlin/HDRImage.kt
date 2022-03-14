data class HDRImage (
    val width: Int,
    val height: Int,
    var pixels: Array<Color> = Array(width * height) {Color (0.0f,0.0f,0.0f )}
        ) {
    fun ValidCoordinates(x: Int, y: Int): Boolean{
        return ((x>=0 && x<= this.width) && (y>=0 && y<= this.height))
    }
    //get pixel: obtain the 1 dimensional position of a pixel from a tuple (x, y)
    fun GetPixel(x: Int, y: Int): Color{
        assert(ValidCoordinates(x, y))
        val pos: Int = x * this.width + y
        val pixel: Color = this.pixels[pos]
        return pixel
    }
}
