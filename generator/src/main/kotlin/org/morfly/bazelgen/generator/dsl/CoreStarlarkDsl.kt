@file:Suppress("PropertyName", "FunctionName", "SpellCheckingInspection")

package org.morfly.bazelgen.generator.dsl

import org.morfly.bazelgen.generator.buildfile.*
import org.morfly.bazelgen.generator.buildfile.ComprehensionType.DICT
import org.morfly.bazelgen.generator.buildfile.ComprehensionType.LIST
import org.morfly.bazelgen.generator.dsl.feature.*
import org.morfly.bazelgen.generator.dsl.type.DictionaryReference
import org.morfly.bazelgen.generator.dsl.type.Reference


/**
 *
 */
@DslMarker
internal annotation class StarlarkFeatureContext

/**
 *
 */
interface StarlarkLanguageFeature


/**
 *
 */
abstract class StarlarkContext : RawTextFeature, QuotingFeature, AssignmentsFeature, VisibilityFeature, LoadFeature,
    CollectionsFeature, ComprehensionsFeature, StatementConcatenationFeature, FormattingFeature, ReferencesFeature,
    FunctionsFeature {

    protected val statementsHolder = mutableListOf<BuildStatement>()

    /**
     *
     */
    val statements: List<BuildStatement> get() = statementsHolder


    // region RawTextFeature
    override val String.txt: Unit
        get() {
            statementsHolder.add(RawTextStatement(this))
        }

    override fun String.unaryMinus() = txt
    // endregion

    // region AssignmentsFeature
    override fun String.`=`(value: Any): AssignmentStatement<Any> =
        this assign value

    override fun <T> String.`=`(value: List<T>): AssignmentStatement<List<T>> =
        this assign value

    override fun String.`=`(body: DictionaryContext.() -> Unit): AssignmentStatement<Map<String, Any?>> =
        this assign
                // TODO document workaround description
                if (body is Reference) DictionaryReference(body.name)
                else DictionaryContext().apply(body).kwargs


    private infix fun <V> String.assign(value: V): AssignmentStatement<V> =
        AssignmentStatement(this, value).also(statementsHolder::add)
    // endregion

    // region ComprehensionsFeature
    override fun BuildStatement.invoke(
        `for`: String, `in`: Any, `if`: String?, dict: DictComprehensionIdentifier
    ): ComprehensionStatement {
        statementsHolder.remove(this)
        return ComprehensionStatement(
            type = if (dict != null) DICT else LIST,
            statement = this,
            `for`, `in`, `if`
        ).also(statementsHolder::add)
    }
    // endregion

    // region FunctionsFeature
    override fun String.invoke(body: FunctionCallContext.() -> Unit): FunctionStatement =
        FunctionStatement(this, FunctionCallContext().apply(body).kwargs)
            .also(statementsHolder::add)

    internal fun <T : FunctionCallContext> functionCallStatement(
        name: String, context: T, body: T.() -> Unit
    ): FunctionStatement = functionCallStatement(name, context.apply(body).kwargs)

    internal fun functionCallStatement(function: FunctionCall): FunctionStatement =
        functionCallStatement(function.name, function.args)

    internal fun functionCallStatement(name: String, args: Map<String, Any?>): FunctionStatement =
        FunctionStatement(name, args).also(statementsHolder::add)
    // endregion

    // region LoadFeature
    override fun load(file: String, vararg rules: CharSequence) {
        statementsHolder.add(
            LoadStatement(file, rules.map { it.toString() }.toList())
        )
    }
    // endregion

    // region ConcatenationsFeature
    override fun <T> AssignmentStatement<T>.`+`(other: Any?): ConcatenationStatement {
        statementsHolder.remove(this)
        if (other is BuildStatement) statementsHolder.remove(other)
        return ConcatenationStatement(this, "+", other)
            .also(statementsHolder::add)
    }

    override fun ConcatenationStatement.`+`(other: Any?): ConcatenationStatement {
        statementsHolder.remove(this)
        if (other is BuildStatement) statementsHolder.remove(other)
        return ConcatenationStatement(this, "+", other)
            .also(statementsHolder::add)
    }
    // endregion
}


// region WORKSPACE
/**
 *
 */
class WorkspaceContext : StarlarkContext()

/**
 *
 */
inline fun WORKSPACE(body: WorkspaceContext.() -> Unit): BazelWorkspace =
    WorkspaceContext()
        .apply(body)
        .let { BazelWorkspace(hasExtension = false, statements = it.statements) }


/**
 * A DSL operator for declaring [WORKSPACE.bazel] file with explicit extension.
 */
object WORKSPACE

/**
 *
 */
inline fun WORKSPACE.bazel(body: WorkspaceContext.() -> Unit): BazelWorkspace =
    WorkspaceContext()
        .apply(body)
        .let { BazelWorkspace(hasExtension = true, statements = it.statements) }

// endregion


// region BUILD
/**
 *
 */
class BuildContext : StarlarkContext()

/**
 *
 */
inline fun BUILD(relativePath: CharSequence = "", body: BuildContext.() -> Unit): BazelBuild =
    BuildContext()
        .apply(body)
        .let { BazelBuild(hasExtension = false, relativePath, statements = it.statements) }

/**
 * DSL operator for declaring [BUILD.bazel] file with explicit extension.
 */
object BUILD

/**
 *
 */
inline fun BUILD.bazel(relativePath: CharSequence = "", body: BuildContext.() -> Unit): BazelBuild =
    BuildContext()
        .apply(body)
        .let { BazelBuild(hasExtension = true, relativePath, statements = it.statements) }
// endregion