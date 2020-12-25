package org.morfly.bazelgen.generator.buildfile


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
data class ExpressionStatement(val expression: Any?) : BuildStatement()

/**
 * @param file
 * @param rules key - rule name, value - alias val rules: Map<String, String> TODO
 */
data class LoadStatement(val file: String, val rules: List<String>) : BuildStatement()

/**
 * @param name
 * @param args
 */
data class FunctionStatement(override val name: String, override val args: Map<String, Any?>) : BuildStatement(),
    FunctionCall

/**
 *
 */
typealias RuleStatement = FunctionStatement

/**
 * @param name
 * @param value
 */
data class AssignmentStatement<T>(override val name: String, override val value: T) : BuildStatement(), Assignment<T>

/**
 *
 */
enum class ComprehensionType { LIST, DICT }

/**
 * @param type
 * @param statement
 * @param for
 * @param in
 * @param if
 */
data class ComprehensionStatement(
    val type: ComprehensionType,
    val statement: BuildStatement,
    val `for`: String, val `in`: Any, val `if`: String? = null
) : BuildStatement()

/**
 * @param left
 * @param operator
 * @param right
 */
data class ConcatenationStatement(
    override val left: BuildStatement,
    override val operator: String,
    override val right: Any?
) : BuildStatement(), Concatenation<BuildStatement>

/**
 * @param command
 * @param config
 * @param option
 */
data class BazelRcStatement(
    val command: String,
    val config: String?,
    val option: String
) : BuildStatement()

/**
 *
 */
object EmptyLineStatement : BuildStatement()