plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    kotlin("plugin.serialization")
//    alias(libs.plugins.google.gms.google.services)
//    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.neesan.nicokeyshifter2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.neesan.nicokeyshifter2"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.neesan.nicokeyshifter2.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    
    // Robolectricのテストで必要なための設定
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // dagger hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // okhttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.kotlin.serialization)

    // kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    
    // テスト用の依存関係
    // HiltとRobolectricのテスト用依存関係
    testImplementation(libs.hilt.android.testing)
//    kspTest("com.google.dagger:hilt-android-compiler:2.48.1")
    testImplementation("org.robolectric:robolectric:4.14.1")
    
    // コルーチンのテスト用
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}