package org.morfly.bazelgen.generator.dsl.core

import org.morfly.bazelgen.generator.dsl.core.element.Assignment
import org.morfly.bazelgen.generator.dsl.core.element.Expression


/**
 *
 */
sealed class BuildStatement

/**
 * @param text
 */
data class RawTextStatement(val text: String) : BuildStatement()

/**
 * @param expression
 */
data class ExpressionStatement<out T : Expression>(val expression: T) : BuildStatement()


/**
 *
 */
typealias Symbol = String

/**
 *
 */
typealias Alias = String

/**
 * @param file
 * @param symbols
 */
data class LoadStatement(val file: String, val symbols: Map<Symbol, Alias?>) : BuildStatement()

/**
 *
 */
data class AssignmentStatement<out T>(
    override val name: String,
    override val value: T?
) : BuildStatement(), Assignment<T>

/**
 *
 */
object WhiteSpaceStatement : BuildStatement()

/**
 *
 */
data class BazelRcStatement(
    val command: String,
    val config: String?,
    val option: String
) : BuildStatement()