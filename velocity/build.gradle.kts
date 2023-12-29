plugins {
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}
dependencies {
    api(libs.anvil.core)
    api(platform("net.kyori:adventure-bom:4.15.0"))
    api("net.kyori:adventure-text-serializer-legacy")
    api("net.kyori:adventure-text-serializer-plain")

    implementation(project(":ontime-api"))
    implementation(project(":ontime-core"))
    implementation(libs.anvil.velocity)
    kapt(libs.velocity)
}
