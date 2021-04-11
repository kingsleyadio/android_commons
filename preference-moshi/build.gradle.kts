plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    api(project(":preference"))
    api("com.squareup.moshi:moshi:1.12.0")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
