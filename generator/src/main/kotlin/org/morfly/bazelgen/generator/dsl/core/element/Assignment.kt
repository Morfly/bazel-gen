package org.morfly.bazelgen.generator.dsl.core.element


/**
 *
 */
interface Assignment<out T> : StarlarkDslElement {
    /**
     *
     */
    val name: String

    /**
     *
     */
    val value: T?
}


/**
 *
 */
data class ExpressionAssignment<out T : Expression>(
    override val name: String,
    override val value: T
) : Assignment<T>