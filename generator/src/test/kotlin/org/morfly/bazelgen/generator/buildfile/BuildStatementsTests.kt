package org.morfly.bazelgen.generator.buildfile

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class BuildStatementsTests : ShouldSpec({

    should("'RuleStatement' type be the same as 'FunctionStatement'") {
        RuleStatement::class shouldBe FunctionStatement::class
    }
})