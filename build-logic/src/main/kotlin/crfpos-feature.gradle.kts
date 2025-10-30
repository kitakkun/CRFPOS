import com.android.build.gradle.BaseExtension
import util.libs

plugins {
    id("org.jetbrains.kotlin.android")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

kotlin {
    jvmToolchain(8)
}

configure<BaseExtension> {
    compileSdkVersion(libs.findVersion("compileSdk").get().requiredVersion.toInt())

    defaultConfig {
        minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildFeatures.compose = true

    buildTypes {
        maybeCreate("release").apply {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))


    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-appcompat").get())
    implementation(libs.findLibrary("material").get())

    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-ui").get())
    implementation(libs.findLibrary("androidx-ui-graphics").get())
    implementation(libs.findLibrary("androidx-ui-tooling-preview").get())
    implementation(libs.findLibrary("androidx-material3").get())
    implementation(libs.findLibrary("androidx-material-icons-core").get())

    implementation(libs.findLibrary("hilt-android").get())
    
    ksp(libs.findLibrary("hilt-compiler").get())
    ksp(libs.findLibrary("hilt-android-compiler").get())

    implementation(libs.findLibrary("androidx-activity-compose").get())

    testImplementation(libs.findLibrary("junit").get())
    androidTestImplementation(libs.findLibrary("androidx-junit").get())
    androidTestImplementation(libs.findLibrary("androidx-espresso-core").get())
    debugImplementation(libs.findLibrary("androidx-ui-tooling").get())
    debugImplementation(libs.findLibrary("androidx-ui-test-manifest").get())
}