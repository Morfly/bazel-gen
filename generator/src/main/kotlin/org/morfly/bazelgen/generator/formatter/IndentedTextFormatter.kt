package org.morfly.bazelgen.generator.formatter

import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


/**
 * Default number of whitespace characters that should be applied per single indentation step/level.
 */
const val DEFAULT_INDENT_SIZE = 4

/**
 * Indentation mode that helps formatters to understand how exactly arguments should be formatted. This is mostly
 * related to cases when depending on length, argument number, item number, value placement, etc. arguments can be formatted
 * either to single-line or multiline strings.
 */
enum class IndentMode {

    /**
     * Indentation mode which denotes that every line of the argument to be formatted will be printed from a new line.
     * Indentation should be applied to every line of the argument.
     */
    NEW_LINE,

    /**
     * Indentation mode which denotes that the argument continues the line that already contains text.
     * Indentation should be applied to every line of the argument EXCEPT the first one.
     */
    CONTINUE_LINE,

    /**
     * Indentation mode which denotes that the argument should be formatted to a single-line string even if initially it
     * is multiline.
     * No indentation should be applied to any line of the argument.
     * All line separators should be replaced with the whitespace character.
     */
    CONTINUE_SINGLE_LINE
}

/**
 * @param indentSize
 */
abstract class IndentedTextFormatter<T>(private val indentSize: Int) : TextFormatter<T> {

    init {
        require(indentSize >= 0) { "Value of 'indentSize' must be non-negative but was $indentSize." }
    }

    /**
     * @param arg
     * @param indentIndex
     */
    abstract fun format(arg: T, indentIndex: Int, mode: IndentMode): String

    override fun format(arg: T): String =
        format(arg, 0, NEW_LINE)


    /**
     *
     */
    open fun validate(arg: T, indentIndex: Int, mode: IndentMode) {
        validate(arg)
        require(indentIndex >= 0) { "Value of 'indentIndex' must be non-negative but was $indentIndex." }
    }

    /**
     * @param index
     * @return
     */
    protected fun indent(index: Int): String {
        require(index >= 0) { "Indent 'index' must be non-negative but was $index." }

        return " ".repeat(index * indentSize)
    }

    /**
     *
     */
    protected fun String.nextIndent(): String =
        this + " ".repeat(indentSize)
}


/**
 * Alias for [IndentedTextFormatter.format] function for shorter calls.
 */
operator fun <T> IndentedTextFormatter<T>.invoke(arg: T, indentIndex: Int, mode: IndentMode): String =
    format(arg, indentIndex, mode)