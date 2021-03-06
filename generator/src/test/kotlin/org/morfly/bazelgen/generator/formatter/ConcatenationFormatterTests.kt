@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.dsl.core.RawTextStatement
import org.morfly.bazelgen.generator.dsl.core.element.AnyConcatenation
import org.morfly.bazelgen.generator.dsl.core.element.Comprehension
import org.morfly.bazelgen.generator.dsl.core.element.ComprehensionType.LIST
import org.morfly.bazelgen.generator.dsl.core.element.ListReference
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


class ConcatenationFormatterTests : ShouldSpec({
    val expressionFormatter = mockk<ExpressionFormatter>()

    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = ConcatenationFormatter().apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                every { expressionFormatter(left, indentIndex = 1, mode = NEW_LINE) } returns """
                    |${___4}TEST_VARIABLE = [
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_______8}"//label3",
                    |${_______8}"//label4",                    
                    |${___4}]
                """.trimMargin()

                val expectedResult = """
                    |${___4}TEST_VARIABLE = [
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

                every { expressionFormatter(left, indentIndex = 1, mode = NEW_LINE) } returns """
                    |${___4}TEST_VARIABLE = [
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                val expectedResult = """
                    |${___4}TEST_VARIABLE = [
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}] + [
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                formatter.format(concat, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = ConcatenationFormatter(indentSize = 3).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                every { expressionFormatter(left, indentIndex = 1, mode = NEW_LINE) } returns """
                    |${__3}TEST_VARIABLE = [
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_____6}"//label3",
                    |${_____6}"//label4",                    
                    |${__3}]
                """.trimMargin()

                val expectedResult = """
                    |${__3}TEST_VARIABLE = [
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

                every { expressionFormatter(left, indentIndex = 1, mode = NEW_LINE) } returns """
                    |${__3}TEST_VARIABLE = [
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_____6}"update_" + view_models_with_resource_imports[0:-3]
                    |${_____6}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$__3]
                """.trimMargin()

                val expectedResult = """
                    |${__3}TEST_VARIABLE = [
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

            val formatter = ConcatenationFormatter(indentSize = 0).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                every { expressionFormatter(left, indentIndex = 1, mode = NEW_LINE) } returns """
                    |TEST_VARIABLE = [
                    |"//label1",
                    |"//label2",
                    |]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |"//label3",
                    |"//label4",                    
                    |]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

                every { expressionFormatter(left, indentIndex = 1, mode = NEW_LINE) } returns """
                    |TEST_VARIABLE = [
                    |"//label1",
                    |"//label2",
                    |]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

            val formatter = ConcatenationFormatter().apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                every { expressionFormatter(left, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |TEST_VARIABLE = [
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_______8}"//label3",
                    |${_______8}"//label4",                    
                    |${___4}]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

                every { expressionFormatter(left, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |TEST_VARIABLE = [
                    |${_______8}"//label1",
                    |${_______8}"//label2",
                    |${___4}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_______8}"update_" + view_models_with_resource_imports[0:-3]
                    |${_______8}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$___4]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

            val formatter = ConcatenationFormatter(indentSize = 3).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                every { expressionFormatter(left, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |TEST_VARIABLE = [
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_____6}"//label3",
                    |${_____6}"//label4",                    
                    |${__3}]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

                every { expressionFormatter(left, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |TEST_VARIABLE = [
                    |${_____6}"//label1",
                    |${_____6}"//label2",
                    |${__3}]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |${_____6}"update_" + view_models_with_resource_imports[0:-3]
                    |${_____6}for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |$__3]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

            val formatter = ConcatenationFormatter(indentSize = 0).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format two multiline lists") {
                val left = listOf("//label1", "//label2")
                val right = listOf("//label3", "//label4")

                val concat = AnyConcatenation(left, "+", right)

                every { expressionFormatter(left, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |TEST_VARIABLE = [
                    |"//label1",
                    |"//label2",
                    |]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |"//label3",
                    |"//label4",                    
                    |]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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

                every { expressionFormatter(left, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |TEST_VARIABLE = [
                    |"//label1",
                    |"//label2",
                    |]
                """.trimMargin()

                every { expressionFormatter(right, indentIndex = 1, mode = CONTINUE_LINE) } returns """
                    |[
                    |"update_" + view_models_with_resource_imports[0:-3]
                    |for view_models_with_resource_imports in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    |]
                """.trimMargin()

                val expectedResult = """
                    |TEST_VARIABLE = [
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