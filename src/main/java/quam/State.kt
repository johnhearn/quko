package quam

class State(initialState: ComplexVector = FALSE, private val gateBuilder: GateBuilder = GateBuilder()) {

    val components = initialState

    val size = log2n(initialState.components.size)

    fun apply(k : Int, gate: ComplexMatrix) = State(gateBuilder.lift(size, k, gate) * components)

    fun apply(k: Int, j: Int, gate: ComplexMatrix) = State(gateBuilder.lift(size, k, j, gate) * components)

    infix fun kronecker(other: State) = State(components kronecker other.components)

    fun probFalse(k: Int = 0) = (components inner (gateBuilder.lift(size, k, M0) * components)).l2norm()

    override fun equals(other: Any?) = when(other) {
        is State -> components == other.components
        else -> false
    }

    override fun toString()= components.toString()

    internal fun normalise() = State(components.normalise())
}

val FALSE = ComplexVector(1.0, 0.0)
val TRUE = ComplexVector(0.0, 1.0)

private fun log2n(size: Int)
        = (Math.log(1.0 * size) / Math.log(2.0)).toInt()

fun composeState(count: Int) = (2..count).fold(State()) { a, _ -> a kronecker State() }

class Memoize1<in T, out R>(val f: (T) -> R) : (T) -> R {
    private val values = mutableMapOf<T, R>()
    override fun invoke(x: T): R {
        return values.getOrPut(x) { f(x) }
    }
}

fun <T, R> ((T) -> R).memoize(): (T) -> R = Memoize1(this)

