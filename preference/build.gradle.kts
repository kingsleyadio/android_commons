plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    defaultConfig.consumerProguardFiles("consumer-rules.pro")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":util"))

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
