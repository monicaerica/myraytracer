import org.junit.Assert.*
import org.junit.Test

internal class NormalTest {

    @Test
    fun TestNormal(){
        val normal: Normal = Normal(1.0f, 2.0f, 3.0f)
        println(normal.Norm())
        normal.Normalize()
        println(normal.Norm())

//        assertEquals(normal.norm(), 1.0f)
    }
//    def test_vectors(self):
//    a = Vec(1.0, 2.0, 3.0)
//    b = Vec(4.0, 6.0, 8.0)
//    assert a.is_close(a)
//    assert not a.is_close(b)
//
//    def test_vector_operations(self):
//    a = Vec(1.0, 2.0, 3.0)
//    b = Vec(4.0, 6.0, 8.0)
//    assert (-a).is_close(Vec(-1.0, -2.0, -3.0))
//    assert (a + b).is_close(Vec(5.0, 8.0, 11.0))
//    assert (b - a).is_close(Vec(3.0, 4.0, 5.0))
//    assert (a * 2).is_close(Vec(2.0, 4.0, 6.0))
//    assert pytest.approx(40.0) == a.dot(b)
//    assert a.cross(b).is_close(Vec(-2.0, 4.0, -2.0))
//    assert b.cross(a).is_close(Vec(2.0, -4.0, 2.0))
//    assert pytest.approx(14.0) == a.squared_norm()
//    assert pytest.approx(14.0) == a.norm() ** 2


}