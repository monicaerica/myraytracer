import kotlin.math.abs

/*
    A collection of shapes
 */
class World (vararg shapesin: Shape){
    var shapes = mutableListOf<Shape?>()
    var pointLights = mutableListOf<pointLight?>()
    /**
     * Add a shape to the world
     * @param shape: The shape that we want to add to the scene
     */
    fun AddShape(shape: Shape): Unit{
        this.shapes.add(shape)
    }

    fun AddLights(light: pointLight): Unit{
        this.pointLights.add(light)
    }

    fun isPointVisible(point: Point?, observerPos: Point): Boolean {
        if (point == null) {
            return false
        }
        val direction: Vec = point - observerPos
        val dirNorm: Float = direction.Norm()
        val ray: Ray = Ray(observerPos, direction, 0.001f / dirNorm, 1.0f)

        for (shape in this.shapes) {
            if (shape?.rayIntersection(ray) != null){
                return false
            }
        }

        return true
    }
    /**
     * Computes the intersection with a ray
     * @param ray: The ray we want to calculate the intersection
     * @return closest: return the closest intersection with the ray
     */
    fun rayIntersection(ray: Ray): HitRecord? {
    var closest: HitRecord? = null

    for (shape in shapes) {
        val intersection = shape?.rayIntersection(ray)

        if (intersection != null) {
            if (closest == null || intersection.t < closest.t) {
                closest = intersection
            }
        }
    }

    return closest
}

}