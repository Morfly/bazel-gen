package org.morfly.bazelgen.generator.dsl.core.element


/**
 *
 */
sealed class Reference(open val name: String) : StarlarkDslElement

/**
 *
 */
data class StringReference(override val name: String) : Reference(name),
    CharSequence by name {

    override fun toString() = name
}

/**
 *
 */
data class ListReference<T>(override val name: String) : Reference(name),
    List<T> by emptyList()

/**
 *
 */
data class DictionaryReference(override val name: String) : Reference(name),
    Map<String, Any?> by emptyMap()


/**
 *
 */
data class AnyReference(override val name: String) : Reference(name),
    // TODO document workaround description
    Function1<Any?, Unit> {

    override fun toString() = name

    override fun invoke(p1: Any?) {}
}