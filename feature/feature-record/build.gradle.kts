plugins {
    id("crfpos-feature")
}

android {
    namespace = "com.example.feature_record"
}

dependencies {
    implementation(project(":core:database"))
}
