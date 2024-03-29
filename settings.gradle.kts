@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.anvilpowered.org/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        maven("https://libraries.minecraft.net")
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "ontime"

sequenceOf(
    "api",
    "core",
    "paper",
    "velocity",
).forEach {
    val project = ":ontime-$it"
    include(project)
    project(project).projectDir = file(it.replace('-', '/'))
}
