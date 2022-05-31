//A Data Class is like a regular class but with some additional functionalities.
//
//With Kotlin’s data classes, you don’t need to write/generate all the lengthy
//boilerplate code yourself. The compiler automatically generates a default
//getter and setter for all the mutable properties, and a getter (only)
//for all the read-only properties of the data class. Moreover, It also
//derives the implementation of standard methods like equals(), hashCode()
//and toString() from the properties declared in the data class’s primary
//constructor.
//https://www.callicoder.com/kotlin-data-classes/

import java.lang.Math.pow
import kotlin.math.pow

/**
 * Generates random integer positive numbers of 32 bits in the [0, 2^32-1] interval
 * PCG algorithm
 */
data class PCG(val init_state : ULong = 42u, val init_seq : ULong = 54u,
               var state : ULong = 0u,
               var inc : ULong = 0u){
    init{
        this.inc = (init_seq shl 1) or 1u
        this.Random()
        this.state += init_state
        this.Random()
    }
    /**
     * Generates random integer positive numbers of 32 bits in the [0, 2^32-1] interval
     * PCG algorithm
     * @return UInt random number
     */
    fun Random(): UInt{
        var oldstate = this.state
        this.state = oldstate * 6364136223846793005u + this.inc
        var xorshifted = (((oldstate shr 18) xor oldstate) shr 27).toUInt()
        var rot = (oldstate shr 59).toUInt()
        return ((xorshifted shr rot.toInt()) or ( xorshifted shl ((-rot.toInt()) and 31).toInt())).toUInt()
    }

    /**
     * Generates random float numbers
     * PCG algorithm
     * @return Float random number
     */
    fun RandomFloat(): Float{
        return ((this.Random().toFloat())/2f.pow(32))
    }
}