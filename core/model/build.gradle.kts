plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    namespace = "com.example.core_model"
}

kotlin {
    jvmToolchain(8)
}
