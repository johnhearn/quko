package quam

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ComplexVectorShould {

    private val a = 1.0 + 2.0 * i
    private val b = 3.0 + 4.0 * i
    private val c = 5.0 + 6.0 * i
    private val v = ComplexVector(a, b)

    @Test
    fun `have equals method`() {
        assertEquals(ComplexVector(a, b), v)
    }

    @Test
    fun `allow construction with real numbers`() {
        ComplexVector(2.0, 3.0)
    }

    @Test
    fun `compute length`() {
        assertEquals(5.0, ComplexVector(3.0, 4.0).l2norm())
    }

    @Test
    fun `compute scalar product`() {
        val alpha = 1.5
        assertEquals(ComplexVector(alpha * a, alpha * b), alpha * v)
    }

    @Test
    fun `compute inner product`() {
        assertEquals(a.conjugate() * a + b.conjugate() * b, v inner v)
    }

    @Test
    fun `compute outer product`() {
        assertEquals(ComplexMatrix(2, a * a, a * b, b * a, b * b), v outer v)
    }

    @Test
    fun `compute kronecker product`() {
        assertEquals(ComplexVector(a * a, a * b, b * a, b * b), v kronecker v)
    }

    @Test
    fun `compute associative outer product`() {
        val u = ComplexVector(b, c)
        val w = ComplexVector(c, a)
        assertEquals(v kronecker (u kronecker w), (v kronecker u) kronecker w)
    }

    @Test
    fun `compute normal`() {
        val u = ComplexVector(2.0, 3.0).normalise()
        assertEquals(1.0, u.l2norm())
    }
}
