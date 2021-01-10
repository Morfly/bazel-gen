package org.morfly.bazelgen.generator.dsl.core.element


/**
 *
 */
enum class ComprehensionType { LIST, DICT }


/**
 *
 */
data class Comprehension(
    val type: ComprehensionType,
    val expression: Expression,
    val `for`: String, val `in`: Expression, val `if`: String? = null
) : Expression(), StarlarkDslElement