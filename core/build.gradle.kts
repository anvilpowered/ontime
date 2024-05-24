dependencies {
    api(project(":ontime-api"))
    api(libs.anvil.core)

    api(platform(libs.exposed.bom))
    api(libs.bundles.exposed) {
        exclude(group = "org.slf4j")
    }

    compileOnlyApi(libs.luckperms)

    runtimeOnly(libs.driver.mariadb) {
        exclude(group = "org.slf4j")
    }
    runtimeOnly(libs.driver.postgresql)
}
