// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.jetbrains.kotlinx.kover") version "0.9.0-RC"
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.google.firebase.crashlytics) apply false
}

buildscript {
    allprojects {
        tasks.withType<Test>().configureEach {
            testLogging {
                outputs.upToDateWhen { false }
                showStandardStreams = true
            }
            // モジュールを跨いだ依存が必要なユニットテストではhiltを上手く使うのが難しいみたい、
            // テスト用の実装を自前で注入するために環境変数を用意する
            systemProperty("isTestEnvironment", "true")
        }
    }
}