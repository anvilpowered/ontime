plugins {
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}
dependencies {
    implementation(project(":ontime-core"))
    implementation(libs.anvil.velocity)
    api(platform("net.kyori:adventure-bom:4.15.0"))
    api("net.kyori:adventure-text-serializer-legacy")
    api("net.kyori:adventure-text-serializer-plain")
    kapt(libs.velocity)
}
