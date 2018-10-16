import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "io.rybalkinsd"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("jvm") version "1.2.71"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

repositories {
    jcenter()
}

val ktlint by configurations.creating

dependencies {
    compile(kotlin("stdlib-jdk8"))
    testCompile("junit", "junit", "4.12")

    ktlint("com.github.shyiko", "ktlint", "0.28.0")
}

tasks {
    val ktlint by creating(JavaExec::class) {
        group = "verification"
        description = "Check Kotlin code style."
        main = "com.github.shyiko.ktlint.Main"
        classpath = ktlint
        args = listOf("src/**/*.kt")
    }

    "check" {
        dependsOn(ktlint)
    }
}

val fatJar = task("fatJar", type = Jar::class) {
    baseName = "${project.name}-fat"
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    from(
            configurations.runtime.map {
                if (it.isDirectory) it else zipTree(it)
            }
    )
    with(tasks["jar"] as CopySpec)
}
