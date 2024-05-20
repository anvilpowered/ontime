plugins {
    id("ontime-publish")
    id("ontime-sign")
}

dependencies {
    implementation(libs.anvil.core)
    compileOnly(platform(libs.adventure.bom))
    compileOnly("net.kyori:adventure-text-serializer-legacy")
    compileOnly("net.kyori:adventure-text-serializer-plain")
}
