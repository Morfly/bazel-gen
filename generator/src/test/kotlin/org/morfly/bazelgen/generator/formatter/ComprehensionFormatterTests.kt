@file:Suppress("UNUSED_VARIABLE", "LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.dsl.core.ExpressionStatement
import org.morfly.bazelgen.generator.dsl.core.RawTextStatement
import org.morfly.bazelgen.generator.dsl.core.WhiteSpaceStatement
import org.morfly.bazelgen.generator.dsl.core.element.Comprehension
import org.morfly.bazelgen.generator.dsl.core.element.ComprehensionType.DICT
import org.morfly.bazelgen.generator.dsl.core.element.ComprehensionType.LIST
import org.morfly.bazelgen.generator.dsl.core.element.ListReference
import org.morfly.bazelgen.generator.dsl.core.element.StringReference
import org.morfly.bazelgen.generator.dsl.core.element.VoidFunctionCall
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


class ComprehensionFormatterTests : ShouldSpec({
    val expressionFormatter = mockk<ExpressionFormatter>()
    val statementFormatter = mockk<BuildStatementFormatter>()

    context("validation") {
        should("fail if dependencies are not initialized") {
            val formatter = ComprehensionFormatter()

            shouldThrow<IllegalArgumentException> {
                formatter.validate(mockk(), indentIndex = 0, mode = NEW_LINE)
            }

            shouldThrow<IllegalArgumentException> {
                formatter.format(mockk(), indentIndex = 0, mode = NEW_LINE)
            }
        }
    }

    context("mode NEW_LINE") {

        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)
            val __________12 = " ".repeat(12)
            val ______________16 = " ".repeat(16)

            should("format list comprehension with 'RawTextStatement'") {
                val innerStatement = RawTextStatement("\"update_\" + view_models_with_resource_imports[0:-3]")
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "$_______8${innerStatement.text}"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "$_______8${innerStatement.text}"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = DICT, expression = innerStatement,
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
                val innerStatement = ExpressionStatement(
                    VoidFunctionCall(
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
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val tripleQuote = "\"\"\""
                every {
                    statementFormatter(innerStatement, indentIndex = 1, mode = NEW_LINE)
                } returns """
                    *${___4}genrule(
                    *${_______8}name = "update_" + file[0:-3],
                    *${_______8}srcs = [file],
                    *${_______8}outs = [file[0:-3] + "_updated.kt"],
                    *${_______8}cmd = $tripleQuote
                    *${__________12}cat $(SRCS) |
                    *${__________12}sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *${_______8}$tripleQuote,
                    *${___4})
                """.trimMargin("*")
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
                    `for` = "file", `in` = listRef
                )

                val expectedResult = """
                    *[
                    *${___4}genrule(
                    *${_______8}name = "update_" + file[0:-3],
                    *${_______8}srcs = [file],
                    *${_______8}outs = [file[0:-3] + "_updated.kt"],
                    *${_______8}cmd = $tripleQuote
                    *${__________12}cat $(SRCS) |
                    *${__________12}sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *${_______8}$tripleQuote,
                    *${___4})
                    *${___4}for file in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    *]
                """.trimMargin("*")

                formatter.format(comp, indentIndex = 0, mode = NEW_LINE) shouldBe expectedResult
            }

            should("format with 'EmptyLineStatement'") {
                val innerStatement = WhiteSpaceStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "\n"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "$_______8${innerStatement.text}"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns innerStatement.text
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns innerStatement.text
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = DICT, expression = innerStatement,
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
                val innerStatement = ExpressionStatement(
                    VoidFunctionCall(
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
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val tripleQuote = "\"\"\""
                every {
                    statementFormatter(innerStatement, indentIndex = 1, mode = NEW_LINE)
                } returns """
                    *genrule(
                    *name = "update_" + file[0:-3],
                    *srcs = [file],
                    *outs = [file[0:-3] + "_updated.kt"],
                    *cmd = $tripleQuote
                    *cat $(SRCS) |
                    *sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *$tripleQuote,
                    *)
                """.trimMargin("*")
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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
                val innerStatement = WhiteSpaceStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "\n"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns innerStatement.text
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "$_______8${innerStatement.text}"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "$_______8${innerStatement.text}"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = DICT, expression = innerStatement,
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
                val innerStatement = ExpressionStatement(
                    VoidFunctionCall(
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
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val tripleQuote = "\"\"\""
                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns """
                    *${_______8}genrule(
                    *${__________12}name = "update_" + file[0:-3],
                    *${__________12}srcs = [file],
                    *${__________12}outs = [file[0:-3] + "_updated.kt"],
                    *${__________12}cmd = $tripleQuote
                    *${______________16}cat $(SRCS) |
                    *${______________16}sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *${__________12}$tripleQuote,
                    *${_______8})
                """.trimMargin("*")
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
                    `for` = "file", `in` = listRef
                )

                val expectedResult = """
                    *[
                    *${_______8}genrule(
                    *${__________12}name = "update_" + file[0:-3],
                    *${__________12}srcs = [file],
                    *${__________12}outs = [file[0:-3] + "_updated.kt"],
                    *${__________12}cmd = $tripleQuote
                    *${______________16}cat $(SRCS) |
                    *${______________16}sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *${__________12}$tripleQuote,
                    *${_______8})
                    *${_______8}for file in VIEW_MODELS_WITH_RESOURCE_IMPORTS
                    *$___4]
                """.trimMargin("*")

                formatter.format(comp, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedResult
            }

            should("format with 'EmptyLineStatement'") {
                val innerStatement = WhiteSpaceStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = CONTINUE_LINE)
                } returns "\n"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "$_______8${innerStatement.text}"

                every {
                    expressionFormatter(listRef, indentIndex = 0, mode = CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter().apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns innerStatement.text
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns innerStatement.text
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = DICT, expression = innerStatement,
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
                val innerStatement = ExpressionStatement(
                    VoidFunctionCall(
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
                )
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                val tripleQuote = "\"\"\""
                every {
                    statementFormatter(innerStatement, indentIndex = 1, mode = NEW_LINE)
                } returns """
                    *genrule(
                    *name = "update_" + file[0:-3],
                    *srcs = [file],
                    *outs = [file[0:-3] + "_updated.kt"],
                    *cmd = $tripleQuote
                    *cat $(SRCS) |
                    *sed 's/import org.morfly.android.R/import org.morfly.android.app.view_models.R/g' > $(OUTS)
                    *$tripleQuote,
                    *)
                """.trimMargin("*")
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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
                val innerStatement = WhiteSpaceStatement
                val listRef = ListReference<String>("VIEW_MODELS_WITH_RESOURCE_IMPORTS")

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns "\n"
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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

                every {
                    statementFormatter(innerStatement, indentIndex = 2, mode = NEW_LINE)
                } returns innerStatement.text
                every {
                    expressionFormatter(listRef, indentIndex = 0, CONTINUE_LINE)
                } returns "VIEW_MODELS_WITH_RESOURCE_IMPORTS"

                val formatter = ComprehensionFormatter(indentSize = 0).apply {
                    this.expressionFormatter = expressionFormatter
                    this.statementFormatter = statementFormatter
                }

                val comp = Comprehension(
                    type = LIST, expression = innerStatement,
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