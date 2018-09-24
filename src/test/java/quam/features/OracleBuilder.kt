package quam.features

import org.junit.jupiter.api.Test
import quam.ComplexMatrix
import quam.permutationMatrix
import quam.pow2
import kotlin.test.assertEquals

fun oracle(m: Int, n: Int, f: (Int, Int) -> Int): ComplexMatrix {
    val map = mutableMapOf<Int, Int>()
    for (x in 0 until pow2(m))
        for (y in 0 until pow2(n)) {
            val xy = (x shl n) + y      // |x>|y>
            val fxy = f(x, y)           // |f(x,y)>
            val xfxy = (x shl n) + fxy  // |x>|F(x,y)>
            println("${format(x, m)} ${format(y, n)} | ${format(x, m)} ${format(fxy, n)} | $xy $xfxy")
            map[xy] = xfxy
        }
    return permutationMatrix(pow2(n + m)) { map[it] ?: it }
}

private fun format(x: Int, width: Int) = x.toByte().toString(2).padStart(width, '0')


class OracleBuilderShould {

    @Test
    fun `xor oracle is unitary`() {
        val oracle = oracle(3, 3) { x, y -> x xor y }
        assertEquals(oracle, oracle * oracle.transpose() * oracle)
    }

    @Test
    fun `plus oracle is unitary`() {
        val oracle = oracle(3, 4) { x, y -> (x + y) % pow2(4) }
        assertEquals(oracle, oracle * oracle.transpose() * oracle)
    }

    @Test
    fun `times oracle is unitary`() {
        val a = 3
        val m = 4
        val n = 4
        val oracle = oracle(m, n) { k, y -> `u^k`(y, { x -> x + a }, k, pow2(n)) }
        assertEquals(oracle, oracle * oracle.transpose() * oracle)
    }

    @Test
    fun `power oracle is unitary`() {
        val a = 3
        val m = 3
        val n = 3
        val oracle = oracle(m, n) { k, y -> `u^k`(y, { x -> x * a }, k, pow2(n)) }
        assertEquals(oracle, oracle * oracle.transpose() * oracle)
    }

    private fun `u^k`(y0: Int, f: (Int) -> Int, k: Int, N: Int)
            = (1 until k).fold(y0) { acc, _ -> f(acc) % N }

    @Test
    fun `powk should`() {
        assertEquals(445, `u^k`(445, { x -> x }, 1, 497))
        assertEquals(445, `u^k`(445, { x -> x }, 2, 497))
        assertEquals(445, `u^k`(4, { x -> 4 * x }, 13, 497))
    }

    fun modular_pow(base:Int, exponent:Int, modulus:Int): Int {
        if (modulus == 1) return 0
        var c = 1
        for (e_prime in 0 until exponent)
            c = (c * base) % modulus
        return c
    }

    @Test
    fun `modular_pow`() {
        assertEquals(445, modular_pow(4, 13, 497))
    }
}
