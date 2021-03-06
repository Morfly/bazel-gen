package org.morfly.example.generator

import org.morfly.bazelgen.analyzer.PackageName
import org.morfly.bazelgen.generator.writer.BazelFileWriter
import org.morfly.bazelgen.generator.writer.DefaultFileWriter
import org.morfly.example.template.android_data_binding_library_build_template
import org.morfly.example.template.manifest.android_manifest_template
import org.morfly.example.template.manifest.main_android_manifest_template
import org.morfly.example.template.src.*
import java.io.File


class AndroidDataBindingModuleGenerator(
    private val workspaceDir: File,
    private val rootPackageName: PackageName,
    private val bazelFileWriter: BazelFileWriter,
    private val fileWriter: DefaultFileWriter
) {

    fun generate(moduleNumber: Int, internalDeps: List<String>, isRootModule: Boolean): String {
        val data = AndroidDataBindingModuleData(
            moduleNumber, rootPackageName, workspaceDir, internalDeps, isRootModule
        )

        generateSourceFiles(data)
        generateManifestFiles(data)
        generateBuild(data)

        return data.label
    }

    private fun generateBuild(data: AndroidDataBindingModuleData) = with(data) {
        val build = android_data_binding_library_build_template(
            relativePath = moduleName,
            dataBindingLayouts = dataBindingLayouts,
            viewModelsWithRes = emptyList(),
            viewModels = viewModels,
            bindingAdapters = bindingAdapters,
            targetName = moduleName,
            packageName = packageName,
            internalDeps = internalDeps,
            externalDeps = externalDeps,
            exportAndroidManifest = isRootModule
        )
        bazelFileWriter.write(workspaceDir, build)
    }

    private fun generateManifestFiles(data: AndroidDataBindingModuleData) = with(data) {
        with(fileWriter) {
            write(
                File("$manifestDir/AndroidManifest.xml"),
                if (isRootModule) main_android_manifest_template(packageName, "App", ".MainActivity")
                else android_manifest_template(packageName)
            )
            write(
                File("$manifestDir/ViewModelsManifest.xml"),
                android_manifest_template("$packageName.viewmodels")
            )
            write(
                File("$manifestDir/DataBindingResourcesManifest.xml"),
                android_manifest_template("$packageName.databinding")
            )
        }
    }

    private fun generateSourceFiles(data: AndroidDataBindingModuleData) = with(data) {
        val adapterName = "text$moduleNumber"
        val vmPropertyName = "text"
        val vmPropertyValue = "value from $moduleName"
        val vmVarName = "viewModel"

        with(fileWriter) {
            write(
                File("$resDir/layout/${dataBindingLayoutName(moduleName)}.xml"), binding_layout_template(
                    vmVarName = vmVarName,
                    vmPackageName = packageName,
                    vmClassName = viewModelClassName(moduleName),
                    adapterName = adapterName,
                    vmPropertyName = vmPropertyName
                )
            )
            write(
                File("$srcDir/BindingAdapters.java"),
                binding_adapters_template(packageName, adapterName)
            )
            write(
                File("$srcDir/${viewModelClassName(moduleName)}.kt"), view_model_template(
                    vmPackageName = packageName,
                    vmClassName = viewModelClassName(moduleName),
                    vmPropertyName = vmPropertyName,
                    vmPropertyValue = vmPropertyValue
                )
            )
            write(
                File("$srcDir/${moduleName.capitalize()}.kt"), regular_file_template(
                    packageName = packageName,
                    objectName = moduleName.capitalize(),
                    functionName = moduleName,
                    bindingClassName = "Layout${moduleName.capitalize()}Binding",
                    viewModelClassName = viewModelClassName(moduleName),
                    viewModelBindingPropertyName = vmVarName,
                    layoutName = dataBindingLayoutName(moduleName)
                )
            )
            if (isRootModule) {
                write(
                    File("$srcDir/MainActivity.kt"),
                    activity_template(
                        packageName = packageName,
                        className = "MainActivity",
                        bindingClassName = "LayoutAppBinding",
                        layoutName = dataBindingLayoutName(moduleName),
                        viewModelClassName = viewModelClassName(moduleName),
                        viewModelBindingPropertyName = vmVarName
                    )
                )
            }
        }
    }
}

private fun viewModelClassName(prefix: String) =
    "${prefix.capitalize()}ViewModel"

private fun dataBindingLayoutName(suffix: String) =
    "layout_${suffix.toLowerCase()}"


private data class AndroidDataBindingModuleData(
    val moduleNumber: Int,
    val moduleName: String,
    val packageName: PackageName,
    val label: String,
    val moduleRoot: String,
    val srcDir: String,
    val resDir: String,
    val manifestDir: String,
    val viewModels: List<String>,
    val bindingAdapters: List<String>,
    val dataBindingLayouts: List<String>,
    val internalDeps: List<String>,
    val externalDeps: List<String>,
    val isRootModule: Boolean
) {

    companion object {

        const val SRC_SUB_DIR = "src/main/kotlin"
        const val RES_SUB_DIR = "src/main/res"
        const val MANIFEST_SUB_DIR = "src/main"

        operator fun invoke(
            moduleNumber: Int,
            rootPackageName: PackageName,
            workspaceDir: File,
            internalDeps: List<String>,
            isRootModule: Boolean
        ): AndroidDataBindingModuleData {
            val moduleName = if (isRootModule) "app" else "lib$moduleNumber"
            val packageName = "$rootPackageName.$moduleName"
            val packageSubDir = packageName.replace(".", "/")
            val moduleRoot = "${workspaceDir.path}/$moduleName"
            return AndroidDataBindingModuleData(
                moduleNumber = moduleNumber,
                moduleName = moduleName,
                packageName = packageName,
                label = "//$moduleName",
                moduleRoot = moduleRoot,
                srcDir = "$moduleRoot/$SRC_SUB_DIR/$packageSubDir",
                resDir = "$moduleRoot/$RES_SUB_DIR",
                manifestDir = "$moduleRoot/$MANIFEST_SUB_DIR",
                viewModels = listOf("$SRC_SUB_DIR/$packageSubDir/${viewModelClassName(moduleName)}.kt"),
                bindingAdapters = listOf("$SRC_SUB_DIR/$packageSubDir/BindingAdapters.java"),
                dataBindingLayouts = listOf("$RES_SUB_DIR/layout/${dataBindingLayoutName(moduleName)}.xml"),
                internalDeps = internalDeps,
                externalDeps = emptyList(),
                isRootModule = isRootModule
            )
        }
    }
}