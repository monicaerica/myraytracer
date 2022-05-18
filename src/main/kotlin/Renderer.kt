import kotlin.math.abs
import kotlin.math.floor

abstract class Renderer(val world: World, val background_color: Color = BLACK) {
    abstract fun Render(ray: Ray) : Color
}

class OnOffRender(world: World = World(), background_color: Color = BLACK, var color: Color = WHITE):Renderer(world, background_color){
    override fun Render(ray: Ray):Color{
        return if (this.world.rayIntersection(ray) != null) this.color
        else
            return this.background_color
    }
}

class FlatRender(world: World = World(), background_color: Color = BLACK, var color: Color = WHITE):Renderer(world, background_color){
    override fun Render(ray: Ray):Color{
        val hit = this.world.rayIntersection(ray)
        if (hit == null)
            return this.background_color
        val material = hit.shape.material
        return (material.brdf.pigment.GetColor(hit.surfacePoint) + material.emitted_radiance.GetColor(hit.surfacePoint))
    }
}