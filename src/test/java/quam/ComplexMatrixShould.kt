package quam

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ComplexMatrixShould {

    @Test
    fun `build from integers`() {
        val lhs = ComplexMatrix(2, 1, 1, 0, 0)
        val expected = ComplexMatrix(2, 1.0, 1.0, 0.0, 0.0)
        assertEquals(expected, lhs)
    }

    /**
     * (a b) => (a  c*)
     * (c d)    (b* d)
     */
    @Test
    fun `compute transpose`() {
        val a = 1.0 + 2.0 * i
        val b = 3.0 + 4.0 * i
        val c = 5.0 + 6.0 * i
        val d = 7.0 + 8.0 * i
        val lhs = ComplexMatrix(2, a, b, c, d)
        val expected = ComplexMatrix(2, a, c, b, d)
        assertEquals(expected, lhs.transpose())
    }

    /**
     * alpha*(a b)
     *       (c d)
     */
    @Test
    fun `compute product with scalar`() {
        val a = 1.0 + 2.0 * i
        val b = 3.0 + 4.0 * i
        val c = 5.0 + 6.0 * i
        val d = 7.0 + 8.0 * i
        val rhs = ComplexMatrix(2, a, b, c, d)
        val alpha = 1.5
        val expected = ComplexMatrix(2, alpha * a, alpha * b, alpha * c, alpha * d)
        assertEquals(expected, alpha * rhs)
    }

    /**
     * (a b)(a)=(aa+bb)
     * (c d)(b) (ca+db)
     */
    @Test
    fun `compute product with vector`() {
        val a = 1.0 + 2.0 * i
        val b = 3.0 + 4.0 * i
        val c = 5.0 + 6.0 * i
        val d = 7.0 + 8.0 * i
        val matrix = ComplexMatrix(2, a, b, c, d)
        val vector = ComplexVector(a, b)
        val expected = ComplexVector(a * a + b * b, c * a + d * b)
        assertEquals(expected, matrix * vector)
    }

    /**
     * (a b)(a b)=(aa+bc ab+bd)
     * (c d)(c d) (ca+dc cb+dd)
     */
    @Test
    fun `compute product with matrix`() {
        val a = 1.0 + 2.0 * i
        val b = 3.0 + 4.0 * i
        val c = 5.0 + 6.0 * i
        val d = 7.0 + 8.0 * i
        val matrix1 = ComplexMatrix(2, a, b, c, d)
        val matrix2 = ComplexMatrix(2, a, b, c, d)
        val expected = ComplexMatrix(2, a * a + b * c, a * b + b * d, c * a + d * c, c * b + d * d)
        assertEquals(expected, matrix1 * matrix2)
    }

    /**
     * (a b)x(a c) = (aa ac ba bc)
     * (c d) (b d)   (ab ad bb bd)
     *               (ca cc da dc)
     *               (cb cd db dd)
     */
    @Test
    fun `compute tensor product`() {
        val a = 1.0 + 2.0 * i
        val b = 3.0 + 4.0 * i
        val c = 5.0 + 6.0 * i
        val d = 7.0 + 8.0 * i
        val lhs = ComplexMatrix(2, a, b, c, d)
        val rhs = ComplexMatrix(2, a, c, b, d)
        val expected = ComplexMatrix(4,
                a * a, a * c, b * a, b * c,
                a * b, a * d, b * b, b * d,
                c * a, c * c, d * a, d * c,
                c * b, c * d, d * b, d * d)
        val actual = lhs tensor rhs
        assertEquals(expected, actual)
    }

    @Test
    fun `have useful toString`() {
        val a = 1.0 + Math.PI * i
        val b = 3.0 + 4.0 * i
        val c = 5.0 + 0.0 * i
        val d = 7.0 + 8.0 * i
        val matrix = ComplexMatrix(2, a, b, c, d)
        assertEquals(
                "1 + i3.142 | 3 + i4\n" +
                        "5 | 7 + i8", matrix.toString())
    }
}
