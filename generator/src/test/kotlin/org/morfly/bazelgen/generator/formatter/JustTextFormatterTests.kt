@file:Suppress("LocalVariableName")

package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE

class JustTextFormatterTests : ShouldSpec({

    context("mode NEW_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = JustTextFormatter()

            context("single-line strings") {
                val singleLineString = "Single-line string"

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(singleLineString, indentIndex = 0, mode = NEW_LINE) shouldBe singleLineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    formatter.format(
                        singleLineString, indentIndex = 1, mode = NEW_LINE
                    ) shouldBe "$___4$singleLineString"
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    formatter.format(
                        singleLineString, indentIndex = 2, mode = NEW_LINE
                    ) shouldBe "$_______8$singleLineString"
                }
            }

            context("multiline strings") {
                val multilineString = """
                    Multiline
                    string
                """.trimIndent()

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(multilineString, indentIndex = 0, mode = NEW_LINE) shouldBe multilineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    val expectedString = """
                        |${___4}Multiline
                        |${___4}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 1, mode = NEW_LINE) shouldBe expectedString
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    val expectedString = """
                        |${_______8}Multiline
                        |${_______8}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 2, mode = NEW_LINE) shouldBe expectedString
                }
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = JustTextFormatter(indentSize = 3)

            context("single-line strings") {
                val singleLineString = "Single-line string"

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(singleLineString, indentIndex = 0, mode = NEW_LINE) shouldBe singleLineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    formatter.format(
                        singleLineString, indentIndex = 1, mode = NEW_LINE
                    ) shouldBe "$__3$singleLineString"
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    formatter.format(
                        singleLineString, indentIndex = 2, mode = NEW_LINE
                    ) shouldBe "$_____6$singleLineString"
                }
            }

            context("multiline strings") {
                val multilineString = """
                    Multiline
                    string
                """.trimIndent()

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(multilineString, indentIndex = 0, mode = NEW_LINE) shouldBe multilineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    val expectedString = """
                        |${__3}Multiline
                        |${__3}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 1, mode = NEW_LINE) shouldBe expectedString
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    val expectedString = """
                        |${_____6}Multiline
                        |${_____6}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 2, mode = NEW_LINE) shouldBe expectedString
                }
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = JustTextFormatter(indentSize = 0)

            val singleLineString = "Single-line string"
            val multilineString = """
                Multiline
                string
            """.trimIndent()

            should("return unchanged string in all cases") {
                formatter.format(singleLineString, indentIndex = 0, mode = NEW_LINE) shouldBe singleLineString
                formatter.format(multilineString, indentIndex = 0, mode = NEW_LINE) shouldBe multilineString

                formatter.format(singleLineString, indentIndex = 1, mode = NEW_LINE) shouldBe singleLineString
                formatter.format(multilineString, indentIndex = 1, mode = NEW_LINE) shouldBe multilineString

                formatter.format(singleLineString, indentIndex = 2, mode = NEW_LINE) shouldBe singleLineString
                formatter.format(multilineString, indentIndex = 2, mode = NEW_LINE) shouldBe multilineString
            }
        }
    }

    context("mode CONTINUE_LINE") {
        context("with default 'indentSize' (4)") {
            val ___4 = " ".repeat(4)
            val _______8 = " ".repeat(8)

            val formatter = JustTextFormatter()

            context("single-line strings") {
                val singleLineString = "Single-line string"

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(singleLineString, indentIndex = 0, mode = CONTINUE_LINE) shouldBe singleLineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    formatter.format(singleLineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe singleLineString
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    formatter.format(singleLineString, indentIndex = 2, mode = CONTINUE_LINE) shouldBe singleLineString
                }
            }

            context("multiline strings") {
                val multilineString = """
                    Multiline
                    string
                """.trimIndent()

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(multilineString, indentIndex = 0, mode = CONTINUE_LINE) shouldBe multilineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    val expectedString = """
                        |Multiline
                        |${___4}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedString
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    val expectedString = """
                        |Multiline
                        |${_______8}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 2, mode = CONTINUE_LINE) shouldBe expectedString
                }
            }
        }

        context("with custom 'indentSize' (3)") {
            val __3 = " ".repeat(3)
            val _____6 = " ".repeat(6)

            val formatter = JustTextFormatter(indentSize = 3)

            context("single-line strings") {
                val singleLineString = "Single-line string"

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(singleLineString, indentIndex = 0, mode = CONTINUE_LINE) shouldBe singleLineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    formatter.format(singleLineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe singleLineString
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    formatter.format(singleLineString, indentIndex = 2, mode = CONTINUE_LINE) shouldBe singleLineString
                }
            }

            context("multiline strings") {
                val multilineString = """
                    Multiline
                    string
                """.trimIndent()

                should("return unchanged string when 'indentIndex' is 0") {
                    formatter.format(multilineString, indentIndex = 0, mode = CONTINUE_LINE) shouldBe multilineString
                }

                should("return correctly indented string when 'indentIndex' is 1") {
                    val expectedString = """
                        |Multiline
                        |${__3}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe expectedString
                }

                should("return correctly indented string when 'indentIndex' is 2") {
                    val expectedString = """
                        |Multiline
                        |${_____6}string
                    """.trimMargin()

                    formatter.format(multilineString, indentIndex = 2, mode = CONTINUE_LINE) shouldBe expectedString
                }
            }
        }

        context("with no 'indentSize' (0)") {
            val formatter = JustTextFormatter(indentSize = 0)

            val singleLineString = "Single-line string"
            val multilineString = """
                Multiline
                string
            """.trimIndent()

            should("return unchanged string in all cases") {
                formatter.format(singleLineString, indentIndex = 0, mode = CONTINUE_LINE) shouldBe singleLineString
                formatter.format(multilineString, indentIndex = 0, mode = CONTINUE_LINE) shouldBe multilineString

                formatter.format(singleLineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe singleLineString
                formatter.format(multilineString, indentIndex = 1, mode = CONTINUE_LINE) shouldBe multilineString

                formatter.format(singleLineString, indentIndex = 2, mode = CONTINUE_LINE) shouldBe singleLineString
                formatter.format(multilineString, indentIndex = 2, mode = CONTINUE_LINE) shouldBe multilineString
            }
        }
    }
})