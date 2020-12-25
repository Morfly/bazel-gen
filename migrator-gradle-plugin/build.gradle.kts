plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        create("bazelgen") {
            id = "org.morfly.bazelgen"
            implementationClass = "org.morfly.bazelgen.migrator.plugin.SmithMigratorGradlePlugin"
        }
    }
}

dependencies {
    implementation(project(":descriptor"))
    implementation(project(":generator"))

    val androidGradlePluginVersion: String by rootProject.extra

    implementation(embeddedKotlin("gradle-plugin"))
    implementation("com.android.tools.build:gradle:$androidGradlePluginVersion")

    // -- test --
    val kotestVersion: String by rootProject.extra
    val mockkVersion: String by rootProject.extra

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
}