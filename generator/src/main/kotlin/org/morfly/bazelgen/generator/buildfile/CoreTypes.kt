package org.morfly.bazelgen.generator.buildfile


/**
 *
 */
interface Assignment<T> {
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
interface Concatenation<T : Any> {
    /**
     *
     */
    val left: T

    /**
     *
     */
    val operator: String

    /**
     *
     */
    val right: Any?
}

/**
 *
 */
interface FunctionCall {
    /**
     *
     */
    val name: String

    /**
     *
     */
    val args: Map<String, Any?>
}