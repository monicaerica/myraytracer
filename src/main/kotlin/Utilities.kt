import kotlin.math.abs

fun IsClose(first: Float, second: Float, epsilon: Float = 1e-5f): Boolean{
    var bool: Boolean
    if (abs(first - second) < epsilon){
        bool = true
    }
    else{
        bool = false
    }
    return bool
}

fun IsClose(first: Int, second: Int, epsilon: Float = 1e-5f): Boolean{
    var bool: Boolean
    if (abs(first - second) < epsilon){
        bool = true
    }
    else{
        bool = false
    }
    return bool
}

fun IsClose(first: Double, second: Double, epsilon: Float = 1e-5f): Boolean{
    var bool: Boolean
    if (abs(first - second) < epsilon){
        bool = true
    }
    else{
        bool = false
    }
    return bool
}

fun MatrixProduct(a: HomMatrix, b: HomMatrix): HomMatrix {
    var c: HomMatrix = HomMatrix()
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            var sum: Float = 0.0f
            for (k in 0 until 4) {
                sum += a.GetIndex(k, i) * b.GetIndex(j, k)
            }
            c.elements[4 * i + j] = sum
        }
    }
    return c
}

class HomMatrix(var elements: FloatArray){
    init {
        require(elements.size == 16) {"Matrix must be 4x4"}
    }

    constructor() : this(
        floatArrayOf(
            1.0f, 0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f
        )
    )

    fun GetIndex(i: Int, j: Int): Float{
        return this.elements.elementAt((4 * j) + i)
    }

    fun BooleanToInt(b: Boolean): Int {
        return if (b) 0 else 1
    }

    fun IsClose(other: HomMatrix, epsilon: Float = 1e-5f): Boolean{
        var sum: Int = 0
        var result: Boolean = false
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                for (k in 0 until 4) {
                    result = ((this.GetIndex(i, j) - other.GetIndex(i, j)) < epsilon)
                    sum += BooleanToInt(result)
                }
            }
        }
        return (sum == 0)
    }
}