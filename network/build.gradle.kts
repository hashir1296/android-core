plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
}

android {
    namespace = "com.darvis.network"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        version = "1.0.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
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

}

dependencies {
    //Kotlin Coroutines
    val coroutinesVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // Hilt dependencies
    val hiltVersion = "2.44.2"
    implementation("com.google.dagger:hilt-android:2.44.2")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    //Okhttp & Stetho
     val okhttp = "4.9.0"
    implementation("com.squareup.okhttp3:logging-interceptor:${okhttp}")
    implementation("com.squareup.okhttp3:okhttp:${okhttp}")
    implementation("com.facebook.stetho:stetho-okhttp3:1.6.0")

    val ktor_version = "2.2.3"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")


    //Serializer
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //Socket.io
    implementation("io.socket:socket.io-client:2.1.0") {
        // excluding org.json which is provided by Android
        exclude(group = "org.json", module = "json")
    }
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