plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("kapt")
}

dependencies {
    commonMainImplementation(project(":ontime-app-plugin-core"))
    jvmMainCompileOnly(libs.velocity)
    kapt(libs.velocity)
    commonMainImplementation(libs.kbrig.brigadier)
}
