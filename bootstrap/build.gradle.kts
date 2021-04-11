plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    implementation(project(":util"))
    implementation(kotlin("stdlib-jdk8"))
    val androidCore: String by rootProject
    implementation("androidx.core:core-ktx:$androidCore")
    val androidAppCompat: String by rootProject
    implementation("androidx.appcompat:appcompat:$androidAppCompat")
    val timberVersion: String by rootProject
    implementation("com.jakewharton.timber:timber:$timberVersion")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
