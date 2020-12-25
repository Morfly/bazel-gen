@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.morfly.bazelgen.generator.dsl.type.ListReference
import org.morfly.bazelgen.generator.dsl.type.StringFunctionCall


fun newAssignmentFormatter(indentSize: Int): AssignmentFormatter {
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

    return assignmentFormatter
}


class AssignmentFormatterTests : ShouldSpec({
    val ___4 = " ".repeat(4)
    val _______8 = " ".repeat(8)

    should("format reference assignment") {
        val ref = "arg" to ListReference<String>(name = "TEST_VARIABLE")
        val noNameRef = "" to ref.second

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(ref, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = TEST_VARIABLE"
        formatter.format(noNameRef, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}TEST_VARIABLE"

        formatter.format(ref, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = TEST_VARIABLE"
        formatter.format(noNameRef, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "TEST_VARIABLE"
    }

    should("format single-line list assignment") {
        val list = "arg" to listOf("item1")
        val noNameList = "" to list.second

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(list, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = [\"item1\"]"
        formatter.format(noNameList, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}[\"item1\"]"

        formatter.format(list, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = [\"item1\"]"
        formatter.format(noNameList, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "[\"item1\"]"
    }

    should("format list assignment") {
        val list = "arg" to listOf("item1", "item2")
        val noNameList = "" to list.second

        val formattedList = """
            |[
            |${_______8}"item1",
            |${_______8}"item2",
            |$___4]
        """.trimMargin()

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(list, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = $formattedList"
        formatter.format(noNameList, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}$formattedList"

        formatter.format(list, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = $formattedList"
        formatter.format(noNameList, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe formattedList
    }

    should("format single-argument function call assignment") {
        val func = "arg" to StringFunctionCall("test_func", mapOf("arg1" to 1))
        val noNameFunc = "" to func.second

        val formattedFunc = """
            |test_func(
            |${_______8}arg1 = 1,
            |$___4)
        """.trimMargin()

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(func, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = $formattedFunc"
        formatter.format(noNameFunc, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}$formattedFunc"

        formatter.format(func, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = $formattedFunc"
        formatter.format(noNameFunc, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe formattedFunc
    }

    should("format function call assignment") {
        val func = "arg" to StringFunctionCall("test_func", mapOf("arg1" to 1, "arg2" to "2"))
        val noNameFunc = "" to func.second

        val formattedFunc = """
            |test_func(
            |${_______8}arg1 = 1,
            |${_______8}arg2 = "2",
            |$___4)
        """.trimMargin()

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(func, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = $formattedFunc"
        formatter.format(noNameFunc, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}$formattedFunc"

        formatter.format(func, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = $formattedFunc"
        formatter.format(noNameFunc, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe formattedFunc
    }

    should("format single-line dictionary assignment") {
        val dict = "arg" to mapOf("param1" to "value1")
        val noNameDict = "" to dict.second

        val formattedDict = """
            {"param1": "value1"}
        """.trimIndent()

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(dict, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = $formattedDict"
        formatter.format(noNameDict, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}$formattedDict"

        formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = $formattedDict"
        formatter.format(noNameDict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe formattedDict
    }

    should("format dictionary assignment") {
        val dict = "arg" to mapOf("param1" to "value1", "param2" to "value2")
        val noNameDict = "" to dict.second

        val formattedDict = """
            |{
            |$_______8"param1": "value1",
            |$_______8"param2": "value2",
            |$___4}
        """.trimMargin()

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(dict, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = $formattedDict"
        formatter.format(noNameDict, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}$formattedDict"

        formatter.format(dict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = $formattedDict"
        formatter.format(noNameDict, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe formattedDict
    }

    should("format literals assignment") {
        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        val nullAssignment = "arg" to null
        val noNameNullAssignment = "" to null

        val intAssignment = "arg" to 42
        val noNameIntAssignment = "" to 42

        val booleanAssignment = "arg" to true
        val noNameBooleanAssignment = "" to true

        formatter.format(nullAssignment, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "${___4}arg = None"
        formatter.format(noNameNullAssignment, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "${___4}None"

        formatter.format(intAssignment, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "${___4}arg = 42"
        formatter.format(noNameIntAssignment, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "${___4}42"

        formatter.format(booleanAssignment, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "${___4}arg = True"
        formatter.format(noNameBooleanAssignment, indentIndex = 1, mode = IndentMode.NEW_LINE) shouldBe "${___4}True"


        formatter.format(nullAssignment, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "arg = None"
        formatter.format(noNameNullAssignment, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "None"

        formatter.format(intAssignment, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "arg = 42"
        formatter.format(noNameIntAssignment, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "42"

        formatter.format(booleanAssignment, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "arg = True"
        formatter.format(noNameBooleanAssignment, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "True"
    }

    should("format single-line string assignment") {
        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        val str = "arg" to "single-line string"
        val noNameStr = "" to "single-line string"

        formatter.format(
            str,
            indentIndex = 1,
            mode = IndentMode.NEW_LINE
        ) shouldBe "${___4}arg = \"single-line string\""
        formatter.format(
            noNameStr,
            indentIndex = 1,
            mode = IndentMode.NEW_LINE
        ) shouldBe "${___4}\"single-line string\""

        formatter.format(str, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "arg = \"single-line string\""
        formatter.format(noNameStr, indentIndex = 1, mode = IndentMode.CONTINUE_LINE) shouldBe "\"single-line string\""
    }

    should("format multiline string assignment") {
        val str = "arg" to """
            multiline
            string
        """.trimIndent()
        val noNameStr = "" to str.second

        val quote = "\"\"\""
        val formattedStr = """
            |$quote
            |${___4}multiline
            |${___4}string
            |$___4$quote
        """.trimMargin()

        val formatter = newAssignmentFormatter(indentSize = DEFAULT_INDENT_SIZE)

        formatter.format(str, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}arg = $formattedStr"
        formatter.format(noNameStr, indentIndex = 1, IndentMode.NEW_LINE) shouldBe "${___4}$formattedStr"

        formatter.format(str, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe "arg = $formattedStr"
        formatter.format(noNameStr, indentIndex = 1, IndentMode.CONTINUE_LINE) shouldBe formattedStr
    }
})