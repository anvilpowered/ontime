dependencies {
    api(project(":ontime-api"))
    api(libs.anvil.core)

    api(platform(libs.exposed.bom))
    api(libs.bundles.exposed) {
        exclude(group = "org.slf4j")
    }

    compileOnlyApi(libs.luckperms)
    // provided by each platform, no need to declare as a transitive dependency
    compileOnly(platform(libs.adventure.bom))
    compileOnly("net.kyori:adventure-text-serializer-legacy")
    compileOnly("net.kyori:adventure-text-serializer-plain")

    runtimeOnly(libs.driver.mariadb) {
        exclude(group = "org.slf4j")
    }
    runtimeOnly(libs.driver.postgresql)
}
