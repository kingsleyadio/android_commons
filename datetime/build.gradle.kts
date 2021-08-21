plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    buildFeatures.viewBinding = true
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    val androidCore: String by rootProject
    implementation("androidx.core:core-ktx:$androidCore")
    val googleMaterial: String by rootProject
    implementation("com.google.android.material:material:$googleMaterial")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
