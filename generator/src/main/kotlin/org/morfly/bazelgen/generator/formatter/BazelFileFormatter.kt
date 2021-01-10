@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package org.morfly.bazelgen.generator.formatter

import org.morfly.bazelgen.generator.dsl.core.BazelRcStatement
import org.morfly.bazelgen.generator.dsl.core.BuildStatement
import org.morfly.bazelgen.generator.dsl.core.LoadStatement
import org.morfly.bazelgen.generator.file.BazelFile
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


/**
 *
 */
class BazelFileFormatter(private val statementFormatter: IndentedTextFormatter<BuildStatement>) :
    TextFormatter<BazelFile> {

    private var lastStatement: BuildStatement? = null

    override fun format(file: BazelFile): String {
        validate(file)

        return file.statements
            .joinToString(separator = LINE_SEPARATOR) { statement ->
                val additionalSpace = lastStatement.let {
                    when {
                        it == null -> ""
                        it is LoadStatement && statement is LoadStatement -> ""
                        it is BazelRcStatement && it.command == (statement as? BazelRcStatement)?.command -> ""
                        else -> LINE_SEPARATOR
                    }
                }
                lastStatement = statement

                additionalSpace + statementFormatter(statement, indentIndex = 0, mode = NEW_LINE)
            }
            .also { lastStatement = null }
    }


    companion object
}


/**
 *
 */
fun BazelFileFormatter.Companion.newInstance(indentSize: Int = DEFAULT_INDENT_SIZE): BazelFileFormatter {
    val baseFormatter = BaseTextFormatter()
    val quoteFormatter = QuoteFormatter()

    val justTextFormatter = JustTextFormatter(indentSize)
    val listFormatter = ListFormatter(indentSize)
    val dictionaryFormatter = DictionaryFormatter(quoteFormatter, indentSize)
    val functionCallFormatter = FunctionCallFormatter(indentSize)
    val comprehensionFormatter = ComprehensionFormatter(indentSize)
    val concatenationFormatter = ConcatenationFormatter(indentSize)

    val expressionFormatter = ExpressionFormatter(
        baseFormatter, quoteFormatter, justTextFormatter, listFormatter, dictionaryFormatter, functionCallFormatter,
        comprehensionFormatter, concatenationFormatter, indentSize
    )
    listFormatter.expressionFormatter = expressionFormatter
    dictionaryFormatter.expressionFormatter = expressionFormatter
    comprehensionFormatter.expressionFormatter = expressionFormatter
    concatenationFormatter.expressionFormatter = expressionFormatter

    val assignmentFormatter = AssignmentFormatter(expressionFormatter, indentSize)
    functionCallFormatter.assignmentFormatter = assignmentFormatter

    val loadFormatter = LoadFormatter(quoteFormatter, indentSize)
    val bazelRcFormatter = BazelRcFormatter()

    val statementFormatter = BuildStatementFormatter(
        justTextFormatter, expressionFormatter, assignmentFormatter, loadFormatter, bazelRcFormatter, indentSize
    )
    comprehensionFormatter.statementFormatter = statementFormatter

    return BazelFileFormatter(statementFormatter)
}
