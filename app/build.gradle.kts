plugins {
    alias(libs.plugins.android.application)

    // add this line serially
    alias(libs.plugins.kotlin.android)  // builtInKotlin=false এর কারণে দরকার
    alias(libs.plugins.ksp)             // ksp সবসময় hilt এর আগে
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.nnote"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.nnote"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // udpate java version to 17
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        viewBinding = true
    }

    // add this lines
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)



    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // room
    val roomVersion = "2.8.4"
    implementation ("androidx.room:room-runtime:$roomVersion")
    ksp ("androidx.room:room-compiler:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")

}