plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    defaultConfig {
        applicationId = "com.kingsleyadio.appcommons.demo"
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":bootstrap"))
    implementation(project(":view"))
    implementation(project(":datetime"))
    val kotlinVersion: String by rootProject
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    val googleMaterial: String by rootProject
    implementation("com.google.android.material:material:$googleMaterial")

    testImplementation(kotlin("test-junit"))
}
