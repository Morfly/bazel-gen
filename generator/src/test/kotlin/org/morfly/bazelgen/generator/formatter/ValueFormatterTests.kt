@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.buildfile.RawTextStatement
import org.morfly.bazelgen.generator.dsl.type.ListReference
import org.morfly.bazelgen.generator.dsl.type.StringFunctionCall
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


class ValueFormatterTests : ShouldSpec({
    val baseFormatter = mockk<BaseTextFormatter>()
    val quoteFormatter = mockk<QuoteFormatter>()
    val justTextFormatter = mockk<JustTextFormatter>()
    val listTextFormatter = mockk<ListFormatter>()
    val dictionaryFormatter = mockk<DictionaryFormatter>()
    val functionCallFormatter = mockk<FunctionCallFormatter>()

    val ___4 = " ".repeat(4)
    val _______8 = " ".repeat(8)

    should("throw an exception for dictionary with the non-string key") {
        val formatter = ValueFormatter(
            baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
            functionCallFormatter
        )

        val dict = mapOf(Any() to "value1")

        shouldThrow<IllegalStateException> {
            formatter.format(dict, indentIndex = 1, NEW_LINE)
        }
    }

    should("format empty dictionary with the non-string key") {
        val dict = mapOf<Any, Any?>()

        every { dictionaryFormatter(emptyMap<String, Any?>(), indentIndex = 1, mode = NEW_LINE) } returns "$___4{}"

        val formatter = ValueFormatter(
            baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
            functionCallFormatter
        )

        formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe "$___4{}"
    }

    should("throw an exception for 'BuildStatement'") {
        val formatter = ValueFormatter(
            baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
            functionCallFormatter
        )

        val statement = RawTextStatement("statement")

        shouldThrow<IllegalStateException> {
            formatter.format(statement, indentIndex = 1, mode = NEW_LINE)
        }
    }

    context("mode NEW_LINE") {
        should("format reference") {
            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            val ref = ListReference<String>(name = "TEST_VARIABLE")

            formatter.format(ref, indentIndex = 2, NEW_LINE) shouldBe "${_______8}TEST_VARIABLE"
        }

        should("format single-line list") {
            val expectedResult = """
                |$___4["item1"]
            """.trimMargin()

            val list = listOf("item1")

            every { listTextFormatter(list, indentIndex = 1, mode = NEW_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(list, indentIndex = 1, NEW_LINE) shouldBe expectedResult
        }

        should("format list") {
            val expectedResult = """
                |$___4[
                |${_______8}"item1",
                |${_______8}"item2",
                |$___4]
            """.trimMargin()

            val list = listOf("item1", "item2")

            every { listTextFormatter(list, indentIndex = 1, mode = NEW_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(list, indentIndex = 1, NEW_LINE) shouldBe expectedResult
        }

        should("format single-line function call") {
            val expectedResult = """
                |${___4}test_func(
                |${_______8}arg1 = 1,
                |${___4})
            """.trimMargin()

            val func = StringFunctionCall("test_func", mapOf("arg1" to 1))

            every { functionCallFormatter(func, indentIndex = 1, mode = NEW_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
        }

        should("format function call") {
            val expectedResult = """
                |${___4}test_func(
                |${_______8}arg1 = 1,
                |${_______8}arg2 = "2",
                |${___4})
            """.trimMargin()

            val func = StringFunctionCall("test_func", mapOf("arg1" to 1, "arg2" to "2"))

            every { functionCallFormatter(func, indentIndex = 1, mode = NEW_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
        }

        should("format single-line dictionary") {
            val expectedResult = """
                |$___4{"param1": "value1"}
            """.trimMargin()

            val dict = mapOf("param1" to "value1")

            every { dictionaryFormatter(dict, indentIndex = 1, mode = NEW_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe expectedResult
        }

        should("format dictionary") {
            val expectedResult = """
                |$___4{
                |${_______8}"param1": "value1",
                |${_______8}"param2": "value2",
                |$___4}
            """.trimMargin()

            val dict = mapOf("param1" to "value1", "param2" to "value2")

            every { dictionaryFormatter(dict, indentIndex = 1, mode = NEW_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(dict, indentIndex = 1, NEW_LINE) shouldBe expectedResult
        }

        should("format literals") {
            every { baseFormatter(any()) } returns "literal"
            every { justTextFormatter("literal", indentIndex = 1, mode = NEW_LINE) } returns "literal"

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            val nullValue: Any? = null
            val intValue = 42
            val booleanValue = true
            val floatValue = 42f
            val doubleValue = 42.0
            val longValue = 42L
            val shortValue = 42
            val byteValue = 101

            formatter.format(nullValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(intValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(booleanValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(floatValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(doubleValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(longValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(shortValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(byteValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
        }

        should("format single-line string") {
            every { quoteFormatter("single-line string") } returns "\"single-line string\""
            every {
                justTextFormatter("\"single-line string\"", indentIndex = 1, mode = NEW_LINE)
            } returns "$___4\"single-line string\""

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(
                "single-line string", indentIndex = 1, mode = NEW_LINE
            ) shouldBe "$___4\"single-line string\""
        }

        should("format multiline string") {
            val multilineString = """
                multiline
                string
            """.trimIndent()

            val quotes = "\"\"\""
            val quotedMultilineString = """
                |$___4$quotes
                |${___4}multiline
                |${___4}string
                |$___4$quotes
            """.trimMargin()

            every { quoteFormatter(multilineString) } returns quotedMultilineString
            every {
                justTextFormatter(
                    quotedMultilineString,
                    indentIndex = 1,
                    mode = any()
                )
            } returns quotedMultilineString

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(multilineString, indentIndex = 1, mode = NEW_LINE) shouldBe quotedMultilineString
        }

        should("format other types") {
            data class TestType(val arg1: String)

            every { quoteFormatter("TestType(arg1=value1)") } returns "\"TestType(arg1=value1)\""
            every {
                justTextFormatter("\"TestType(arg1=value1)\"", indentIndex = 1, mode = any())
            } returns "$___4\"TestType(arg1=value1)\""

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            val testObject = TestType(arg1 = "value1")

            formatter.format(testObject, indentIndex = 1, mode = NEW_LINE) shouldBe "$___4\"TestType(arg1=value1)\""
        }
    }

    context("mode CONTINUE_LINE") {
        should("format reference") {
            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            val ref = ListReference<String>(name = "TEST_VARIABLE")

            formatter.format(ref, indentIndex = 2, CONTINUE_LINE) shouldBe "TEST_VARIABLE"
        }

        should("format single-line list") {
            val expectedResult = """
                ["item1"]
            """.trimIndent()

            val list = listOf("item1")

            every { listTextFormatter(list, indentIndex = 1, mode = CONTINUE_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(list, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
        }

        should("format list") {
            val expectedResult = """
                |[
                |${_______8}"item1",
                |${_______8}"item2",
                |$___4]
            """.trimMargin()

            val list = listOf("item1", "item2")

            every { listTextFormatter(list, indentIndex = 1, mode = CONTINUE_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(list, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
        }

        should("format single-line function call") {
            val expectedResult = """
                test_func(arg1 = 1)
            """.trimIndent()

            val func = StringFunctionCall("test_func", mapOf("arg1" to 1))

            every { functionCallFormatter(func, indentIndex = 1, mode = CONTINUE_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
        }

        should("format function call") {
            val expectedResult = """
                |test_func(
                |${_______8}arg1 = 1,
                |${_______8}arg2 = "2",
                |$___4)
            """.trimMargin()

            val func = StringFunctionCall("test_func", mapOf("arg1" to 1, "arg2" to "2"))

            every { functionCallFormatter(func, indentIndex = 1, mode = CONTINUE_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
        }

        should("format single-line dictionary") {
            val expectedResult = """
                {"param1": "value1"}
            """.trimIndent()

            val dict = mapOf("param1" to "value1")

            every { dictionaryFormatter(dict, indentIndex = 1, mode = CONTINUE_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
        }

        should("format dictionary") {
            val expectedResult = """
                |{
                |${_______8}"param1": "value1",
                |${_______8}"param2": "value2",
                |$___4}
            """.trimMargin()

            val dict = mapOf("param1" to "value1", "param2" to "value2")

            every { dictionaryFormatter(dict, indentIndex = 1, mode = CONTINUE_LINE) } returns expectedResult

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(dict, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
        }

        should("format literals") {
            every { baseFormatter(any()) } returns "literal"
            every { justTextFormatter("literal", indentIndex = 1, mode = any()) } returns "literal"

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            val nullValue: Any? = null
            val intValue = 42
            val booleanValue = true
            val floatValue = 42f
            val doubleValue = 42.0
            val longValue = 42L
            val shortValue = 42
            val byteValue = 101

            formatter.format(nullValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(nullValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(intValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(intValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(booleanValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(booleanValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(floatValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(floatValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(doubleValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(doubleValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(longValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(longValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(shortValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(shortValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"

            formatter.format(byteValue, indentIndex = 1, mode = NEW_LINE) shouldBe "literal"
            formatter.format(byteValue, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "literal"
        }

        should("format single-line string") {
            every { quoteFormatter("single-line string") } returns "\"single-line string\""
            every {
                justTextFormatter("\"single-line string\"", indentIndex = 1, mode = CONTINUE_LINE)
            } returns "\"single-line string\""

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(
                "single-line string", indentIndex = 1, mode = CONTINUE_LINE
            ) shouldBe "\"single-line string\""
        }

        should("format multiline string") {
            val multilineString = """
                multiline
                string
            """.trimIndent()

            val quotes = "\"\"\""
            val quotedMultilineString = """
                |$quotes
                |${___4}multiline
                |${___4}string
                |$___4$quotes
            """.trimMargin()

            every { quoteFormatter(multilineString) } returns quotedMultilineString
            every {
                justTextFormatter(
                    quotedMultilineString,
                    indentIndex = 1,
                    mode = any()
                )
            } returns quotedMultilineString

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            formatter.format(multilineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe quotedMultilineString
        }

        should("format other types") {
            data class TestType(val arg1: String)

            every { quoteFormatter("TestType(arg1=value1)") } returns "\"TestType(arg1=value1)\""
            every {
                justTextFormatter("\"TestType(arg1=value1)\"", indentIndex = 1, mode = any())
            } returns "\"TestType(arg1=value1)\""

            val formatter = ValueFormatter(
                baseFormatter, quoteFormatter, justTextFormatter, listTextFormatter, dictionaryFormatter,
                functionCallFormatter
            )

            val testObject = TestType(arg1 = "value1")

            formatter.format(testObject, indentIndex = 1, mode = CONTINUE_LINE) shouldBe "\"TestType(arg1=value1)\""
        }
    }
})