@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


fun newDictionaryFormatter(indentSize: Int): DictionaryFormatter {
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
    comprehensionFormatter.expressionFormatter = expressionFormatter
    concatenationFormatter.expressionFormatter = expressionFormatter
    listFormatter.expressionFormatter = expressionFormatter
    dictionaryFormatter.expressionFormatter = expressionFormatter
    val assignmentFormatter = AssignmentFormatter(expressionFormatter, indentSize)
    functionCallFormatter.assignmentFormatter = assignmentFormatter
    val loadFormatter = LoadFormatter(quoteFormatter, indentSize)
    val bazelRcFormatter = BazelRcFormatter()
    val statementFormatter = BuildStatementFormatter(
        justTextFormatter, expressionFormatter, assignmentFormatter, loadFormatter, bazelRcFormatter, indentSize
    )
    comprehensionFormatter.statementFormatter = statementFormatter

    return dictionaryFormatter
}


class DictionaryFormatterTests : ShouldSpec({
    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = newDictionaryFormatter(indentSize = DEFAULT_INDENT_SIZE)

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$___4{}"
            }

            should("format single entry dictionary") {
                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$___4{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |$___4{
                    |$_______8"param1": "value1",
                    |$_______8"param2": "value2",
                    |$_______8"param3": "value3",
                    |$___4}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newDictionaryFormatter(indentSize = 3)

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$__3{}"
            }

            should("format single entry dictionary") {
                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$__3{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |$__3{
                    |$_____6"param1": "value1",
                    |$_____6"param2": "value2",
                    |$_____6"param3": "value3",
                    |$__3}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = newDictionaryFormatter(indentSize = 0)

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |"param1": "value1",
                    |"param2": "value2",
                    |"param3": "value3",
                    |}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }
        }
    }

    context("mode CONTINUE_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = newDictionaryFormatter(indentSize = DEFAULT_INDENT_SIZE)

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |$_______8"param1": "value1",
                    |$_______8"param2": "value2",
                    |$_______8"param3": "value3",
                    |$___4}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newDictionaryFormatter(indentSize = 3)

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |$_____6"param1": "value1",
                    |$_____6"param2": "value2",
                    |$_____6"param3": "value3",
                    |$__3}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = newDictionaryFormatter(indentSize = 0)

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |"param1": "value1",
                    |"param2": "value2",
                    |"param3": "value3",
                    |}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})