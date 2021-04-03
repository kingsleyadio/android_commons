buildscript {
    apply(from = "gradle/versions.gradle")

    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        val kotlinVersion: String by project
        classpath("com.android.tools.build:gradle:4.1.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}

subprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
    val commonAndroid: AppliedPlugin.() -> Unit = {
        configure<com.android.build.gradle.BaseExtension> {
            val targetSdkVersion = 29
            compileSdkVersion(targetSdkVersion)
            defaultConfig {
                minSdk = 16
                targetSdk = targetSdkVersion
                vectorDrawables.useSupportLibrary = true
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
            lintOptions {
                isAbortOnError = false
            }
        }
        if (id == "com.android.library") {
            apply(from = "../gradle/publishing.gradle")
        }
    }
    pluginManager.withPlugin("com.android.application", commonAndroid)
    pluginManager.withPlugin("com.android.library", commonAndroid)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
