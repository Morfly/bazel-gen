import org.gradle.api.tasks.SourceSet.MAIN_SOURCE_SET_NAME

plugins {
    kotlin("jvm")
}


val sourceSets = the<SourceSetContainer>()

sourceSets.create("integrationTest") {
    compileClasspath += sourceSets[MAIN_SOURCE_SET_NAME].output
    runtimeClasspath += sourceSets[MAIN_SOURCE_SET_NAME].output
}

configurations["integrationTestImplementation"].extendsFrom(configurations["implementation"])
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations["runtimeOnly"])

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter("test")
}

tasks.named("check") {
    dependsOn(integrationTest)
}