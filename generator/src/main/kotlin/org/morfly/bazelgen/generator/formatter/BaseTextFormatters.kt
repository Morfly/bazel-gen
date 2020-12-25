package org.morfly.bazelgen.generator.formatter


/**
 *
 */
class BaseTextFormatter : TextFormatter<Any?> {

    override fun format(arg: Any?): String {
        validate(arg)

        return arg.format()
    }
}

/**
 *
 */
class QuoteFormatter : TextFormatter<String> {

    override fun format(arg: String): String {
        validate(arg)

        val isMultiline = LINE_SEPARATOR in arg
        return if (!arg.containsQuotedSubstring(isMultiline))
            arg.quote(isMultiline)
        else arg
    }
}


/**
 *
 */
fun Any?.format(): String =
    when (this) {
        null -> "None"
        is Boolean -> if (this) "True" else "False"
        else -> toString()
    }

/**
 *
 */
fun Any?.isLiteral(): Boolean =
    when (this) {
        null, is Number, is Boolean -> true
        else -> false
    }

/**
 * Surrounds the single-line string with single quotes or multiline string with triple quotes.
 * Ignores the input string if it is already quoted.
 *
 * @param isMultiline allows to explicitly specify whether the function will treat this string as a single-line or a
 * multiline string. If 'null' is passed, the string type will be identified by the function itself.
 */
fun String.quote(isMultiline: Boolean? = null): String =
    if (!isQuoted(isMultiline)) {
        val openingQuote =
            if (isMultiline == true || (isMultiline == null && LINE_SEPARATOR in this)) "\"\"\"$LINE_SEPARATOR"
            else "\""
        val closingQuote =
            if (isMultiline == true || (isMultiline == null && LINE_SEPARATOR in this)) "$LINE_SEPARATOR\"\"\""
            else "\""
        "$openingQuote$this$closingQuote"
    } else this

/**
 * Determines whether the single-line string is surrounded by single quotes OR the multiline string is surrounded by
 * triple quotes.
 *
 * @param isMultiline allows to explicitly specify whether the function will treat this string as a single-line or a
 * multiline string. If 'null' is passed, the string type will be identified by the function itself.
 */
fun String.isQuoted(isMultiline: Boolean? = null): Boolean {
    val quote =
        if (isMultiline == true || (isMultiline == null && LINE_SEPARATOR in this)) "\"\"\""
        else "\""
    return startsWith(quote) && endsWith(quote)
}

/**
 *
 * @param isMultiline allows to explicitly specify whether the function will treat this string as a single-line or a
 * multiline string. If 'null' is passed, the string type will be identified by the function itself.
 */
fun String.containsQuotedSubstring(isMultiline: Boolean? = null): Boolean =
    if (isMultiline == true || (isMultiline == null && LINE_SEPARATOR in this)) {
        val quotesNum = windowed(3) { if (it == "\"\"\"") 1 else 0 }.sum()
        quotesNum >= 2
    } else count { it == '"' } >= 2

/**
 * Unwraps the single-line string of single quotes or multiline string of triple quotes.
 *
 * @param isMultiline allows to explicitly specify whether the function will treat this string as a single-line or a
 * multiline string. If 'null' is passed, the string type will be identified by the function itself.
 */
fun String.unquote(isMultiline: Boolean? = null): String =
    if (isQuoted(isMultiline)) {
        if (isMultiline == true || (isMultiline == null && LINE_SEPARATOR in this))
            removeSurrounding("\"\"\"").removeSurrounding(LINE_SEPARATOR)
        else removeSurrounding("\"")
    } else this