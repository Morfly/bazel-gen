@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.morfly.bazelgen.generator.dsl.core.RawTextStatement
import org.morfly.bazelgen.generator.dsl.core.element.AnyConcatenation
import org.morfly.bazelgen.generator.dsl.core.element.Comprehension
import org.morfly.bazelgen.generator.dsl.core.element.ComprehensionType.LIST
import org.morfly.bazelgen.generator.dsl.core.element.ListConcatenation
import org.morfly.bazelgen.generator.dsl.core.element.ListReference
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


fun newConcatenationFormatter(indentSize: Int): ConcatenationFormatter {
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

    return concatenationFormatter
}


class ConcatenationFormatterTests : ShouldSpec({
    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = newConcatenationFormatter(indentSize = 4)

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |${___4}[
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"//label3",
                    |${_______8}"//label4",
                    |${___4}]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format assignment and comprehension") {
                val left = listOf("//label1", "//label2")
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val right = Comprehension(
                    type = LIST, innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |${___4}[
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format nested concatenations") {
                val left = listOf("//label1", "//label2")
                val middle = listOf("//label3", "//label4")
                val right = ListReference<String>("TEST_LIST")

                val innerConcat = AnyConcatenation(left, "+", middle)
                val concat = AnyConcatenation(innerConcat, "+", right)

                val expectedResult = """
                    |${___4}[
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"//label3",
                    |${_______8}"//label4",
                    |${___4}] + TEST_LIST
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format concatenations with concatenated values") {
                val left = listOf("//label1", "//label2")
                val right = ListConcatenation<String>(
                    left = listOf("//label3", "//label4"),
                    operator = "+",
                    right = ListReference<String>("TEST_LIST")
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |${___4}[
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"//label3",
                    |${_______8}"//label4",
                    |${___4}] + TEST_LIST
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newConcatenationFormatter(indentSize = 3)

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |${__3}[
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}] + [
                    |${_____6}"//label3",
                    |${_____6}"//label4",
                    |${__3}]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format assignment and comprehension") {
                val left = listOf("//label1", "//label2")
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val right = Comprehension(
                    type = LIST, innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |${__3}[
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}] + [
                    |${_____6}"update_" + view_models_with_resource_imports[0:-3]
                    |${_____6}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$__3]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {

            val formatter = newConcatenationFormatter(indentSize = 0)

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |"//label1",
                    |"//label2",
                    |] + [
                    |"//label3",
                    |"//label4",
                    |]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format assignment and comprehension") {
                val left = listOf("//label1", "//label2")
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val right = Comprehension(
                    type = LIST, innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |"//label1",
                    |"//label2",
                    |] + [
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }
    }

    context("mode CONTINUE_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = newConcatenationFormatter(indentSize = 4)

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"//label3",
                    |${_______8}"//label4",
                    |${___4}]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format assignment and comprehension") {
                val left = listOf("//label1", "//label2")
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val right = Comprehension(
                    type = LIST, innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newConcatenationFormatter(indentSize = 3)

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}] + [
                    |${_____6}"//label3",
                    |${_____6}"//label4",
                    |${__3}]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format assignment and comprehension") {
                val left = listOf("//label1", "//label2")
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val right = Comprehension(
                    type = LIST, innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}] + [
                    |${_____6}"update_" + view_models_with_resource_imports[0:-3]
                    |${_____6}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$__3]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {

            val formatter = newConcatenationFormatter(indentSize = 0)

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |"//label1",
                    |"//label2",
                    |] + [
                    |"//label3",
                    |"//label4",
                    |]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format assignment and comprehension") {
                val left = listOf("//label1", "//label2")
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")
                val right = Comprehension(
                    type = LIST, innerStatement,
                    `for` = "view_models_with_resource_imports", `in` = listRef
                )

                val concat = AnyConcatenation(left, "+", right)

                val expectedResult = """
                    |[
                    |"//label1",
                    |"//label2",
                    |] + [
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})