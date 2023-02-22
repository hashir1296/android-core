@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("maven-publish")

}

android {
    namespace = "com.darvis.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33
        version = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Room
    val room = "2.4.2"
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    kapt("androidx.room:room-compiler:$room")

    // Hilt dependencies
    val hiltVersion = "2.44.2"
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    //Data store
    val dataStore = "1.0.0"
    implementation("androidx.datastore:datastore-preferences:$dataStore")
}

afterEvaluate {
    publishing{
        publications {
            create<MavenPublication>("maven"){
                groupId = "com.github.hashir1296"
                artifactId = "android-core"
                version = "1.0.0"

                afterEvaluate {
                    from(components["debug"])
                }

            }
        }
    }
}