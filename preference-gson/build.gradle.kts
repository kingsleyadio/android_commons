plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    api(project(":preference"))
    api("com.google.code.gson:gson:2.8.6")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
