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
class UniformPigment(color: Color) : Pigment(){
    val color = color
    /**
     *  In this case the function returns the same color whichever Vec2d
     *  is given
     *  @return : color, variable of type Color
     */
    override fun GetColor(uv: Vec2d): Color{
        return this.color
    }
}