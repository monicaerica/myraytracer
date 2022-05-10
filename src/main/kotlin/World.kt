import kotlin.math.abs

/*
    A collection of shapes
 */
class World (vararg shapesin: Shape){
    var shapes = mutableListOf<Shape?>()

    /**
     * Add a shape to the world
     * @param shape: The shape that we want to add to the scene
     */
    fun AddShape(shape: Shape): Unit{
        this.shapes.add(shape)
    }
    /**
     * Computes the intersection with a ray
     * @param ray: The ray we want to calculate the intersection
     * @return closest: return the closest intersection with the ray
     */
    fun rayIntersection(ray: Ray): HitRecord?{
        var closest : HitRecord? = null
        var intersection : HitRecord? = null
        for (shape in shapes){
            intersection = shape?.rayIntersection(ray)
            if (intersection != null){
                if (closest == null || intersection.t < closest.t ){
                    closest = intersection
                }
            }
        }
        return closest
    }
}