plugins {
    kotlin("jvm")
    id("integration-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//kotlin.target.compilations.getByName("test") {
//    associateWith(target.compilations.getByName("main"))
//}

dependencies {
    api(project(":descriptor"))

    implementation(kotlin("stdlib"))

    // -- test --
    val kotestVersion: String by rootProject.extra
    val mockkVersion: String by rootProject.extra

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")

    // -- integration test --
    integrationTestImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    integrationTestImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
}