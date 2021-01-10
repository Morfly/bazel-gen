package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.morfly.bazelgen.generator.dsl.core.*
import org.morfly.bazelgen.generator.formatter.IndentMode.CONTINUE_LINE
import org.morfly.bazelgen.generator.formatter.IndentMode.NEW_LINE


class BuildStatementFormatterTests : ShouldSpec({
    val justTextFormatter = mockk<JustTextFormatter>()
    val expressionFormatter = mockk<ExpressionFormatter>()
    val assignmentFormatter = mockk<AssignmentFormatter>()
    val loadFormatter = mockk<LoadFormatter>()
    val bazelRcFormatter = mockk<BazelRcFormatter>()
    val statementFormatter = BuildStatementFormatter(
        justTextFormatter, expressionFormatter, assignmentFormatter, loadFormatter, bazelRcFormatter
    )

    context("modes NEW_LINE, CONTINUE_LINE") {

        should("call 'JustTextFormatter' for 'RawTextStatement'") {
            every { justTextFormatter("text", 0, NEW_LINE) } returns "text"
            every { justTextFormatter("text", 0, CONTINUE_LINE) } returns "text"

            val statement = RawTextStatement("text")

            statementFormatter.format(statement, 0, NEW_LINE) shouldBe "text"
            statementFormatter.format(statement, 0, CONTINUE_LINE) shouldBe "text"
        }

        should("call 'ExpressionFormatter' for 'ExpressionStatement'") {
            every { expressionFormatter("expression", 0, NEW_LINE) } returns "expression"
            every { expressionFormatter("expression", 0, CONTINUE_LINE) } returns "expression"

            val statement = ExpressionStatement("expression")

            statementFormatter.format(statement, 0, NEW_LINE) shouldBe "expression"
            statementFormatter.format(statement, 0, CONTINUE_LINE) shouldBe "expression"
        }

        should("call 'LoadFormatter' for 'LoadStatement'") {
            val statement = LoadStatement("file.bzl", mapOf("symbol" to null))

            every { loadFormatter(statement, 0, NEW_LINE) } returns "load"
            every { loadFormatter(statement, 0, CONTINUE_LINE) } returns "load"

            statementFormatter.format(statement, 0, NEW_LINE) shouldBe "load"
            statementFormatter.format(statement, 0, CONTINUE_LINE) shouldBe "load"
        }

        should("call 'AssignmentFormatter' for 'AssignmentStatement'") {
            val statement = AssignmentStatement("arg", "value")

            every { assignmentFormatter(statement.name to statement.value, 0, NEW_LINE) } returns "assignment"
            every { assignmentFormatter(statement.name to statement.value, 0, CONTINUE_LINE) } returns "assignment"

            statementFormatter.format(statement, 0, NEW_LINE) shouldBe "assignment"
            statementFormatter.format(statement, 0, CONTINUE_LINE) shouldBe "assignment"
        }

        should("call 'BazelRcFormatter' for 'BazelRcStatement'") {
            val statement = BazelRcStatement("mobile-install", null, "--start_app")

            every { bazelRcFormatter(statement) } returns "bazelrc"

            statementFormatter.format(statement, 0, NEW_LINE) shouldBe "bazelrc"
            statementFormatter.format(statement, 0, CONTINUE_LINE) shouldBe "bazelrc"
        }

        should("return new line char for 'WhiteSpaceStatement'") {
            val lineSeparator = System.getProperty("line.separator")

            statementFormatter.format(WhiteSpaceStatement, 0, NEW_LINE) shouldBe lineSeparator
            statementFormatter.format(WhiteSpaceStatement, 0, CONTINUE_LINE) shouldBe lineSeparator
        }
    }

})