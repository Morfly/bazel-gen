package org.morfly.bazelgen.generator.dsl.type

import org.morfly.bazelgen.generator.buildfile.Assignment


/**
 *
 */
data class ValueAssignment<T>(override val name: String, override val value: T) : Assignment<T>, StarlarkDslType