@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


fun newListFormatter(indentSize: Int): ListFormatter {
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

    return listFormatter
}


class ListFormatterTests : ShouldSpec({

    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = newListFormatter(indentSize = DEFAULT_INDENT_SIZE)

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "$___4[]"
            }

            should("format single item list") {
                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "$___4[\"//label\"]"
            }

            should("format list") {
                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |$___4[
                        |$_______8"//label1",
                        |$_______8"//label2",
                        |$_______8"//label3",
                        |$___4]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newListFormatter(indentSize = 3)

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "$__3[]"
            }

            should("format single item list") {
                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "$__3[\"//label\"]"
            }

            should("format list") {
                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |$__3[
                        |$_____6"//label1",
                        |$_____6"//label2",
                        |$_____6"//label3",
                        |$__3]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = newListFormatter(indentSize = 0)

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "[]"
            }

            should("format single item list") {
                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |"//label1",
                        |"//label2",
                        |"//label3",
                        |]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe expectedResult
            }
        }
    }

    context("mode CONTINUE_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = newListFormatter(indentSize = DEFAULT_INDENT_SIZE)

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "[]"
            }

            should("format single item list") {
                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |$_______8"//label1",
                        |$_______8"//label2",
                        |$_______8"//label3",
                        |$___4]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newListFormatter(indentSize = 3)

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "[]"
            }

            should("format single item list") {
                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |$_____6"//label1",
                        |$_____6"//label2",
                        |$_____6"//label3",
                        |$__3]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = newListFormatter(indentSize = 0)

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "[]"
            }

            should("format single item list") {
                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |"//label1",
                        |"//label2",
                        |"//label3",
                        |]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})