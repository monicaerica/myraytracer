import kotlin.math.abs
import kotlin.math.*
fun IsClose(first: Float, second: Float, epsilon: Float = 1e-4f): Boolean{
    var bool: Boolean
    if (abs(first - second) < epsilon){
        bool = true
    }
    else{
        bool = false
    }
    return bool
}

fun IsClose(first: Int, second: Int, epsilon: Float = 1e-4f): Boolean{
    var bool: Boolean
    if (abs(first - second) < epsilon){
        bool = true
    }
    else{
        bool = false
    }
    return bool
}

fun IsClose(first: Double, second: Double, epsilon: Float = 1e-4f): Boolean{
    var bool: Boolean
    if (abs(first - second) < epsilon){
        bool = true
    }
    else{
        bool = false
    }
    return bool
}

// fun MatrixProduct(a: HomMatrix, b: HomMatrix): HomMatrix {
//     var c: HomMatrix = HomMatrix()
//     for (i in 0 until 4) {
//         for (j in 0 until 4) {
//             var sum: Float = 0.0f
//             for (k in 0 until 4) {
//                 sum += a.GetIndex(k, i) * b.GetIndex(j, k)
//             }
//             c.elements[4 * i + j] = sum
//         }
//     }
//     return c
// }

// class HomMatrix(var elements: FloatArray){
//     init {
//         require(elements.size == 16) {"Matrix must be 4x4"}
//     }

//     constructor() : this(
//         floatArrayOf(
//             1.0f, 0.0f, 0.0f, 0.0f,
//             0.0f, 1.0f, 0.0f, 0.0f,
//             0.0f, 0.0f, 1.0f, 0.0f,
//             0.0f, 0.0f, 0.0f, 1.0f
//         )
//     )

//     fun GetIndex(i: Int, j: Int): Float{
//         return this.elements.elementAt((4 * i) + j)
//     }

//     fun BooleanToInt(b: Boolean): Int {
//         return if (b) 0 else 1
//     }

//     fun IsClose(other: HomMatrix, epsilon: Float = 1e-4f): Boolean{
//         var sum: Int = 0
//         var result: Boolean = false
//         for (i in 0 until 4) {
//             for (j in 0 until 4) {
//                 for (k in 0 until 4) {
//                     result = ((this.GetIndex(i, j) - other.GetIndex(i, j)) < epsilon)
//                     sum += BooleanToInt(result)
//                 }
//             }
//         }
//         return (sum == 0)
//     }
// }

val ID4 = Array(4) { i ->
    FloatArray(4) { k ->
        if (k != i) {
            0.0F
        } else {
            1.0F
        }
    }
}

val ID3 = Array(3) { i ->
    FloatArray(3) { k ->
        if (k != i) {
            0.0F
        } else {
            1.0F
        }
    }
}

fun chiGGX(x: Float): Float {
    var chi:Float = 1.0F
    if (x <= 0.0f){
        chi = 0.0F
    }
    return chi
}

fun GGXDistribution(n: Normal, m: Vec, alpha: Float): Float {
    val NdotM: Float = n.Dot(m)
    val alpha2: = alpha * alpha
    val NdotM2: Float = NdotM * NdotM
    val den:Float = NdotM2 * (alpha2 + (1 - NdotM2))
    val num: Float = chiGGX(NdotM) * alpha2
    return num / (PI.toFloat() * den * den)
}

fun Clamp(x: Float): Float{
    return x / (1.0f + x)
}

fun GGXPartialGeometry(omega: Vec, n: Normal, m: Vec, alpha: Float){
    val omegaDotm2: Float = Clamp(m.Dot(omega))
    val chi: Float = chiGGX(omegaDotm2 / (Clamp(n.Dot(omega))))
    omegaDotm2 = omegaDotm2 * omegaDotm2
    val tan2: Float = (1.0f - omegaDotm2) / omegaDotm2
    return (chi * 2) / (1.0f + sqrt(1.0F + alpha * alpha * tan2))
}