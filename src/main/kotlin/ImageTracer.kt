class ImageTracer(val image: HDRImage, val camera: Camera) {

    fun FireRay(col: Int, row: Int, uPixel: Float = 0.5f, vPixel: Float = 0.5f): Ray {
        val u: Float = (col + uPixel) / (this.image.width - 1)
        val v: Float = 1.0f - (row + vPixel) / (this.image.height - 1)
        return this.camera.FireRay(u, v)
    }

    fun FireAllRays(func: (Ray) -> Color) {
        var percent: Float = 0.0f
        for (row in 0 until this.image.height) {
            for (col in 0 until this.image.width) {
                var ray: Ray = this.FireRay(col, row)
                var color: Color = func(ray)
                this.image.SetPixel(col, row, color)
                percent += 1.0f
                println(percent / (this.image.width * this.image.height) * 100.0f)
            }
        }
    }
}