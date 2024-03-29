import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import kotlin.math.pow

internal class VecTest {

    @Test
    fun plus() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 3.0f, 4.0f)
        val sum: Vec = Vec(3.0f, 5.0f, 7.0f)
        val vec3: Vec = vec1 + vec2
        assertEquals(sum, vec3)
    }

    @Test
    fun times() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val scalar: Float = 2.0f
        val res: Vec = Vec(2.0f, 4.0f, 6.0f)
        assertEquals(res, vec * scalar)
    }

    @Test
    fun neg() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val negvec: Vec = Vec(-1.0f, -2.0f, -3.0f)
        assertEquals(negvec, vec.Neg())
    }

    @Test
    fun minus() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 4.0f, 2.0f)
        val vecsub: Vec = Vec(-1.0f, -2.0f, 1.0f)
        assertEquals(vecsub, vec1 - vec2)
    }

    @Test
    fun testTimes() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 4.0f, 2.0f)
        val dot: Float = 16.0f
        assertEquals(dot, vec1 * vec2)
    }

    @Test
    fun squaredNorm() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val norm2: Float = 14.0f
        assertEquals(norm2, vec.SquaredNorm())
    }

    @Test
    fun norm() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        assertEquals(14.0f, vec.Norm() * vec.Norm(), 1e-5f)
    }

    @Test
    fun cross() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(2.0f, 3.0f, 4.0f)
        val cross: Vec = Vec(-1.0f, 2.0f, -1.0f)
        assertEquals(cross, vec1.Cross(vec2))
    }

    @Test
    fun normalize() {
        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        val norm = vec.Norm()
        val normalized: Vec = Vec(1.0f / norm, 2.0f / norm, 3.0f / norm)
        assertEquals(normalized, vec.Normalize())
    }

    @Test
    fun vecall() {
        val vec1: Vec = Vec(1.0f, 2.0f, 3.0f)
        val vec2: Vec = Vec(4.0f, 6.0f, 8.0f)
        assert(vec1.IsClose(vec1))
        assert(!vec1.IsClose(vec2))
        assert((-vec1).IsClose(Vec(-1.0f, -2.0f, -3.0f)))
        assert((vec1 + vec2).IsClose(Vec(5.0f, 8.0f, 11.0f)))
        assert((vec2 - vec1).IsClose(Vec(3.0f, 4.0f, 5.0f)))
        assert((vec1 * 2).IsClose(Vec(2.0f, 4.0f, 6.0f)))
        assert(IsClose(40.0f, vec1 * vec2))
        assert(vec1.Cross(vec2).IsClose(Vec(-2.0f, 4.0f, -2.0f)))
        assert(IsClose(14.0f, vec1.SquaredNorm()))
        assert(IsClose(14.0f, vec1.Norm().pow(2)))

    }
}

internal class NormalTest {

    @Test
    fun TestNormal(){
        val normal: Normal = Normal(1.0f, 2.0f, 3.0f)
        val normalneg: Normal = Normal(-1.0f, -2.0f, -3.0f)
        println(normal.Neg())
        assert(normal.Neg().IsClose(normalneg))
        normal.Normalize()
        val norm: Float = normal.Norm()
        assertEquals(norm, 1.0f, 1e-5f)
    }

    @Test
    fun createOnbFromZ(){
        val pcg: PCG = PCG()

        for (i in (0 until 100)){
            val normal: Normal = Normal(pcg.RandomFloat(), pcg.RandomFloat(), pcg.RandomFloat())
            val (e1, e2, e3) = createOnbFromZ(normal)

            assert(e3.toNormal().IsClose(normal))
            assert(IsClose(e1 * e2, 0.0f))
        }
    }
}

internal class PointTest {

    companion object {
        val a = Point(1.0f, 2.0f, 3.0f)
        val b = Point(4.0f, 6.0f, 8.0f)
        val v = Vec(4.0f, 6.0f, 8.0f)
        val point: Point = Point(1.0f, 2.0f, 4.0f)
    }

    @Test
    fun point_tostring(){
        Assert.assertEquals(point.PointToString(), "Point: (x:1.0, y:2.0, z:4.0)")
    }

    @Test
    fun is_close() {
        assert(a.IsClose(Point(1.0f, 2.0f, 3.0f)))
        assert(!a.IsClose(b))
    }

    @Test
    fun point_to_vec(){
        assertEquals(a.PointToVec(), Vec(1.0f, 2.0f, 3.0f))
    }

    @Test
    fun operations_overload(){
        assert((a * 2).IsClose(Point(2.0f, 4.0f, 6.0f)))
        assert((a + v).IsClose(Point(5.0f, 8.0f, 11.0f)))
        assert((a - v).IsClose(Point(-3.0f, -4.0f, -5.0f)))
    }

    @Test
    fun test_points(){
        assert(a.IsClose(a))
        assert(!a.IsClose(b))
        assert((a * 2).IsClose(Point(2.0f, 4.0f, 6.0f)))
        assert((a + v).IsClose(Point(5.0f, 8.0f, 11.0f)))
        assert((b - a).IsClose(Vec(3.0f, 4.0f, 5.0f)))
        assert((a - v).IsClose(Point(-3.0f, -4.0f, -5.0f)))
    }

}

internal class TransformationTesting() {
    companion object {
        val VEC_X : Vec = Vec(1.0f, 0.0f, 0.0f)
        val VEC_Y : Vec = Vec(0.0f, 1.0f, 0.0f)
        val VEC_Z : Vec = Vec(0.0f, 0.0f, 1.0f)

        val testM: Array<FloatArray> = arrayOf(
                floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f),
                floatArrayOf(5.0f, 6.0f, 7.0f, 8.0f),
                floatArrayOf(9.0f, 9.0f, 8.0f, 7.0f),
                floatArrayOf(6.0f, 5.0f, 4.0f, 1.0f)
            )
        

        val testInvM:Array<FloatArray> = arrayOf(
            
                floatArrayOf(-3.75f, 2.75f, -1.0f, 0.0f),
               floatArrayOf( 4.375f, -3.875f, 2.0f, -0.5f),
                floatArrayOf(0.5f, 0.5f, -1.0f, 1.0f),
                floatArrayOf(-1.375f, 0.875f, 0.0f, -0.5f)
            )
        

        val identity: Array<FloatArray> = ID4
    }

    @Test
    fun isConsistentTest(){
        var product: Array<FloatArray> = ID4
        val tr: Transformation = Transformation(testM, testInvM)
        assert(tr.isConsistent())

    }

    @Test
    fun TransMult(){
        val m1: Transformation = Transformation(
            m=  arrayOf(
                
                    floatArrayOf(1.0f, 2.0f, 3.0f, 4.0f),
                    floatArrayOf(5.0f, 6.0f, 7.0f, 8.0f),
                    floatArrayOf(9.0f, 9.0f, 8.0f, 7.0f),
                    floatArrayOf(6.0f, 5.0f, 4.0f, 1.0f)
                )
            ,
            invm = arrayOf(
                    floatArrayOf(-3.75f, 2.75f, -1.0f, 0.0f),
                    floatArrayOf(4.375f, -3.875f, 2.0f, -0.5f),
                    floatArrayOf(0.5f, 0.5f, -1.0f, 1.0f),
                    floatArrayOf(-1.375f, 0.875f, 0.0f, -0.5f)
                )
            )
        

        val m2: Transformation = Transformation(
            m=  arrayOf(
                    floatArrayOf(3.0f, 5.0f, 2.0f, 4.0f),
                    floatArrayOf(4.0f, 1.0f, 0.0f, 5.0f),
                    floatArrayOf(6.0f, 3.0f, 2.0f, 0.0f),
                    floatArrayOf(1.0f, 4.0f, 2.0f, 1.0f)
                )
            ,
            invm=  arrayOf(
                    floatArrayOf(0.4f, -0.2f, 0.2f, -0.6f),
                    floatArrayOf(2.9f, -1.7f, 0.2f, -3.1f),
                    floatArrayOf(-5.55f, 3.15f, -0.4f, 6.45f),
                    floatArrayOf(-0.9f, 0.7f, -0.2f, 1.1f)
                )
            )
        

        val expected = Transformation(
            m=  arrayOf(
                    floatArrayOf(33.0f, 32.0f, 16.0f, 18.0f),
                    floatArrayOf(89.0f, 84.0f, 40.0f, 58.0f),
                    floatArrayOf(118.0f, 106.0f, 48.0f, 88.0f),
                    floatArrayOf(63.0f, 51.0f, 22.0f, 50.0f)
                )
            ,
            invm = arrayOf(
                    floatArrayOf(-1.45f, 1.45f, -1.0f, 0.6f),
                    floatArrayOf(-13.95f, 11.95f, -6.5f, 2.6f),
                    floatArrayOf(25.525f, -22.025f, 12.25f, -5.2f),
                    floatArrayOf(4.825f, -4.325f, 2.5f, -1.1f)
                )
            )
        
        assertTrue(Transformation().areMatrixClose(expected.m, (m1*m2).m))
      
    }

    @Test
    fun TransfornationClassic() {

        val vec: Vec = Vec(1.0f, 2.0f, 3.0f)
        var tr1: Transformation = Transformation().Translation(vec)
        assert(tr1.isConsistent())

        var tr2: Transformation = Transformation().Translation(Vec(4.0f, 6.0f, 8.0f))
        assert(tr2.isConsistent())

        var prod = tr1 * tr2
        assert(prod.isConsistent())

        var expected : Transformation = Transformation().Translation(Vec(5.0f, 8.0f, 11.0f))
        assert(prod.isClose(expected))

        var tr3: Transformation = Transformation().RotationX(0.1f)
        assert(tr3.isConsistent())
        var tr4: Transformation = Transformation().RotationY(0.1f)
        assert(tr4.isConsistent())
        
        var tr5: Transformation = Transformation().RotationZ(0.1f)
        
        assert(tr5.isConsistent())

        tr3 = Transformation().RotationX(90f)
        var vec2 = tr3 * VEC_Y
        assert(vec2.IsClose(VEC_Z))
        tr3 = Transformation().RotationY(90f)
        vec2 = tr3 * VEC_Z
        assert(vec2.IsClose(VEC_X))
        tr3 = Transformation().RotationZ(90f)
        vec2 = tr3 * VEC_X
        assert(vec2.IsClose(VEC_Y))

        tr3 = Transformation().Scaling(Vec(2.0f, 5.0f, 10.0f))
        assert(tr3.isConsistent())
        tr4 = Transformation().Scaling(Vec(3.0f, 2.0f, 4.0f))
        assert(tr4.isConsistent())

        var expected_tr : Transformation = Transformation().Scaling(Vec(6.0f, 10.0f, 40.0f))
        assert(expected_tr.isClose(tr3*tr4))

    }
}