// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")

    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20-Beta")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.21")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.41")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.21")

    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

