package org.morfly.bazelgen.generator.dsl.core.element


/**
 *
 */
sealed class Concatenation<out L : Expression, out R : Expression>(
    open val left: L,
    open val operator: String,
    open val right: R
) : Expression(), StarlarkDslElement

/**
 *
 */
data class StringConcatenation(
    override val left: CharSequence?,
    override val operator: String,
    override val right: CharSequence?
) : Concatenation<CharSequence?, CharSequence?>(left, operator, right),
    CharSequence by "StringConcatenation($left$operator$right)"

/**
 *
 */
data class ListConcatenation<T>(
    override val left: List<T>?,
    override val operator: String,
    override val right: List<T>?
) : Concatenation<List<T>?, List<T>?>(left, operator, right),
    List<T> by emptyList()

/**
 *
 */
data class DictionaryConcatenation(
    override val left: Map<String, Any?>?,
    override val operator: String,
    override val right: Map<String, Any?>?
) : Concatenation<Map<String, Any?>?, Map<String, Any?>?>(left, operator, right),
    Map<String, Any?> by emptyMap()

/**
 *
 */
data class AnyConcatenation<out L : Expression, out R : Expression>(
    override val left: L,
    override val operator: String,
    override val right: R
) : Concatenation<L, R>(left, operator, right)