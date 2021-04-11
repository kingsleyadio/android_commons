plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    val androidCore: String by rootProject
    implementation("androidx.core:core-ktx:$androidCore")
    val androidExif: String by rootProject
    implementation("androidx.exifinterface:exifinterface:$androidExif")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
