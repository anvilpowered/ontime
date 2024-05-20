dependencies {
    api(project(":ontime-api"))
    api(libs.anvil.core)

    api(platform(libs.exposed.bom))
    api(libs.bundles.exposed)

    compileOnlyApi(libs.luckperms)
    // provided by each platform, no need to declare as a transitive dependency
    compileOnly(platform(libs.adventure.bom))
    compileOnly("net.kyori:adventure-text-serializer-legacy")
    compileOnly("net.kyori:adventure-text-serializer-plain")

    runtimeOnly(libs.driver.mariadb)
    runtimeOnly(libs.driver.postgresql)
}
