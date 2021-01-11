@file:Suppress("FunctionName", "SpellCheckingInspection")

package org.morfly.bazelgen.generator.dsl

import org.morfly.bazelgen.generator.dsl.core.BazelRcStatement
import org.morfly.bazelgen.generator.dsl.core.BuildStatement
import org.morfly.bazelgen.generator.file.BazelRc


/**
 *
 */
inline fun bazelrc(relativePath: String = "", body: BazelRcContext.() -> Unit): BazelRc =
    BazelRcContext()
        .apply(body)
        .statements
        .let { BazelRc(it, namePrefix = "", relativePath) }

/**
 *
 */
inline fun String.bazelrc(relativePath: String = "", body: BazelRcContext.() -> Unit): BazelRc =
    BazelRcContext()
        .apply(body)
        .statements
        .let { BazelRc(it, namePrefix = this, relativePath) }

/**
 *
 */
interface bazelrc

/**
 *
 */
inline fun bazelrc.bazelrc(relativePath: String = "", body: BazelRcContext.() -> Unit): BazelRc =
    BazelRcContext()
        .apply(body)
        .statements
        .let {
            BazelRc(
                it,
                namePrefix = this::class.simpleName ?: error("'bazelrc' prefix must not be anonymous object."),
                relativePath
            )
        }

/**
 *
 */
@StarlarkFeatureContext
class BazelRcContext {

    private val statementsHolder = mutableListOf<BuildStatement>()

    /**
     *
     */
    val statements: List<BuildStatement> get() = statementsHolder

    /**
     *
     */
    fun build(config: String? = null, body: BazelRcCommandContext.() -> Unit) =
        declareBuildOptions("build", config, body)

    /**
     *
     */
    fun `mobile-install`(config: String? = null, body: BazelRcCommandContext.() -> Unit) =
        declareBuildOptions("mobile-install", config, body)

    /**
     *
     */
    operator fun String.invoke(config: String? = null, body: BazelRcCommandContext.() -> Unit) =
        declareBuildOptions(this, config, body)


    private fun declareBuildOptions(command: String, config: String? = null, body: BazelRcCommandContext.() -> Unit) {
        BazelRcCommandContext()
            .apply(body)
            .options
            .map { BazelRcStatement(command, config?.formatConfig(), it) }
            .let(statementsHolder::addAll)
    }

    private fun String.formatConfig(): String =
        if (startsWith(":")) substring(1, length)
        else this

    /**
     *
     */
    fun import(bazelrc: () -> String) {
        statementsHolder.add(BazelRcStatement("import", null, bazelrc()))
    }

    /**
     *
     */
    fun `try-import`(bazelrc: () -> String) {
        statementsHolder.add(BazelRcStatement("try-import", null, bazelrc()))
    }
}

/**
 *
 */
@StarlarkFeatureContext
class BazelRcCommandContext {

    private val optionsHolder = mutableListOf<String>()

    /**
     *
     */
    val options: List<String> get() = optionsHolder

    /**
     *
     */
    operator fun String.unaryMinus() {
        optionsHolder.add(formatOption())
    }

    /**
     *
     */
    operator fun List<String>.unaryMinus() {
        map { it.formatOption() }.let(optionsHolder::addAll)
    }

    private fun String.formatOption(): String =
        when {
            startsWith("--") -> this
            startsWith("-") -> "-$this"
            else -> "--$this"
        }
}