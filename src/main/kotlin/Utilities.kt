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