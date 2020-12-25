package org.morfly.bazelgen.generator.type

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import org.morfly.bazelgen.generator.dsl.feature.ref
import org.morfly.bazelgen.generator.dsl.type.*


class ReferencesTests : ShouldSpec({

    should("'StringReference' be compatible with string type") {
        val ref = StringReference("NAME")

        ref should beInstanceOf<CharSequence>()
        ref.length shouldBe ref.name.length
        ref.subSequence(0, ref.length) shouldBe ref.name.subSequence(0, ref.name.length)
    }

    should("'ListReference' be compatible with list type") {
        val ref = ListReference<String>("NAME")

        ref should beInstanceOf<List<String>>()
        ref.size shouldBe 0
        ref.isEmpty() shouldBe true
    }

    should("'DictionaryReference' be compatible with map type") {
        val ref = DictionaryReference("NAME")

        ref should beInstanceOf<Map<String, Any?>>()
        ref.size shouldBe 0
        ref.isEmpty() shouldBe true
    }

    should("'AnyReference' be compatible with function1 type as a DSL workaround") {
        val ref = AnyReference("NAME")

        ref should beInstanceOf<Function1<Any?, *>>()
        ref.toString() shouldBe ref.name
    }

    context("String.ref") {
        val variableName = "NAME"

        should("auto resolve reference type") {
            val string: CharSequence = variableName.ref()
            val list: List<String> = variableName.ref()
            val dict: Map<String, *> = variableName.ref()
            val func1: Any.() -> Unit = variableName.ref()
            val func2: (Any) -> Unit = variableName.ref()
            val any: Any = variableName.ref()

            string should beInstanceOf<StringReference>()
            string.toString() shouldBe "NAME"

            list should beInstanceOf<ListReference<String>>()
            list.isEmpty() shouldBe true
            list.size shouldBe 0

            dict should beInstanceOf<DictionaryReference>()
            dict.size shouldBe 0

            func1 should beInstanceOf<AnyReference>()
            func2 should beInstanceOf<AnyReference>()
            any should beInstanceOf<AnyReference>()
        }
    }
})