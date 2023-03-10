plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("maven-publish")
    id("dagger.hilt.android.plugin")
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
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java", "src/test/java")
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.1.3")

    //Kotlin Coroutines
    val coroutinesVersion = "1.6.4"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    //Okhttp & Stetho
     val okhttp = "4.9.0"
    implementation("com.squareup.okhttp3:logging-interceptor:${okhttp}")
    implementation("com.squareup.okhttp3:okhttp:${okhttp}")
    implementation("com.facebook.stetho:stetho-okhttp3:1.6.0")

    val ktor_version = "2.2.3"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-auth:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")



    //Serializer
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    //Socket.io
    implementation("io.socket:socket.io-client:2.1.0") {
        // excluding org.json which is provided by Android
        exclude(group = "org.json", module = "json")
    }

    // Hilt dependencies
     val hilt = "2.44.2"
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-android-compiler:$hilt")

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