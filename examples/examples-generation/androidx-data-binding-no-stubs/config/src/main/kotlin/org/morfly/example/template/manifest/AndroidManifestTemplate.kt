package org.morfly.example.template.manifest

import org.morfly.bazelgen.descriptor.PackageName


fun android_manifest_template(
    packageName: PackageName
    /**
     *
     */
) = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="$packageName">
</manifest>    
""".trimIndent()