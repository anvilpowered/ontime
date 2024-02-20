dependencies {
    api(project(":ontime-api"))
    api(libs.anvil.core)
    compileOnlyApi(libs.luckperms)
    runtimeOnly(libs.driver.mariadb)
    runtimeOnly(libs.driver.postgresql)
}
