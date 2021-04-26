object Plugins {

    const val AndroidApplication: String = "com.android.application"
    const val KotlinAndroid: String = "kotlin-android"
    const val KotlinKapt: String = "kotlin-kapt"
    const val Hilt: String = "dagger.hilt.android.plugin"
    const val Google: String = "com.google.gms.google-services"
    const val FirebaseCrash: String = "com.google.firebase.crashlytics"
    const val FirebasePerf: String = "com.google.firebase.firebase-perf"

    const val VersionUpdate = "com.github.ben-manes.versions"
    val Gradle: String
        get() = "com.android.tools.build:gradle:${Versions.Gradle}"
    val KotlinGradlePlugin: String
        get() = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin}"
    val HiltAndroidGradlePlugin: String
        get() = "com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt}"
    val GoogleServices: String
        get() = "com.google.gms:google-services:${Versions.Google}"
    val FirebaseCrashlyticsGradle: String
        get() = "com.google.firebase:firebase-crashlytics-gradle:${Versions.FirebaseCrashlytics}"
    val FirebasePerformancePlugin: String
        get() = "com.google.firebase:perf-plugin:${Versions.FirebasePerformance}"
}