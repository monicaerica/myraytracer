import jdk.dynalink.linker.support.Guards.isInstance
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.io.InputStreamReader

fun assertIsKeyword(token: Token, keyword: keywordEnum) {
    assert(token is keywordToken)
}

fun assertIsIdentifier(token: Token, identifier: String) {
    assert(token is identifierToken)
}

fun assertIsSymbol(token: Token, Symbol: String) {
    assert(token is symbolToken)
}

fun assertIsNumber(token: Token, number: String) {
    assert(token is literalNumberToken)
}

fun assertIsString(token: Token, s: String) {
    assert(token is stringToken)
}

internal class InputStreamTest() {
    @Test
    fun testInputFile() {
        val stream: InputStream = InputStream(InputStreamReader("abc \nd\nef".byteInputStream()))
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.ReadChar() == 'a')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 2)

        stream.UnreadChar('A')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.ReadChar() == 'A')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.ReadChar() == 'b')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 3)

        assertTrue(stream.ReadChar() == 'c')
        assertTrue(stream.location.line_num == 1)
        assertTrue(stream.location.col_num == 4)

        stream.skipWhitespaceAndComents()

        assertTrue(stream.ReadChar() == 'd')
        assertTrue(stream.location.line_num == 2)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.ReadChar() == '\n')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 1)

        assertTrue(stream.ReadChar() == 'e')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 2)

        assertTrue(stream.ReadChar() == 'f')
        assertTrue(stream.location.line_num == 3)
        assertTrue(stream.location.col_num == 3)

        assertTrue(stream.ReadChar() == null)

    }

    @Test
    fun testLexer() {
        val inFile: InputStream = InputStream(
            InputStreamReader(
                """
        # This is a comment
        # This is another comment
        new material sky_material(
            diffuse(image("my file.pfm")),
            <5.0, 500.0, 300.0>
        ) # Comment at the end of the line
""".byteInputStream()
            )
        )

        assertIsKeyword(inFile.readToken(), keywordEnum.NEW)
        assertIsKeyword(inFile.readToken(), keywordEnum.MATERIAL)
        assertIsIdentifier(inFile.readToken(), "sky_material")
        assertIsSymbol(inFile.readToken(), "(")
        assertIsKeyword(inFile.readToken(), keywordEnum.DIFFUSE)
        assertIsSymbol(inFile.readToken(), "(")
        assertIsKeyword(inFile.readToken(), keywordEnum.IMAGE)
        assertIsSymbol(inFile.readToken(), "(")
        assertIsString(inFile.readToken(), "my file.pfm")
        assertIsSymbol(inFile.readToken(), ")")
    }

}

internal class scene_test {
    @Test
    fun test_parser() {
        val inFile = InputStream(InputStreamReader("""
        float clock(150)
    
        material sky_material(
            diffuse(uniform(<0, 0, 0>)),
            uniform(<0.7, 0.5, 1>)
        )
    
        # Here is a comment
    
        material ground_material(
            diffuse(checkered(<0.3, 0.5, 0.1>,
                              <0.1, 0.2, 0.5>, 4)),
            uniform(<0, 0, 0>)
        )
    
        material sphere_material(
            specular(uniform(<0.5, 0.5, 0.5>)),
            uniform(<0, 0, 0>)
        )
    
        sphere(sphere_material, translation([0, 0, 1]))
    
        camera(perspective, rotationz(30), 1.0, 2.0)
        """.byteInputStream()
            )
        )

        var scene = parseScene(inFile)

        //Check that the float variables are ok

        assert(scene.floatVariables.size == 1)
        assert("clock" in scene.floatVariables.keys)
        assert( scene.floatVariables["clock"] == 150.0f)

        //Check that the materials are ok
        assert(scene.materials.size ==  3)
        assert("sphere_material" in scene.materials)
        assert("sky_material" in scene.materials)
        assert("ground_material" in scene.materials)

        var sphere_material = scene.materials["sphere_material"]
        var sky_material = scene.materials["sky_material"]
        var ground_material = scene.materials["ground_material"]

        assert(sky_material !== null)
        if (sky_material != null) {
            assert(sky_material.brdf is DiffuseBRDF)
            assert(sky_material.brdf.pigment is UniformPigment)
//            assert(sky_material.brdf.pigment.GetColor(Vec2d(0.5f, 0.5f)).IsClose(Color(0.0f, 0.0f, 0.0f)))

        }

        assert(ground_material !== null)
        println(sphere_material)
        if (sphere_material != null && ground_material != null) {
//            println(sphere_material.brdf)
//            println(sphere_material.brdf.pigment)
//            println(sphere_material.brdf.pigment.GetColor(Vec2d(0.0f, 0.0f)))
//            println(ground_material)
//            println(ground_material.brdf)
//            println(ground_material.brdf.pigment)
//            println(ground_material.brdf.pigment.GetColor(Vec2d(0.0f, 0.0f)))
//            assert(ground_material.brdf.pigment is CheckredPigment)
        }
////        if (ground_material != null) {
////            assert(ground_material.brdf is DiffuseBRDF)
////        }
////        if (ground_material != null) {
////            assert(ground_material.brdf.pigment.GetColor(Vec2d(0.1f, 0.1f)).IsClose(Color(0.3f, 0.5f, 0.1f)))
////            assert(ground_material.brdf.pigment.GetColor(Vec2d(0.1f, 0.3f)).IsClose(Color(0.1f, 0.2f, 0.5f)))
////        //        assert ground_material.brdf.pigment.color1.is_close(Color(0.3, 0.5, 0.1))
//////        assert ground_material.brdf.pigment.color2.is_close(Color(0.1, 0.2, 0.5))
//////        assert ground_material.brdf.pigment.num_of_steps == 4
////        }
//        assert(sphere_material !== null)
//        if (sphere_material != null) {
//            assert(sphere_material.brdf is SpecularBRDF)
//            assert(sphere_material.brdf.pigment is UniformPigment)
//            assert(sphere_material.brdf.pigment.GetColor(Vec2d(0.0f, 0.0f)).IsClose(Color(0.5f, 0.5f, 0.5f)))
//        }

////        if (sky_material != null) {
////            assert(sky_material.emitted_radiance is UniformPigment)
////            assert(sky_material.emitted_radiance.GetColor(Vec2d(0.0f, 0.0f)).IsClose(Color(0.7f, 0.5f, 1.0f)))
////        }
////        if (ground_material != null) {
////            assert(ground_material.emitted_radiance is UniformPigment)
////            assert(ground_material.emitted_radiance.GetColor(Vec2d(0.0f, 0.0f)).IsClose(Color(0.0f, 0.0f, 0.0f)))
////        }
////        if (sphere_material != null) {
////            assert(sphere_material.emitted_radiance is UniformPigment)
////            assert(sphere_material.emitted_radiance.GetColor(Vec2d(0.0f, 0.0f)).IsClose(Color(0.0f, 0.0f, 0.0f)))
////        }
//        //Check that the shapes are ok
//
//
    }
}