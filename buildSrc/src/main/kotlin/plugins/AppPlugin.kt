package plugins

import App
import Libs
import Plugins
import Versions
import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class AppPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        applyPlugins(project)
        applyAppExtension(project)
        importLibs(project)
    }

    // Configure plugins
    private fun applyPlugins(project: Project) {
        project.plugins.apply {
            apply(Plugins.AndroidApplication)
            apply(Plugins.KotlinAndroid)
            apply(Plugins.KotlinKapt)
            apply(Plugins.Google)
            apply(Plugins.FirebaseCrash)
            apply(Plugins.FirebasePerf)
            apply(Plugins.Hilt)
        }
    }

    //Configure Android block
    private fun applyAppExtension(project: Project) {

        val extension = project.extensions.getByName("android")
                as? AppExtension ?: return
        extension.apply {
            compileSdkVersion(App.CompileSdk)
            buildToolsVersion(App.BuildTools)

            defaultConfig {
                applicationId = App.Id
                minSdkVersion(App.MinSdk)
                targetSdkVersion(App.TargetSdk)
                versionCode = App.VersionCode
                versionName = App.VersionName
                vectorDrawables.useSupportLibrary = true
                multiDexEnabled = true
                testInstrumentationRunner = App.AndroidJunitRunner
            }

            buildTypes {
                named("debug") {
                    versionNameSuffix = "-dev"
                    buildConfigField("String", "API_BASE", "\"https://timtb.dev/api/\"")
                    isMinifyEnabled = false
                    isDebuggable = true
                    isTestCoverageEnabled = true
                    manifestPlaceholders["crashlyticsEnabled"] = false
                }
                named("release") {
                    buildConfigField("String", "API_BASE", "\"https://timtb.dev/api/\"")
                    // Enables code shrinking for the release build type.
                    isMinifyEnabled = true
                    isDebuggable = false
                    isShrinkResources = true
                    manifestPlaceholders["crashlyticsEnabled"] = true
                    proguardFiles("/settings/proguard_files/proguard-square-retrofit.pro")
                    proguardFiles("/settings/proguard_files/proguard-gson.pro")
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }
            sourceSets {
                named("main") {
                    java {
                        srcDir("src/main/kotlin")
                    }
                }

                named("debug") {
                    java {
                        srcDir("src/debug/kotlin")
                    }
                }
            }

            buildFeatures.apply {
                viewBinding = true
                compose = true
                buildConfig = true

            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            project.tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    freeCompilerArgs = listOf(
                        "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        "-Xopt-in=kotlinx.coroutines.FlowPreview",
                        "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi",
                        "-Xopt-in=kotlin.time.ExperimentalTime"
                    )
                    jvmTarget = "${JavaVersion.VERSION_1_8}"
                }

            }

            composeOptions {
                kotlinCompilerExtensionVersion = Versions.Compose
            }

            lintOptions {
                isCheckReleaseBuilds = false
            }

            packagingOptions {
                resources.excludes.add("META-INF/AL2.0")
                resources.excludes.add("META-INF/LGPL2.1")
            }

            applicationVariants.forEach { variant ->
                variant.outputs.forEach { output ->
                    val outputImpl = output as BaseVariantOutputImpl
                    val name = project.name
                    val sep = "_"
                    val flavor = variant.flavorName
                    val buildType = variant.buildType.name
                    val version = variant.versionName

                    val newApkName = "$name$sep$flavor$sep$buildType$sep$version.apk"
                    outputImpl.outputFileName = newApkName
                }
            }

        }
    }

    // Configure dependencies

    private fun importLibs(project: Project){
        project.dependencies {
            // Kotlin
            add("implementation", Libs.Kotlin.Stdlib)
            add("implementation", Libs.Kotlin.Coroutines)
            // Material Design
            add("implementation", Libs.Google.MaterialDesign)
            // Hilt
            add("implementation", Libs.Google.Hilt.Android)
            add("implementation", Libs.Google.Hilt.NavigationCompose)
            add("kapt", Libs.Google.Hilt.Compiler)
            // Accompanist
            add("implementation", Libs.Google.Accompanist.Coil)
            add("implementation", Libs.Google.Accompanist.Pager)
            add("implementation", Libs.Google.Accompanist.FlowLayout)
            add("implementation", Libs.Google.Accompanist.Indicator)
            // AndroidX
            add("implementation", Libs.AndroidX.Ktx)
            add("implementation", Libs.AndroidX.AppCompat)
            add("implementation", Libs.AndroidX.ConstraintLayoutCompose)
            add("implementation", Libs.AndroidX.DataStorePreference)
            // Compose
            add("implementation", Libs.AndroidX.Compose.Ui)
            add("implementation", Libs.AndroidX.Compose.UiTooling)
            add("implementation", Libs.AndroidX.Compose.Foundation)
            add("implementation", Libs.AndroidX.Compose.Material)
            add("implementation", Libs.AndroidX.Compose.Icons)
            add("implementation", Libs.AndroidX.Compose.IconsExt)
            add("implementation", Libs.AndroidX.Compose.Activity)
            add("implementation", Libs.AndroidX.Compose.ViewModel)
            add("androidTestImplementation", Libs.AndroidX.Compose.UiTest)
            // Navigation
            add("implementation", Libs.AndroidX.NavigationCompose)
            // Room
            add("implementation", Libs.AndroidX.Room.Runtime)
            add("implementation", Libs.AndroidX.Room.Ktx)
            add("kapt", Libs.AndroidX.Room.Kapt)
            // Work
            add("implementation", Libs.AndroidX.Work)
            // Firebase
            add("implementation", platform(Libs.Firebase.Bom))
            add("implementation", Libs.Firebase.Analytics)
            add("implementation", Libs.Firebase.Crashlytics)
            add("implementation", Libs.Firebase.Performance)
            // Square
            add("implementation", Libs.Square.Retrofit)
            add("implementation", Libs.Square.GsonConverter)
            add("implementation", Libs.Square.OkHttp)
            add("implementation", Libs.Square.Gson)
            add("implementation", Libs.Square.LeakCanary)
            add("testImplementation", Libs.Square.MockWebServer)
            // Junit5
            add("testImplementation", Libs.Junit.API)
            add("testRuntimeOnly", Libs.Junit.Engine)
        }
    }
}
