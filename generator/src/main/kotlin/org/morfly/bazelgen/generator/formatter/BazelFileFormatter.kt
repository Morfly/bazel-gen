@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package org.morfly.bazelgen.generator.formatter

import org.morfly.bazelgen.generator.buildfile.BazelFile
import org.morfly.bazelgen.generator.buildfile.BazelRcStatement
import org.morfly.bazelgen.generator.buildfile.BuildStatement
import org.morfly.bazelgen.generator.buildfile.LoadStatement
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

    val valueFormatter = ValueFormatter(
        baseFormatter, quoteFormatter, justTextFormatter, listFormatter, dictionaryFormatter, functionCallFormatter,
        indentSize
    )
    listFormatter.valueFormatter = valueFormatter
    dictionaryFormatter.valueFormatter = valueFormatter

    val assignmentFormatter = AssignmentFormatter(valueFormatter, indentSize)
    functionCallFormatter.assignmentFormatter = assignmentFormatter

    val loadFormatter = LoadFormatter(quoteFormatter, indentSize)
    val comprehensionFormatter = ComprehensionFormatter(valueFormatter, indentSize)
    val concatenationFormatter = ConcatenationFormatter(valueFormatter, indentSize)
    val bazelRcFormatter = BazelRcFormatter()

    val statementFormatter = BuildStatementFormatter(
        justTextFormatter, functionCallFormatter, valueFormatter, assignmentFormatter, loadFormatter,
        comprehensionFormatter, concatenationFormatter, bazelRcFormatter, indentSize
    )
    comprehensionFormatter.statementFormatter = statementFormatter
    concatenationFormatter.statementFormatter = statementFormatter

    return BazelFileFormatter(statementFormatter)
}
