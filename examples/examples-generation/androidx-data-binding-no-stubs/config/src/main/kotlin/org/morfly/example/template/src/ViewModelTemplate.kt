package org.morfly.example.template.src

import org.morfly.bazelgen.descriptor.PackageName


fun view_model_template(
    vmPackageName: PackageName,
    vmClassName: String,
    vmPropertyName: String,
    vmPropertyValue: String
    /**
     *
     */
) = """
package $vmPackageName


class $vmClassName {

    val $vmPropertyName: String
        get() = "$vmPropertyValue"
}
""".trimIndent()