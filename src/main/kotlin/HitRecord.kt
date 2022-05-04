import java.security.KeyStore.TrustedCertificateEntry
import kotlin.math.abs
data class HitRecord(
    var worldPoint: Point = Point(),
    var normal: Normal = Normal(),
    var surfacePoint: Vec2d = Vec2d(),
    var t: Float = 0.0f,
    var ray: Ray = Ray()
) {
    fun isClose(other: HitRecord?): Boolean {
        var isClose: Boolean = false
        if (other != null) {
            if (this.worldPoint.is_close(other.worldPoint) &&
                this.normal.IsClose(other.normal) &&
                this.surfacePoint.IsClose(other.surfacePoint) &&
                abs(this.t - other.t)<1e-5f &&
                this.ray.IsClose(other.ray)
            )
                isClose = true
        }
        return isClose
    }
}