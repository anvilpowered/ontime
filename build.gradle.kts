import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlin.serialization)
}

val projectVersion: String = run {
    val rawVersion = file("version").readLines().first()
    if (project.hasProperty("rawVersion")) {
        rawVersion
    } else {
        val branch = System.getenv("VCS_BRANCH")?.replace('/', '-') ?: "unknown-branch"
        System.getenv("BUILD_NUMBER")?.let { buildNumber ->
            val gitRev = ByteArrayOutputStream()
            exec {
                commandLine("git", "rev-parse", "--short", "HEAD")
                standardOutput = gitRev
            }.assertNormalExitValue()
            rawVersion.replace("SNAPSHOT", "BETA$buildNumber-$branch-${gitRev.toString().trim()}")
        } ?: rawVersion
    }
}

logger.warn("Resolved project version $projectVersion")

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "java-library")

    group = "org.anvilpowered"
    version = projectVersion

    kotlin {
        compilerOptions {
            freeCompilerArgs = listOf(
                "-opt-in=kotlin.RequiresOptIn",
                "-Xcontext-receivers",
            )
        }
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "21"
        }
        withType<JavaCompile> {
            options.encoding = "UTF-8"
            sourceCompatibility = "21"
            targetCompatibility = "21"
        }
    }
}

// for uber jar
dependencies {
    runtimeOnly(project(":ontime-paper"))
    runtimeOnly(project(":ontime-velocity"))
}

tasks {
    shadowJar {
        archiveFileName = "ontime-${project.version}.jar"
    }
}
