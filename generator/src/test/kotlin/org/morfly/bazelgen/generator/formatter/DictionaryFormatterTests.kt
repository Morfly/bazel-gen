@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


class DictionaryFormatterTests : ShouldSpec({

    val quoteFormatter = mockk<QuoteFormatter>()
    val expressionFormatter = mockk<ExpressionFormatter>()

    should("fail if dependencies are not initialized") {
        val formatter = DictionaryFormatter(quoteFormatter)

        shouldThrow<IllegalArgumentException> {
            formatter(mapOf("arg1" to "value1"), indentIndex = 0, mode = NEW_LINE)
        }
    }

    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = DictionaryFormatter(quoteFormatter).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$___4{}"
            }

            should("format single entry dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""

                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$___4{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""
                every { quoteFormatter("param2") } returns "\"param2\""
                every { expressionFormatter("value2", indentIndex = 2, CONTINUE_LINE) } returns "\"value2\""
                every { quoteFormatter("param3") } returns "\"param3\""
                every { expressionFormatter("value3", indentIndex = 2, CONTINUE_LINE) } returns "\"value3\""

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

            val formatter = DictionaryFormatter(quoteFormatter, indentSize = 3)
                .apply { this.expressionFormatter = expressionFormatter }

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$__3{}"
            }

            should("format single entry dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""

                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$__3{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""
                every { quoteFormatter("param2") } returns "\"param2\""
                every { expressionFormatter("value2", indentIndex = 2, CONTINUE_LINE) } returns "\"value2\""
                every { quoteFormatter("param3") } returns "\"param3\""
                every { expressionFormatter("value3", indentIndex = 2, CONTINUE_LINE) } returns "\"value3\""

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
            val formatter = DictionaryFormatter(quoteFormatter, indentSize = 0)
                .apply { this.expressionFormatter = expressionFormatter }

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""

                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""
                every { quoteFormatter("param2") } returns "\"param2\""
                every { expressionFormatter("value2", indentIndex = 2, CONTINUE_LINE) } returns "\"value2\""
                every { quoteFormatter("param3") } returns "\"param3\""
                every { expressionFormatter("value3", indentIndex = 2, CONTINUE_LINE) } returns "\"value3\""

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

            val formatter = DictionaryFormatter(quoteFormatter).apply {
                this.expressionFormatter = expressionFormatter
            }

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""

                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""
                every { quoteFormatter("param2") } returns "\"param2\""
                every { expressionFormatter("value2", indentIndex = 2, CONTINUE_LINE) } returns "\"value2\""
                every { quoteFormatter("param3") } returns "\"param3\""
                every { expressionFormatter("value3", indentIndex = 2, CONTINUE_LINE) } returns "\"value3\""

                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |$_______8"param1": "value1",
                    |$_______8"param2": "value2",
                    |$_______8"param3": "value3",
                    |$___4}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = DictionaryFormatter(quoteFormatter, indentSize = 3)
                .apply { this.expressionFormatter = expressionFormatter }

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""

                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""
                every { quoteFormatter("param2") } returns "\"param2\""
                every { expressionFormatter("value2", indentIndex = 2, CONTINUE_LINE) } returns "\"value2\""
                every { quoteFormatter("param3") } returns "\"param3\""
                every { expressionFormatter("value3", indentIndex = 2, CONTINUE_LINE) } returns "\"value3\""

                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |$_____6"param1": "value1",
                    |$_____6"param2": "value2",
                    |$_____6"param3": "value3",
                    |$__3}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = DictionaryFormatter(quoteFormatter, indentSize = 0)
                .apply { this.expressionFormatter = expressionFormatter }

            should("format empty dictionary") {
                val dict = emptyMap<String, Any?>()

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe "{}"
            }

            should("format single entry dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""

                val dict = mapOf("param1" to "value1")

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe "{\"param1\": \"value1\"}"
            }

            should("format dictionary") {
                every { quoteFormatter("param1") } returns "\"param1\""
                every { expressionFormatter("value1", indentIndex = 2, CONTINUE_LINE) } returns "\"value1\""
                every { quoteFormatter("param2") } returns "\"param2\""
                every { expressionFormatter("value2", indentIndex = 2, CONTINUE_LINE) } returns "\"value2\""
                every { quoteFormatter("param3") } returns "\"param3\""
                every { expressionFormatter("value3", indentIndex = 2, CONTINUE_LINE) } returns "\"value3\""

                val dict = mapOf("param1" to "value1", "param2" to "value2", "param3" to "value3")

                val expectedResult = """
                    |{
                    |"param1": "value1",
                    |"param2": "value2",
                    |"param3": "value3",
                    |}
                """.trimMargin()

                formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})