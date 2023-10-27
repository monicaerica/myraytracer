class ImageTracer(val image: HDRImage, val camera: Camera, val samplesPerSide: Int, val pcg: PCG = PCG()) {

    fun FireRay(col: Int, row: Int, uPixel: Float = 0.5f, vPixel: Float = 0.5f): Ray {
        val u: Float = (col + uPixel) / (this.image.width - 1)
        val v: Float = 1.0f - (row + vPixel) / (this.image.height - 1)
        return this.camera.FireRay(u, v)
    }

    fun FireAllRays(func: (Ray) -> Color) {
        var uPixel = 0.5f
        var vPixel = 0.5f
        var percent: Float = 0.0f

        var ray: Ray
        for (row in 0 until this.image.height) {
        
        
            for (col in 0 until this.image.width) {
                var cumColor: Color = Color(0.0f, 0.0f, 0.0f)

                if (samplesPerSide > 0)
                {
                    for (interPixelRow in 0 until this.samplesPerSide)
                    {
                        for (interPixelCol in 0 until this.samplesPerSide)
                        {
                            uPixel = (interPixelCol + this.pcg.RandomFloat()) / samplesPerSide.toFloat()
                            vPixel = (interPixelRow + this.pcg.RandomFloat()) / samplesPerSide.toFloat()
                            ray = this.FireRay(col, row, uPixel, vPixel)
                            cumColor += func(ray)
                        }
                    }
                    this.image.SetPixel(col, row, cumColor)
                }
                else
                {
                    ray = this.FireRay(col, row)
                    this.image.SetPixel(col, row, func(ray))
                }
                
                
                
                // percent += 1.0f
                // val totalPixels = this.image.width * this.image.height
                // val progress = (percent / totalPixels) * 100.0f

                
                // println("Progress: ${progress.toInt()}%")
                

            }
        }
    }
}