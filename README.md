# Simple Quantum CPU Simulator in Kotlin

This collection of classes simulates a system of qubits, at the moment up to about 10 of them.

The simplest possible program for a quantum 8-sided dice:

```kotlin
import quam.*

val qubits = Qubits(3).hadamard(0..2)
print(qubits.measureAll().toInt())
```

This script creates a 3-bit register, applies Hadamard gates to each one and then measures the result as an integer.

For more examples see the features package.