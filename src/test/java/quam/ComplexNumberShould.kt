package quam

import assertk.assert
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ComplexNumberShould {

    private val complexNumber = 1.0 + 2.0 * i

    @Test
    fun `be equal`() {
        assertEquals(ComplexNumber(1.0, 2.0), complexNumber)
    }

    @Test
    fun `have useful toString`() {
        val a = 1.0 + Math.PI * i
        assertEquals("1 + i3.142", a.toString())
    }

    @Test
    fun `i*i = -1`() {
        assertEquals(ComplexNumber(-1.0, 0.0), i * i)
    }

    @Test
    fun `have addition operator`() {
        assertEquals(ComplexNumber(2.0, 4.0), complexNumber + complexNumber)
    }

    @Test
    fun `have subtraction operator`() {
        assertEquals(ComplexNumber(0.0, 0.0), complexNumber - complexNumber)
    }

    @Test
    fun `have unitary negation operator`() {
        assertEquals(ComplexNumber(-1.0, -2.0), -complexNumber)
    }

    @Test
    fun `have matrix product operator`() {
        assertEquals(ComplexNumber(-3.0, 4.0), complexNumber * complexNumber)
    }

    @Test
    fun `expand from expression`() {
        assertEquals(ComplexNumber(-3.0, 4.0), -3.0 + 4.0 * i)
    }

    @Test
    fun `compute magnitude`() {
        assertEquals(5.0, (3.0 + 4.0 * i).l2norm())
    }

    @Test
    fun `compute conjugate`() {
        assertEquals(3.0 - 4.0*i, (3.0 + 4.0 * i).conjugate())
    }

    @Test
    fun `compute from complex exponent`() {
        assert(exp(Math.PI * i)).isCloseTo(ComplexNumber(-1.0))
    }

}
