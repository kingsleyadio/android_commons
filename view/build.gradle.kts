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
    val androidCore: String by rootProject.extra
    implementation("androidx.core:core-ktx:$androidCore")
    val googleMaterial: String by rootProject.extra
    implementation("com.google.android.material:material:$googleMaterial")
    val androidCardView: String by rootProject.extra
    implementation("androidx.cardview:cardview:$androidCardView")
    val picassoVersion: String by rootProject.extra
    implementation("com.squareup.picasso:picasso:$picassoVersion")

    testImplementation(kotlin("test-junit"))
}

kotlin {
    explicitApi()
}
