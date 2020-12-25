package org.morfly.bazelgen.generator.formatter

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe


class BaseTextFormatterTests : ShouldSpec({
    val formatter = BaseTextFormatter()

    should("format null value") {
        formatter.format(null) shouldBe "None"
    }

    should("format boolean values") {
        formatter.format(true) shouldBe "True"
        formatter.format(false) shouldBe "False"
    }

    should("return string representation of objects") {
        val obj = Pair("arg1", "arg2")

        formatter.format(obj) shouldBe "(arg1, arg2)"
    }

    should("return string representation of numbers") {
        val int = 42
        val float = 42f
        val double = 42.0

        formatter.format(int) shouldBe "42"
        formatter.format(float) shouldBe "42.0"
        formatter.format(double) shouldBe "42.0"
    }

    should("not format string values") {
        formatter.format("string") shouldBe "string"
    }
})