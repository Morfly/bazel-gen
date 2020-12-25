@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.morfly.bazelgen.generator.dsl.type.StringFunctionCall
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


fun newFunctionCallFormatter(indentSize: Int): FunctionCallFormatter {
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

    return functionCallFormatter
}


class FunctionCallFormatterTests : ShouldSpec({
    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)
            val __________12 = " ".repeat(12)
            val ______________16 = " ".repeat(16)

            val formatter = newFunctionCallFormatter(indentSize = DEFAULT_INDENT_SIZE)

            should("format function with no arguments") {
                val func = StringFunctionCall("java_binary", emptyMap())

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe "${___4}java_binary()"
            }

            should("format function with one string argument") {
                val func = StringFunctionCall("java_binary", mapOf("name" to "test_target"))

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe "${___4}java_binary(name = \"test_target\")"
            }

            should("format function with one list argument") {
                val includeList = listOf("**.kt", "**.java")

                val func = StringFunctionCall("glob", mapOf("include" to includeList))

                val expectedResult = """
                    |${___4}glob(
                    |${_______8}include = [
                    |${__________12}"**.kt",
                    |${__________12}"**.java",
                    |${_______8}],
                    |${___4})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }

            should("format function with one list argument without name") {
                val includeList = listOf("**.kt", "**.java")

                val func = StringFunctionCall("glob", mapOf("" to includeList))

                val expectedResult = """
                    |${___4}glob([
                    |${_______8}"**.kt",
                    |${_______8}"**.java",
                    |${___4}])
                """.trimMargin()

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }

            should("format function with one dictionary argument") {
                val manifestValues = mapOf(
                    "versionName" to "1.0",
                    "versionCode" to "1000"
                )

                val func = StringFunctionCall("android_binary", mapOf("manifest_values" to manifestValues))

                val expectedResult = """
                    |${___4}android_binary(
                    |${_______8}manifest_values = {
                    |${__________12}"versionName": "1.0",
                    |${__________12}"versionCode": "1000",
                    |${_______8}},
                    |${___4})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }

            should("format function with one function argument") {
                val globSrcs = StringFunctionCall("glob", mapOf("include" to listOf("**.kt", "**.java")))

                val func = StringFunctionCall("java_library", mapOf("srcs" to globSrcs))

                val expectedResult = """
                    |${___4}java_library(
                    |${_______8}srcs = glob(
                    |${__________12}include = [
                    |${______________16}"**.kt",
                    |${______________16}"**.java",
                    |${__________12}],
                    |${_______8}),
                    |${___4})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }

            should("format function with multiple arguments") {
                val deps = listOf("//lib1", "//lib2")
                val srcs = StringFunctionCall("glob", mapOf("" to listOf("**.kt", "**.java")))
                val manifestValues = mapOf(
                    "versionName" to "1.0",
                    "versionCode" to "1000"
                )

                val func = StringFunctionCall(
                    "android_binary", mapOf(
                        "name" to "app",
                        "deps" to deps,
                        "srcs" to srcs,
                        "manifest_values" to manifestValues
                    )
                )

                val expectedResult = """
                    |android_binary(
                    |${___4}name = "app",
                    |${___4}deps = [
                    |${_______8}"//lib1",
                    |${_______8}"//lib2",
                    |${___4}],
                    |${___4}srcs = glob([
                    |${_______8}"**.kt",
                    |${_______8}"**.java",
                    |${___4}]),
                    |${___4}manifest_values = {
                    |${_______8}"versionName": "1.0",
                    |${_______8}"versionCode": "1000",
                    |${___4}},
                    |)
                """.trimMargin()

                formatter.format(func, indentIndex = 0, NEW_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newFunctionCallFormatter(indentSize = 3)

            should("format function with no arguments") {
                val func = StringFunctionCall("java_binary", emptyMap())

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe "${__3}java_binary()"
            }

            should("format function with one string argument") {
                val func = StringFunctionCall("java_binary", mapOf("name" to "test_target"))

                formatter.format(
                    func,
                    indentIndex = 1,
                    NEW_LINE
                ) shouldBe "${__3}java_binary(name = \"test_target\")"
            }

            should("format function with multiple arguments") {
                val func = StringFunctionCall(
                    "java_binary", mapOf(
                        "name" to "test_target",
                        "main_class" to "com/test/Main.kt"
                    )
                )

                val expectedResult = """
                    |${__3}java_binary(
                    |${_____6}name = "test_target",
                    |${_____6}main_class = "com/test/Main.kt",
                    |${__3})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = newFunctionCallFormatter(indentSize = 0)

            should("format function with no arguments") {
                val func = StringFunctionCall("java_binary", emptyMap())

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe "java_binary()"
            }

            should("format function with arguments") {
                val func = StringFunctionCall(
                    "java_binary", mapOf(
                        "name" to "test_target",
                        "main_class" to "com/test/Main.kt"
                    )
                )

                val expectedResult = """
                    |java_binary(
                    |name = "test_target",
                    |main_class = "com/test/Main.kt",
                    |)
                """.trimMargin()

                formatter.format(func, indentIndex = 1, NEW_LINE) shouldBe expectedResult
            }
        }
    }

    context("mode CONTINUE_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)
            val __________12 = " ".repeat(12)
            val ______________16 = " ".repeat(16)

            val formatter = newFunctionCallFormatter(indentSize = 4)

            should("format function with no arguments") {
                val func = StringFunctionCall("java_binary", emptyMap())

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe "java_binary()"
            }

            should("format function with one string argument") {
                val func = StringFunctionCall("java_binary", mapOf("name" to "test_target"))

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe "java_binary(name = \"test_target\")"
            }

            should("format function with one list argument") {
                val includeList = listOf("**.kt", "**.java")

                val func = StringFunctionCall("glob", mapOf("include" to includeList))

                val expectedResult = """
                    |glob(
                    |${_______8}include = [
                    |${__________12}"**.kt",
                    |${__________12}"**.java",
                    |${_______8}],
                    |${___4})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }

            should("format function with one list argument without name") {
                val includeList = listOf("**.kt", "**.java")

                val func = StringFunctionCall("glob", mapOf("" to includeList))

                val expectedResult = """
                    |glob([
                    |${_______8}"**.kt",
                    |${_______8}"**.java",
                    |${___4}])
                """.trimMargin()

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }

            should("format function with one dictionary argument") {
                val manifestValues = mapOf(
                    "versionName" to "1.0",
                    "versionCode" to "1000"
                )

                val func = StringFunctionCall("android_binary", mapOf("manifest_values" to manifestValues))

                val expectedResult = """
                    |android_binary(
                    |${_______8}manifest_values = {
                    |${__________12}"versionName": "1.0",
                    |${__________12}"versionCode": "1000",
                    |${_______8}},
                    |${___4})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }

            should("format function with one function argument") {
                val globSrcs = StringFunctionCall("glob", mapOf("include" to listOf("**.kt", "**.java")))

                val func = StringFunctionCall("java_library", mapOf("srcs" to globSrcs))

                val expectedResult = """
                    |java_library(
                    |${_______8}srcs = glob(
                    |${__________12}include = [
                    |${______________16}"**.kt",
                    |${______________16}"**.java",
                    |${__________12}],
                    |${_______8}),
                    |${___4})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = newFunctionCallFormatter(indentSize = 3)

            should("format function with no arguments") {
                val func = StringFunctionCall("java_binary", emptyMap())

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe "java_binary()"
            }

            should("format function with one string argument") {
                val func = StringFunctionCall("java_binary", mapOf("name" to "test_target"))

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe "java_binary(name = \"test_target\")"
            }

            should("format function with multiple arguments") {
                val func = StringFunctionCall(
                    "java_binary", mapOf(
                        "name" to "test_target",
                        "main_class" to "com/test/Main.kt"
                    )
                )

                val expectedResult = """
                    |java_binary(
                    |${_____6}name = "test_target",
                    |${_____6}main_class = "com/test/Main.kt",
                    |${__3})
                """.trimMargin()

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = newFunctionCallFormatter(indentSize = 0)

            should("format function with no arguments") {
                val func = StringFunctionCall("java_binary", emptyMap())

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe "java_binary()"
            }

            should("format function with arguments") {
                val func = StringFunctionCall(
                    "java_binary", mapOf(
                        "name" to "test_target",
                        "main_class" to "com/test/Main.kt"
                    )
                )

                val expectedResult = """
                    |java_binary(
                    |name = "test_target",
                    |main_class = "com/test/Main.kt",
                    |)
                """.trimMargin()

                formatter.format(func, indentIndex = 1, CONTINUE_LINE) shouldBe expectedResult
            }
        }
    }
})