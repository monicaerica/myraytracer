import kotlin.math.roundToInt

/**
  *  This represents a class that maps every point of a surface(2DVec) in a Color
*/
abstract class Pigment() {
    abstract fun GetColor(uv: Vec2d): Color
    // No need to add a NotImplemented exception because whenever a class extends
    // an abstract class with abstract members, the class must implement
    // all the abstract members. This is essential when a subclass has
    // to implement a method to create a specific behavior.
}

/**
 *   This is the case of an uniform surface
*/
class UniformPigment(val color: Color) : Pigment(){
    /**
     *  In this case the function returns the same color whichever Vec2d
     *  is given
     *  @return : color, variable of type Color
     */
    override fun GetColor(uv: Vec2d): Color{
        return this.color
    }
}

/**
 *   This is useful for debugging purposes
 *   @param col1 one of the two colors of the chessboard
 *   @param col2 the second color
 *   @param n_steps number of squares for each side of the chessboard
 */
class CheckredPigment(val col1 : Color, val col2 : Color, val n_steps : Int = 10) : Pigment(){
    /**
     *  This returns the color of the chessboard at the point given
     *  @param uv the Vec2d pointing to the area you want to know the color
     *  @return : color, variable of type Color
     */
    override fun GetColor(uv: Vec2d): Color{
        var int_u = (uv.u * this.n_steps).roundToInt()
        var int_v = (uv.v * this.n_steps).roundToInt()
        return if ((int_u % 2) == (int_v % 2)) this.col1
        else this.col2
    }
}