plugins {
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}
dependencies {
    implementation(project(":ontime-core"))
    implementation(libs.anvil.velocity)

    kapt(libs.velocity)
}
