import kotlin.math.*

fun main(args: Array<String>) {
    println("Hello KoTracer user!")

    val img: HDRImage = HDRImage(1920, 1080)
    val incR = 100.0f / img.width.toFloat()
    val incG = 100.0f / img.height.toFloat()
    var curColor = Color(1.0f, 0.0f, 0.0f)
    val Radius: Int = 500
    for (x in 0 until img.width){
        for(y in 0 until img.height){

            if (((x-img.width/2)*(x-img.width/2)+(y-img.height/2)*(y-img.height/2)) <= Radius * Radius){
                curColor = Color(x.toFloat() * incR * 2, y.toFloat() * incG * 3, (y.toFloat() + x.toFloat()) * incG / 5)
            }
            else {
                curColor = Color(x.toFloat() * incR, y.toFloat() * incG, (y.toFloat() + x.toFloat()) / 2.0f * incG)
            }
            img.SetPixel(x, y, curColor)
        }
    }

    img.NormalizeImage(factor = 1.0f)
    img.ClampImg()
    img.SaveLDR("HeloCircPNG.png", "PNG")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
}