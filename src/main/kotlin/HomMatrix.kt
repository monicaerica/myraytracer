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
        return this.elements.elementAt(i + 3 * j)
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