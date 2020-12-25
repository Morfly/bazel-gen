@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package org.morfly.bazelgen.generator.formatter

import org.morfly.bazelgen.generator.buildfile.*
import org.morfly.bazelgen.generator.buildfile.ComprehensionType.DICT
import org.morfly.bazelgen.generator.buildfile.ComprehensionType.LIST
import org.morfly.bazelgen.generator.dsl.type.AnyFunctionCall
import org.morfly.bazelgen.generator.dsl.type.ConcatenatedValue
import org.morfly.bazelgen.generator.dsl.type.Reference
import org.morfly.bazelgen.generator.formatter.IndentMode.*


/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class JustTextFormatter(indentSize: Int = DEFAULT_INDENT_SIZE) : IndentedTextFormatter<String>(indentSize) {

    override fun format(text: String, indentIndex: Int, mode: IndentMode): String {
        validate(text, indentIndex, mode)

        val indent = indent(indentIndex)
        return text.lineSequence()
            .mapIndexed { i, line ->
                if (i == 0 && mode == CONTINUE_LINE) line.trimIndent()
                else indent + line
            }
            .joinToString(separator = LINE_SEPARATOR)
    }
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class ListFormatter(
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<List<*>>(indentSize) {

    lateinit var valueFormatter: IndentedTextFormatter<Any?>

    override fun format(list: List<*>, indentIndex: Int, mode: IndentMode): String {
        validate(list, indentIndex, mode)

        val indent = indent(indentIndex)
        val firstLineIndent = when (mode) {
            NEW_LINE -> indent
            CONTINUE_LINE -> ""
            CONTINUE_SINGLE_LINE -> TODO()
        }
        return when (list.size) {
            0 -> "$firstLineIndent[]"
            1 -> firstLineIndent + "[${valueFormatter(list.first(), indentIndex + 1, CONTINUE_LINE)}]"
            else -> {
                list.joinToString(
                    prefix = "$firstLineIndent[$LINE_SEPARATOR",
                    separator = ",$LINE_SEPARATOR",
                    postfix = ",$LINE_SEPARATOR$indent]",
                ) {
                    valueFormatter(it, indentIndex + 1, NEW_LINE)
                }
            }
        }
    }

    override fun validate(list: List<*>, indentIndex: Int, mode: IndentMode) {
        super.validate(list, indentIndex, mode)
        require(::valueFormatter.isInitialized) { notInitializedMessage("valueFormatter") }
    }
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class DictionaryFormatter(
    private val quoteFormatter: TextFormatter<String>,
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<Map<String, *>>(indentSize) {

    lateinit var valueFormatter: IndentedTextFormatter<Any?>

    override fun format(dict: Map<String, *>, indentIndex: Int, mode: IndentMode): String {
        validate(dict, indentIndex, mode)

        val indent = indent(indentIndex)
        val firstLineIndent = when (mode) {
            NEW_LINE -> indent
            CONTINUE_LINE -> ""
            CONTINUE_SINGLE_LINE -> TODO()
        }
        return when (dict.size) {
            0 -> "$firstLineIndent{}"
            1 -> dict.entries.first().let { (key, value) ->
                val quotedKey = quoteFormatter(key)
                val formattedValue = valueFormatter(value, indentIndex + 1, CONTINUE_LINE)
                "$firstLineIndent{$quotedKey: $formattedValue}"
            }
            else -> {
                val nextIndent = indent.nextIndent()
                dict.entries.joinToString(
                    prefix = "$firstLineIndent{$LINE_SEPARATOR",
                    separator = ",$LINE_SEPARATOR",
                    postfix = ",$LINE_SEPARATOR$indent}"
                ) { (key, value) ->
                    val quotedKey = quoteFormatter(key)
                    val formattedValue = valueFormatter(value, indentIndex + 1, CONTINUE_LINE)
                    "$nextIndent$quotedKey: $formattedValue"
                }
            }
        }
    }

    override fun validate(dict: Map<String, *>, indentIndex: Int, mode: IndentMode) {
        super.validate(dict, indentIndex, mode)
        require(::valueFormatter.isInitialized) { notInitializedMessage("valueFormatter") }
    }
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class FunctionCallFormatter(
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<FunctionCall>(indentSize) {

    lateinit var assignmentFormatter: IndentedTextFormatter<Assignment>

    override fun format(func: FunctionCall, indentIndex: Int, mode: IndentMode): String = with(func) {
        validate(func, indentIndex, mode)

        val indent = indent(indentIndex)
        val firstLineIndent = when (mode) {
            NEW_LINE -> indent
            CONTINUE_LINE -> ""
            CONTINUE_SINGLE_LINE -> TODO()
        }
        when {
            args.isEmpty() -> "$firstLineIndent$name()"
            args.size == 1 && (firstArgName.isBlank() || firstArgValue is String) -> {
                val (arg, value) = args.entries.first()
                val assignment = assignmentFormatter(arg to value, indentIndex, mode = CONTINUE_LINE)
                "$firstLineIndent$name($assignment)"
            }
            else -> {
                args.entries.joinToString(
                    prefix = "$firstLineIndent$name($LINE_SEPARATOR",
                    separator = ",$LINE_SEPARATOR",
                    postfix = ",$LINE_SEPARATOR$indent)"
                ) { (arg, value) ->
                    assignmentFormatter(arg to value, indentIndex + 1, mode = NEW_LINE)
                }
            }
        }
    }

    override fun validate(func: FunctionCall, indentIndex: Int, mode: IndentMode) {
        super.validate(func, indentIndex, mode)
        require(::assignmentFormatter.isInitialized) { notInitializedMessage("assignmentFormatter") }
    }

    private val FunctionCall.firstArgName: String
        get() = args.entries.first().key

    private val FunctionCall.firstArgValue: Any?
        get() = args.entries.first().value
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class ValueFormatter(
    private val baseFormatter: TextFormatter<Any?>,
    private val quoteFormatter: TextFormatter<String>,
    private val justTextFormatter: IndentedTextFormatter<String>,
    private val listFormatter: IndentedTextFormatter<List<*>>,
    private val dictionaryFormatter: IndentedTextFormatter<Map<String, *>>,
    private val functionCallFormatter: IndentedTextFormatter<FunctionCall>,
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<Any?>(indentSize) {

    override fun format(value: Any?, indentIndex: Int, mode: IndentMode): String {
        validate(value, indentIndex, mode)

        val indent = when (mode) {
            NEW_LINE -> indent(indentIndex)
            CONTINUE_LINE -> ""
            CONTINUE_SINGLE_LINE -> TODO()
        }
        return when (value) {
            is ConcatenatedValue -> formatConcatenation(value, indentIndex, mode)
            is Reference -> "$indent${value.name}"
            is FunctionCall -> functionCallFormatter(value, indentIndex, mode)
            is List<*> -> listFormatter(value, indentIndex, mode)
            is Map<*, *> -> dictionaryFormatter(value.cast(), indentIndex, mode)
            else -> when {
                value.isLiteral() -> justTextFormatter(baseFormatter(value), indentIndex, mode)
                else -> justTextFormatter(quoteFormatter(value!!.toString()), indentIndex, mode)
            }
        }
    }

    /**
     *
     */
    private fun formatConcatenation(value: ConcatenatedValue, indentIndex: Int, mode: IndentMode): String =
        with(value) {
            format(left, indentIndex, mode) + " $operator " + format(right, indentIndex, mode = CONTINUE_LINE)
        }

    override fun validate(value: Any?, indentIndex: Int, mode: IndentMode) {
        super.validate(value, indentIndex, mode)
        when (value) {
            is BuildStatement -> error("Unable to format 'BuildStatement'")
            is ConcatenatedValue -> validate(value.left, indentIndex, mode)
            is Map<*, *> -> value.cast()
        }
    }

    @Suppress("USELESS_CAST", "UNCHECKED_CAST")
    private fun Map<*, *>.cast(): Map<String, *> =
        try {
            val dict = this as Map<String, *>
            // Required to force check the key type and try catch the ClassCastException here.
            dict.apply { keys.firstOrNull() as String? }
        } catch (e: ClassCastException) {
            error("Dictionary key type should be string.")
        }
}

/**
 *
 */
typealias Assignment = Pair<String, *>

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class AssignmentFormatter(
    private val valueFormatter: IndentedTextFormatter<Any?>,
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<Assignment>(indentSize) {

    override fun format(assignment: Assignment, indentIndex: Int, mode: IndentMode): String {
        validate(assignment, indentIndex, mode)

        val (variable, value) = assignment
        val indent = when (mode) {
            NEW_LINE -> indent(indentIndex)
            CONTINUE_LINE -> ""
            CONTINUE_SINGLE_LINE -> TODO()
        }
        val prefix = indent + if (variable.isBlank()) "" else "$variable = "
        return prefix + valueFormatter(value, indentIndex, CONTINUE_LINE)
    }
}

/**
 * TODO add alias, multiline support
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class LoadFormatter(
    private val quoteFormatter: TextFormatter<String>,
    indentSize: Int = DEFAULT_INDENT_SIZE,
    val singleLineThreshold: Int = 2
) : IndentedTextFormatter<LoadStatement>(indentSize) {

    override fun format(load: LoadStatement, indentIndex: Int, mode: IndentMode): String = with(load) {
        validate(load, indentIndex, mode)

        val indent = indent(indentIndex)
        val quotedFile = quoteFormatter(file)
        return load.rules.joinToString(
            prefix = "${indent}load($quotedFile, ",
            separator = ", ",
            postfix = ")"
        ) {
            quoteFormatter(it)
        }
    }
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class ComprehensionFormatter(
    private val valueFormatter: IndentedTextFormatter<Any?>,
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<ComprehensionStatement>(indentSize) {

    lateinit var statementFormatter: IndentedTextFormatter<BuildStatement>

    override fun format(comp: ComprehensionStatement, indentIndex: Int, mode: IndentMode): String = with(comp) {
        validate(comp, indentIndex, mode)

        val indent = indent(indentIndex)
        val firstLineIndent = when (mode) {
            NEW_LINE -> indent
            CONTINUE_LINE -> ""
            CONTINUE_SINGLE_LINE -> TODO()
        }
        val nextIndent = indent.nextIndent()
        """
            |$firstLineIndent$open
            |${statementFormatter(statement, indentIndex + 1, mode = NEW_LINE)}
            |${nextIndent}for $`for` in ${formatInValue()}${formatIf()}
            |$indent$close
        """.trimMargin()
    }

    override fun validate(arg: ComprehensionStatement, indentIndex: Int, mode: IndentMode) {
        super.validate(arg, indentIndex, mode)
        require(::statementFormatter.isInitialized) { notInitializedMessage("statementFormatter") }
    }

    private val ComprehensionStatement.open
        get() = when (type) {
            LIST -> "["
            DICT -> "{"
        }

    private val ComprehensionStatement.close
        get() = when (type) {
            LIST -> "]"
            DICT -> "}"
        }

    private fun ComprehensionStatement.formatInValue() =
        valueFormatter(`in`, indentIndex = 0, mode = CONTINUE_LINE)

    private fun ComprehensionStatement.formatIf() =
        if (`if` != null) " if $`if`" else ""
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class ConcatenationFormatter(
    private val valueFormatter: IndentedTextFormatter<Any?>,
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<ConcatenationStatement>(indentSize) {

    lateinit var statementFormatter: IndentedTextFormatter<BuildStatement>

    override fun format(concat: ConcatenationStatement, indentIndex: Int, mode: IndentMode): String {
        validate(concat, indentIndex, mode)

        val left = statementFormatter(concat.left, indentIndex, mode)
        val right =
            if (concat.right is BuildStatement) statementFormatter(concat.right, indentIndex, mode = CONTINUE_LINE)
            else valueFormatter(concat.right, indentIndex, mode = CONTINUE_LINE)
        return "$left ${concat.operator} $right"
    }

    override fun validate(concat: ConcatenationStatement, indentIndex: Int, mode: IndentMode) {
        super.validate(concat, indentIndex, mode)
        require(::statementFormatter.isInitialized) { notInitializedMessage("statementFormatter") }
    }
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class BazelRcFormatter : TextFormatter<BazelRcStatement> {

    override fun format(statement: BazelRcStatement): String = with(statement) {
        validate(statement)

        command + (if (config != null) ":$config" else "") + " $option"
    }
}

/**
 *
 *
 * IMPORTANT: it is PROHIBITED to add to constructur formatters that are declared BELOW the current one in this file.
 * If such formatters are still required, there should be declared 'lateinit' variables for each of them inside the class.
 */
class BuildStatementFormatter(
    private val justTextFormatter: IndentedTextFormatter<String>,
    private val functionCallFormatter: IndentedTextFormatter<FunctionCall>,
    private val valueFormatter: IndentedTextFormatter<Any?>,
    private val assignmentFormatter: IndentedTextFormatter<Assignment>,
    private val loadFormatter: IndentedTextFormatter<LoadStatement>,
    private val comprehensionFormatter: IndentedTextFormatter<ComprehensionStatement>,
    private val concatenationFormatter: IndentedTextFormatter<ConcatenationStatement>,
    private val bazelRcFormatter: TextFormatter<BazelRcStatement>,
    indentSize: Int = DEFAULT_INDENT_SIZE
) : IndentedTextFormatter<BuildStatement>(indentSize) {

    override fun format(statement: BuildStatement, indentIndex: Int, mode: IndentMode): String = with(statement) {
        validate(statement, indentIndex, mode)

        when (this) {
            is RawTextStatement -> justTextFormatter(text, indentIndex, mode)
            is ExpressionStatement -> valueFormatter(expression, indentIndex, mode)
            is LoadStatement -> loadFormatter(this, indentIndex, mode)
            is FunctionStatement -> functionCallFormatter(AnyFunctionCall(name, args), indentIndex, mode)
            is AssignmentStatement<*> -> assignmentFormatter(name to value, indentIndex, mode)
            is ComprehensionStatement -> comprehensionFormatter(this, indentIndex, mode)
            is ConcatenationStatement -> concatenationFormatter(this, indentIndex, mode)
            is BazelRcStatement -> bazelRcFormatter(this)
            EmptyLineStatement -> LINE_SEPARATOR
        }
    }
}


private fun notInitializedMessage(formatter: String) =
    "Text formatter '$formatter' must be initialized."