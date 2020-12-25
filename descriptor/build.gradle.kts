plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    // region test
    val kotestVersion: String by rootProject.extra
    val mockkVersion: String by rootProject.extra

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
    // endregion
}