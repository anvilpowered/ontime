plugins {
    id("ontime-publish")
    id("ontime-sign")
}

dependencies {
    implementation(libs.anvil.core)

    compileOnlyApi(platform(libs.adventure.bom))
    compileOnlyApi("net.kyori:adventure-text-serializer-legacy")
    compileOnlyApi("net.kyori:adventure-text-serializer-plain")
}
