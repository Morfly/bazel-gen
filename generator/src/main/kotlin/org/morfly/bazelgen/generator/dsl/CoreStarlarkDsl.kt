@file:Suppress("PropertyName", "FunctionName", "SpellCheckingInspection")

package org.morfly.bazelgen.generator.dsl

import org.morfly.bazelgen.generator.dsl.core.*
import org.morfly.bazelgen.generator.dsl.core.element.*
import org.morfly.bazelgen.generator.dsl.core.element.ComprehensionType.DICT
import org.morfly.bazelgen.generator.dsl.core.element.ComprehensionType.LIST
import org.morfly.bazelgen.generator.dsl.feature.*
import org.morfly.bazelgen.generator.file.BazelBuild
import org.morfly.bazelgen.generator.file.BazelWorkspace


/**
 *
 */
@DslMarker
internal annotation class StarlarkFeatureContext

/**
 *
 */
interface StarlarkDslFeature


/**
 *
 */
abstract class StarlarkContext : RawTextFeature, QuotingFeature, AssignmentsFeature, VisibilityFeature, LoadFeature,
    CollectionsFeature, ComprehensionsFeature, StatementConcatenationFeature, FormattingFeature, ReferencesFeature,
    FunctionsFeature, WhiteSpacesFeature {

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
        AssignmentStatement(this, value)
            .also(statementsHolder::add)

    override fun <T> String.`=`(value: List<T>): AssignmentStatement<List<T>> =
        AssignmentStatement(this, value)
            .also(statementsHolder::add)

    override fun String.`=`(body: DictionaryContext.() -> Unit): AssignmentStatement<Map<String, Any?>> =
        AssignmentStatement(
            this,
            // TODO document workaround description
            if (body is Reference) DictionaryReference(body.name)
            else DictionaryContext().apply(body).kwargs
        ).also(statementsHolder::add)
    // endregion

    // region ComprehensionsFeature
    override fun BuildStatement.invoke(
        `for`: String, `in`: Any, `if`: String?, dict: DictComprehensionIdentifier
    ): ExpressionStatement<Comprehension> {
        statementsHolder.remove(this)
        return ExpressionStatement(
            Comprehension(
                type = if (dict != null) DICT else LIST,
                expression = this,
                `for`, `in`, `if`
            )
        ).also(statementsHolder::add)
    }
    // endregion

    // region FunctionsFeature
    override fun String.invoke(body: FunctionCallContext.() -> Unit): ExpressionStatement<FunctionCall> =
        functionCallStatement(this, FunctionCallContext().apply(body).kwargs)


    internal fun <T : FunctionCallContext> functionCallStatement(
        name: String, context: T, body: T.() -> Unit
    ): ExpressionStatement<FunctionCall> =
        functionCallStatement(name, context.apply(body).kwargs)

    internal fun functionCallStatement(function: FunctionCall): ExpressionStatement<FunctionCall> =
        functionCallStatement(function.name, function.args)

    internal fun functionCallStatement(name: String, args: Map<String, Any?>): ExpressionStatement<FunctionCall> =
        ExpressionStatement(VoidFunctionCall(name, args)).also(statementsHolder::add)
    // endregion

    // region LoadFeature
    override fun load(file: String, vararg rules: CharSequence) =
        LoadStatement(file, rules.associateBy({ it.toString() }, { null }))
            .also(statementsHolder::add)

    // endregion

    // region ConcatenationsFeature
    override fun AssignmentStatement<CharSequence>.`+`(other: CharSequence?): AssignmentStatement<CharSequence> {
        statementsHolder.remove(this)
        return AssignmentStatement(
            name = name,
            value = StringConcatenation(value, "+", other)
        ).also(statementsHolder::add)
    }

    override fun <T> AssignmentStatement<List<T>>.`+`(other: List<T>?): AssignmentStatement<List<T>> {
        statementsHolder.remove(this)
        return AssignmentStatement(
            name = name,
            value = ListConcatenation(value, "+", other)
        ).also(statementsHolder::add)
    }

    override fun AssignmentStatement<Map<String, Any?>>.`+`(other: Map<String, Any?>?): AssignmentStatement<Map<String, Any?>> {
        statementsHolder.remove(this)
        return AssignmentStatement(
            name = name,
            value = DictionaryConcatenation(value, "+", other)
        ).also(statementsHolder::add)
    }

    override fun <T> AssignmentStatement<T>.`+`(other: ExpressionStatement<T>): AssignmentStatement<Concatenation<T?, T?>> {
        statementsHolder.remove(this)
        statementsHolder.remove(other)
        return AssignmentStatement(
            name = name,
            value = AnyConcatenation(value, "+", other.expression)
        ).also(statementsHolder::add)
    }
    // endregion

    // region WhiteSpacesFeature
    override val space
        get() = WhiteSpaceStatement.also(statementsHolder::add)
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