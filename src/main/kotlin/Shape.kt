abstract class Shape(transformation: Transformation = Transformation()) {
    open fun rayIntersection(ray: Ray): HitRecord?{
        return null
    }
}