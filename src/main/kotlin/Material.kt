data class Material (val brdf: BRDF = DiffuseBRDF(), val emitted_radiance: Pigment = UniformPigment(BLACK)){
}