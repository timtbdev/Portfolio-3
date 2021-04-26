plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("app") {
            id = "app"
            implementationClass = "plugins.AppPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    sourceSets {
        named("main") {
            kotlin.apply {
                srcDir("buildSrc/src/main/kotlin")
            }
        }
    }
}

dependencies {
    implementation(Plugins.Gradle)
    implementation(Plugins.KotlinGradlePlugin)
    implementation(Plugins.HiltAndroidGradlePlugin)
    implementation(Plugins.GoogleServices)
    implementation(Plugins.FirebaseCrashlyticsGradle)
    implementation(Plugins.FirebasePerformancePlugin)

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle.kts files
}