data class HitRecord(
    var worldPoint: Point = Point(),
    var normal: Normal = Normal(),
    var surfacePoint: Vec2d = Vec2d(),
    var t: Float = 0.0f,
    var ray: Ray = Ray()
) {
}