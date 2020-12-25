import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21" apply false
}

allprojects {
    group = "org.morfly.bazelgen"
    version = "0.0.1"

    repositories {
        mavenCentral()
        jcenter()
        google()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}


val androidGradlePluginVersion by extra("4.1.0")

val kotestVersion by extra("4.3.0")
val mockkVersion by extra("1.10.3-jdk8")