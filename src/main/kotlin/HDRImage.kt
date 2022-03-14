data class HDRImage (
    val width: Int,
    val height: Int,
    var pixels: Array<Color> = Array(width * height) {Color (0.0f,0.0f,0.0f )}
        ) {

    fun valid_coordinates(x: Float, y: Float): Boolean{
        var valid = false
        if (x < this.width && y < this.height && x > 0 && y > 0) var = true
        return valid
    }
            
}
