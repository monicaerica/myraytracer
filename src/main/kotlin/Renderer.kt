import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max

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
class FlatRender(world: World = World(), background_color: Color = BLACK):Renderer(world, background_color){
    override fun Render(ray: Ray):Color{
        val hit = this.world.rayIntersection(ray) ?: return this.background_color
        val material = hit.shape.material
        return (material.brdf.pigment.GetColor(hit.surfacePoint) + material.emitted_radiance.GetColor(hit.surfacePoint))
    }
}

class PointLightRenderer(world: World = World(), background_color: Color = BLACK, private val pcg: PCG = PCG(), ambientColor = Color(0.1f, 0.1f, 0.1f)): Renderer(world, background_color){
    override fun Render(ray: Ray): Color {
        val hitRecord = this.world.rayIntersection(ray) ?: return background_color
        val hitMaterial: Material = hitRecord.shape.material
        var resColor = this.ambientColor

        for (light in this.world.pointLights) {
            if (this.world.isPointVisible(light.position, hitRecord.worldPoint)) {
                val distanceVec: Vec = hitRecord.worldPoint - light.position
                val distance: Float = distanceVec.Normalize()
                
                val inDir: Vec = distanceVec * (1.0f / distance)

                val cosTheta: Float = max(0.0f, dot(-ray.direction.Normalize(), hitRecord.normal.Normalize()))
                
                var distanceFactor: Float = 1.0f
                if (light.linearRadius > 0.0f){
                    distanceFactor: Float = (light.linearRadius / distance) * (light.linearRadius / distance)
                }

                val emittedColor: Color = hitMaterial.emitted_radiance.GetColor(hitRecord.surfacePoint)

                val brdfColor: Color = hitMaterial.BRDF.Eval(
                    nor = hitRecord.normal,
                    in_dir = inDir,
                    out_dir = -ray.direction,
                    uv = hitRecord.surfacePoint
                )

                resColor = resColor + (emittedColor + brdfColor) * light.color * cosTheta * distanceFactor
            }
            
        }

        return resColor

    }
}

class PathTracer(world: World = World(), background_color: Color = BLACK, private val pcg: PCG = PCG(), val numberOfRays: Int, val maxDepth: Int, val russianRouletteLimit: Int): Renderer(world, background_color){
    override fun Render (ray: Ray): Color{
        if (ray.Depth > this.maxDepth)
            return BLACK
        val hitRecord = this.world.rayIntersection(ray) ?: return background_color
        val hitMaterial: Material = hitRecord.shape.material
        var hitColor: Color = hitMaterial.brdf.pigment.GetColor(hitRecord.surfacePoint)
        val emittedRadiance: Color = hitMaterial.emitted_radiance.GetColor(hitRecord.surfacePoint)
        val hitColorLum: Float = max(max(hitColor.r, hitColor.g), hitColor.b)

        if (ray.Depth >= this.russianRouletteLimit){
            val q: Float = max(0.05f, 1.0f - hitColorLum)
            if (this.pcg.RandomFloat() > q)
                hitColor *= (1.0f /(1.0f - q))
            else
                return emittedRadiance
        }
        var cumRadiance: Color = Color(0.0f, 0.0f, 0.0f)
        var newRadiance: Color =  Color(0.0f, 0.0f, 0.0f)
        if (hitColorLum > 0.0f){
            for (rayIndex in 0 until numberOfRays){
                val newRay: Ray = hitMaterial.brdf.ScatterRay(
                    pcg = this.pcg,
                    incoming_dir = hitRecord.ray.Dir,
                    interaction_point = hitRecord.worldPoint,
                    normal = hitRecord.normal,
                    depth = ray.Depth + 1
                )

                newRadiance = this.Render(newRay)
                cumRadiance += hitColor * newRadiance


            }
        }
        return emittedRadiance + cumRadiance * (1.0f / this.numberOfRays.toFloat())
    }
}