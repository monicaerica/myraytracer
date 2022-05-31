data class Material (val brdf: BRDF = DiffuseBRDF(), val emitted_radiance: Pigment = UniformPigment(WHITE * 0.1f)){
}