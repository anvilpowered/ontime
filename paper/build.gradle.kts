import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(libs.plugins.pluginyml)
}

dependencies {
    implementation(project(":ontime-core"))
    implementation(libs.anvil.paper)
    compileOnly(libs.paper)
}

paper {
    name = "ontime"
    main = "org.anvilpowered.ontime.paper.OnTimePaperBootstrap"
    serverDependencies {
        register("LuckPerms") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }
    foliaSupported = true
    apiVersion = "1.20"
    authors = rootProject.file("authors").readLines().map { it.substringBefore(',') }
}
