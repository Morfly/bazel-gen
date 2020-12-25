@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.morfly.bazelgen.generator.buildfile.*
import org.morfly.bazelgen.generator.dsl.type.ListReference
import org.morfly.bazelgen.generator.dsl.type.StringReference
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


fun newComprehensionFormatter(indentSize: Int): ComprehensionFormatter {
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

    return comprehensionFormatter
}


class ComprehensionFormatterTests : ShouldSpec({
    context("mode NEW_LINE") {

        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            should("format list comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |$___4[
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format dictionary comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.DICT, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |$___4{
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4}
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'FunctionStatement'") {
                val innerStatement = FunctionStatement(
                    "genrule", mapOf(
                        "name" to "\"update_\" + file[0:-3]",
                        "srcs" to listOf(StringReference("file")),
                        "outs" to listOf("file[0:-3] + \"_updated.kt\""),
                        "cmd" to """
                            cat $(SRCS) |
                            sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                        """.trimIndent()
                    )
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val tripleQuote = "\"\"\""

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "file", `in` = listRef
                )

                val expectedResult = """
                    *[
                    *${___4}genrule(
                    *${_______8}name = "update_" + file[0:-3],
                    *${_______8}srcs = [file],
                    *${_______8}outs = [file[0:-3] + "_updated.kt"],
                    *${_______8}cmd = $tripleQuote
                    *${_______8}cat $(SRCS) |
                    *${_______8}sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *${_______8}$tripleQuote,
                    *${___4})
                    *${___4}for file in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    *]
                """.trimMargin("*")

                formatter.format(comp, indentIndex = 0, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'EmptyLineStatement'") {
                val innerStatement = EmptyLineStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |$___4[
                    |
                    |
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'ConcatenationStatement'") {
                // TODO()
            }

            should("format with 'if' block") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef, `if` = "file not None"
                )

                val expectedResult = """
                    |$___4[
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS if file not None
                    |$___4]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            should("format list comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |[
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format dictionary comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.DICT, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |{
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |}
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'FunctionStatement'") {
                val innerStatement = FunctionStatement(
                    "genrule", mapOf(
                        "name" to "\"update_\" + file[0:-3]",
                        "srcs" to listOf(StringReference("file")),
                        "outs" to listOf("file[0:-3] + \"_updated.kt\""),
                        "cmd" to """
                            cat $(SRCS) |
                            sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                        """.trimIndent()
                    )
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val tripleQuote = "\"\"\""

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "file", `in` = listRef
                )

                val expectedResult = """
                    *[
                    *genrule(
                    *name = "update_" + file[0:-3],
                    *srcs = [file],
                    *outs = [file[0:-3] + "_updated.kt"],
                    *cmd = $tripleQuote
                    *cat $(SRCS) |
                    *sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *$tripleQuote,
                    *)
                    *for file in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    *]
                """.trimMargin("*")

                formatter.format(comp, indentIndex = 0, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'EmptyLineStatement'") {
                val innerStatement = EmptyLineStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |[
                    |
                    |
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'ConcatenationStatement'") {
                // TODO()
            }

            should("format with 'if' block") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef, `if` = "file not None"
                )

                val expectedResult = """
                    |[
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS if file not None
                    |]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }
    }

    context("mode CONTINUE_LINE") {

        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)
            val __________12 = " ".repeat(12)
            val ______________16 = " ".repeat(16)

            should("format list comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |[
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format dictionary comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.DICT, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |{
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4}
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'FunctionStatement'") {
                val innerStatement = FunctionStatement(
                    "genrule", mapOf(
                        "name" to "\"update_\" + file[0:-3]",
                        "srcs" to listOf(StringReference("file"), StringReference("default_file")),
                        "outs" to listOf("file[0:-3] + \"_updated.kt\""),
                        "cmd" to """
                            cat $(SRCS) |
                            sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                        """.trimIndent()
                    )
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val tripleQuote = "\"\"\""

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "file", `in` = listRef
                )

                val expectedResult = """
                    *[
                    *${_______8}genrule(
                    *${__________12}name = "update_" + file[0:-3],
                    *${__________12}srcs = [
                    *${______________16}file,
                    *${______________16}default_file,
                    *${__________12}],
                    *${__________12}outs = [file[0:-3] + "_updated.kt"],
                    *${__________12}cmd = $tripleQuote
                    *${__________12}cat $(SRCS) |
                    *${__________12}sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *${__________12}$tripleQuote,
                    *${_______8})
                    *${_______8}for file in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    *$___4]
                """.trimMargin("*")

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'EmptyLineStatement'") {
                val innerStatement = EmptyLineStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |[
                    |
                    |
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'ConcatenationStatement'") {
                // TODO()
            }

            should("format with 'if' block") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = DEFAULT_INDENT_SIZE)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef, `if` = "file not None"
                )

                val expectedResult = """
                    |[
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS if file not None
                    |$___4]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            should("format list comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |[
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format dictionary comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.DICT, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |{
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |}
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'FunctionStatement'") {
                val innerStatement = FunctionStatement(
                    "genrule", mapOf(
                        "name" to "\"update_\" + file[0:-3]",
                        "srcs" to listOf(StringReference("file")),
                        "outs" to listOf("file[0:-3] + \"_updated.kt\""),
                        "cmd" to """
                            cat $(SRCS) |
                            sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                        """.trimIndent()
                    )
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val tripleQuote = "\"\"\""

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "file", `in` = listRef
                )

                val expectedResult = """
                    *[
                    *genrule(
                    *name = "update_" + file[0:-3],
                    *srcs = [file],
                    *outs = [file[0:-3] + "_updated.kt"],
                    *cmd = $tripleQuote
                    *cat $(SRCS) |
                    *sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *$tripleQuote,
                    *)
                    *for file in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    *]
                """.trimMargin("*")

                formatter.format(comp, indentIndex = 0, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'EmptyLineStatement'") {
                val innerStatement = EmptyLineStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val expectedResult = """
                    |[
                    |
                    |
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'ConcatenationStatement'") {
                // TODO()
            }

            should("format with 'if' block") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val formatter = newComprehensionFormatter(indentSize = 0)

                val comp = ComprehensionStatement(
                    type = ComprehensionType.LIST, statement = innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef, `if` = "file not None"
                )

                val expectedResult = """
                    |[
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS if file not None
                    |]
                """.trimMargin()

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})