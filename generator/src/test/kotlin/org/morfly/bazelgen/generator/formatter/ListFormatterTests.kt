@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


class ListFormatterTests : ShouldSpec({
    val expressionFormatter = mockk<ExpressionFormatter>()

    should("fail if dependencies are not initialized") {
        val formatter = ListFormatter()

        shouldThrow<IllegalArgumentException> {
            formatter(listOf("arg1"), indentIndex = 0, mode = NEW_LINE)
        }
    }

    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = ListFormatter().apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe "$___4[]"
            }

            should("format single item list") {
                every { expressionFormatter("//label", indentIndex = 2, CONTINUE_LINE) } returns "\"//label\""

                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe "$___4[\"//label\"]"
            }

            should("format list") {
                every { expressionFormatter("//label1", indentIndex = 2, NEW_LINE) } returns "$_______8\"//label1\""
                every { expressionFormatter("//label2", indentIndex = 2, NEW_LINE) } returns "$_______8\"//label2\""
                every { expressionFormatter("//label3", indentIndex = 2, NEW_LINE) } returns "$_______8\"//label3\""

                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |$___4[
                        |$_______8"//label1",
                        |$_______8"//label2",
                        |$_______8"//label3",
                        |$___4]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = ListFormatter(indentSize = 3).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe "$__3[]"
            }

            should("format single item list") {
                every { expressionFormatter("//label", indentIndex = 2, CONTINUE_LINE) } returns "\"//label\""

                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe "$__3[\"//label\"]"
            }

            should("format list") {
                every { expressionFormatter("//label1", indentIndex = 2, NEW_LINE) } returns "$_____6\"//label1\""
                every { expressionFormatter("//label2", indentIndex = 2, NEW_LINE) } returns "$_____6\"//label2\""
                every { expressionFormatter("//label3", indentIndex = 2, NEW_LINE) } returns "$_____6\"//label3\""

                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |$__3[
                        |$_____6"//label1",
                        |$_____6"//label2",
                        |$_____6"//label3",
                        |$__3]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = ListFormatter(indentSize = 0).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe "[]"
            }

            should("format single item list") {
                every { expressionFormatter("//label", indentIndex = 2, CONTINUE_LINE) } returns "\"//label\""

                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                every { expressionFormatter("//label1", indentIndex = 2, NEW_LINE) } returns "\"//label1\""
                every { expressionFormatter("//label2", indentIndex = 2, NEW_LINE) } returns "\"//label2\""
                every { expressionFormatter("//label3", indentIndex = 2, NEW_LINE) } returns "\"//label3\""

                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |"//label1",
                        |"//label2",
                        |"//label3",
                        |]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = NEW_LINE) shouldBe expectedResult
            }
        }
    }

    context("mode CONTINUE_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = ListFormatter().apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "[]"
            }

            should("format single item list") {
                every { expressionFormatter("//label", indentIndex = 2, CONTINUE_LINE) } returns "\"//label\""

                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                every { expressionFormatter("//label1", indentIndex = 2, NEW_LINE) } returns "$_______8\"//label1\""
                every { expressionFormatter("//label2", indentIndex = 2, NEW_LINE) } returns "$_______8\"//label2\""
                every { expressionFormatter("//label3", indentIndex = 2, NEW_LINE) } returns "$_______8\"//label3\""

                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |$_______8"//label1",
                        |$_______8"//label2",
                        |$_______8"//label3",
                        |$___4]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = ListFormatter(indentSize = 3).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "[]"
            }

            should("format single item list") {
                every { expressionFormatter("//label", indentIndex = 2, CONTINUE_LINE) } returns "\"//label\""

                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                every { expressionFormatter("//label1", indentIndex = 2, NEW_LINE) } returns "$_____6\"//label1\""
                every { expressionFormatter("//label2", indentIndex = 2, NEW_LINE) } returns "$_____6\"//label2\""
                every { expressionFormatter("//label3", indentIndex = 2, NEW_LINE) } returns "$_____6\"//label3\""

                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |$_____6"//label1",
                        |$_____6"//label2",
                        |$_____6"//label3",
                        |$__3]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = ListFormatter(indentSize = 0).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty list") {
                val list = emptyList<String>()

                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "[]"
            }

            should("format single item list") {
                every { expressionFormatter("//label", indentIndex = 2, CONTINUE_LINE) } returns "\"//label\""

                val list = listOf("//label")

                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "[\"//label\"]"
            }

            should("format list") {
                every { expressionFormatter("//label1", indentIndex = 2, NEW_LINE) } returns "\"//label1\""
                every { expressionFormatter("//label2", indentIndex = 2, NEW_LINE) } returns "\"//label2\""
                every { expressionFormatter("//label3", indentIndex = 2, NEW_LINE) } returns "\"//label3\""

                val list = listOf("//label1", "//label2", "//label3")

                val expectedResult = """
                        |[
                        |"//label1",
                        |"//label2",
                        |"//label3",
                        |]
                    """.trimMargin()
                formatter.format(list, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})