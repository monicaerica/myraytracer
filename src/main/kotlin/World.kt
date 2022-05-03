import kotlin.math.abs

class World (vararg shapesin: Shape){
    var shapes = mutableListOf<Shape?>()

    fun AddShape(shape: Shape): Unit{
        this.shapes.add(shape)
    }

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