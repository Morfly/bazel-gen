package org.morfly.example.generator

import org.morfly.bazelgen.generator.writer.BazelFileWriter
import org.morfly.bazelgen.generator.writer.DefaultFileWriter
import org.morfly.example.template.bazelrc_template
import org.morfly.example.template.root_build_template
import org.morfly.example.template.tools_build_template
import org.morfly.example.template.workspace_template
import java.io.File
import java.util.*


const val GENERATED_PROJECT_ROOT_DIR = "../generated-project"

private const val ROOT_PACKAGE_NAME = "org.morfly.bazelgen.example"


/**
 *
 */
class ProjectGenerator {

    private val internalDeps = LinkedList<String>()

    private val workspaceDir = File(GENERATED_PROJECT_ROOT_DIR)

    private val bazelFileWriter = BazelFileWriter.newInstance()

    private val moduleGenerator = AndroidDataBindingModuleGenerator(
        workspaceDir,
        rootPackageName = ROOT_PACKAGE_NAME,
        bazelFileWriter = bazelFileWriter,
        fileWriter = DefaultFileWriter()
    )

    /**
     *
     */
    fun generate(numOfModules: Int, disableStrictJavaDeps: Boolean = false) {
        require(numOfModules > 0) { "number of data binding modules must be positive." }

//        workspaceDir.deleteRecursively()
        workspaceDir.mkdir()

        generateToolsBuild()
        generateWorkspace()
        generateBazelRc(disableStrictJavaDeps)

        for (i in (numOfModules - 1) downTo 0) {
            val label = moduleGenerator.generate(i, internalDeps, i == 0)
            if (disableStrictJavaDeps) internalDeps.clear()
            internalDeps.add(0, label)
        }

        generateRootBuild()

        internalDeps.clear()
    }

    private fun generateWorkspace() {
        val workspace = workspace_template(
            "androidx-data-binding-sample",
            listOf(
                "androidx.databinding:databinding-adapters:3.4.2",
                "androidx.databinding:databinding-common:3.4.2",
                "androidx.databinding:databinding-compiler:3.4.2",
                "androidx.databinding:databinding-runtime:3.4.2",
                "androidx.annotation:annotation:1.1.0",
            )
        )
        bazelFileWriter.write(workspaceDir, workspace)
    }

    private fun generateBazelRc(disableStrictJavaDeps: Boolean) {
        val bazelRc = bazelrc_template(
            androidToolsLocation = "${workspaceDir.canonicalPath}/tools/android/android_tools",
            disableStrictJavaDeps = disableStrictJavaDeps
        )
        bazelFileWriter.write(workspaceDir, bazelRc)
    }

    private fun generateToolsBuild() {
        File("src/main/resources/android_tools").copyRecursively(
            target = File("$GENERATED_PROJECT_ROOT_DIR/tools/android/android_tools"),
            overwrite = true
        )
        try {
            File("src/main/resources/bazel").copyTo(
                target = File("$GENERATED_PROJECT_ROOT_DIR/tools/bazel"),
                overwrite = false
            )
        } catch (e: FileAlreadyExistsException) {
        }
        File("src/main/resources/kotlin").copyRecursively(
            target = File("$GENERATED_PROJECT_ROOT_DIR/tools/kotlin"),
            overwrite = true
        )
        val toolsBuild = tools_build_template()
        bazelFileWriter.write(workspaceDir, toolsBuild)
    }

    private fun generateRootBuild() {
        val build = root_build_template(
            binaryName = "example_app",
            packageName = ROOT_PACKAGE_NAME,
            internalDeps = internalDeps,
            externalDeps = emptyList()
        )
        bazelFileWriter.write(workspaceDir, build)
    }
}