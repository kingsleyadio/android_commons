plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    val androidCore: String by rootProject.extra
    implementation("androidx.core:core-ktx:$androidCore")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.jakewharton.timber:timber:4.7.1")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
