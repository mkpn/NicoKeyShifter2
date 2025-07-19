plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
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
    implementation(projects.data)
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.presentation)

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

    // coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // Dagger Hilt テスト
    testImplementation(libs.hilt.android.testing)
    kspTest(libs.hilt.compiler)

    // Robolectric
    testImplementation(libs.robolectric)

    // コルーチンテスト
    testImplementation(libs.kotlinx.coroutines.test)

    // okhttp テスト用
    testImplementation(libs.okhttp)
    testImplementation(libs.okhttp.logging)

    // retrofit テスト用
    testImplementation(libs.retrofit)
    testImplementation(libs.retrofit.kotlin.serialization)

    // firebase
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.messaging)
}
