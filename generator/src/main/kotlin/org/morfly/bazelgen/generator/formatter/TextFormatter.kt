package org.morfly.bazelgen.generator.formatter


/**
 *
 */
val LINE_SEPARATOR: String = System.getProperty("line.separator")


/**
 * @param T
 */
interface TextFormatter<T> {

    /**
     * @param arg
     * @return
     */
    fun format(arg: T): String

    /**
     * @param arg
     * @return
     */
    fun validate(arg: T) {}
}


/**
 * Alias for [TextFormatter.format] function for shorter calls.
 */
operator fun <T> TextFormatter<T>.invoke(arg: T): String =
    format(arg)