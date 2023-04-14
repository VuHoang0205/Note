import deps.dependOn
import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

val versions = rootProject.file("version.properties")
val props = Properties()
props.load(FileInputStream(versions))
val major = props["majorVersion"].toString().toInt()
val minor = props["minorVersion"].toString().toInt()
val patch = props["patchVersion"].toString().toInt()

android {
    compileSdk = Build.compileSdk

    defaultConfig {
        applicationId = Build.applicationId
        minSdk = Build.minSdk
        targetSdk = Build.targetSdk
        versionCode = 10000 * major + 1000 * minor + 10 * patch
        versionName = "$major.$minor.$patch"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = deps.Compose.Versions.composeCompiler
    }

    applicationVariants.all(ApplicationVariantAction())
}

repositories {
    mavenCentral()
    google()
}

dependencies {

//    // https://mvnrepository.com/artifact/com.itextpdf/itextpdf
//    implementation("com.itextpdf:itextpdf:5.5.13.3")
//
//    // https://mvnrepository.com/artifact/org.apache.poi/poi
//    implementation("org.apache.poi:poi:5.2.3")
//
//    // https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml
//    implementation("org.apache.poi:poi-ooxml:5.2.3")


    // Module dependencies
    implementation(project(":feature:ads"))
    implementation(project(":feature:purchase"))
    implementation(project(":base:theme"))
    implementation(project(":base:common"))
    implementation(project(":base:ui"))
    implementation(project(":data:persistence"))

    // Standard dependencies
    dependOn(
        deps.AndroidX,
        deps.Hilt,
        deps.Compose,
        deps.Gson,
        deps.Log,
        deps.Glide,
        deps.Coroutine,
        deps.Room,
        deps.CameraX,
        deps.Test,
        deps.Ads,
        deps.Billing,
        deps.RevealSwipe,
        deps.DateTimePicker,
    )
}

class ApplicationVariantAction : Action<com.android.build.gradle.api.ApplicationVariant> {
    override fun execute(variant: com.android.build.gradle.api.ApplicationVariant) {
        val fileName = createFileName(variant)
        variant.outputs.all(VariantOutputAction(fileName))
    }

    private fun createFileName(variant: com.android.build.gradle.api.ApplicationVariant): String {
        return "NoteApp.apk"
    }

    class VariantOutputAction(
        private val fileName: String
    ) : Action<com.android.build.gradle.api.BaseVariantOutput> {
        override fun execute(output: com.android.build.gradle.api.BaseVariantOutput) {
            if (output is com.android.build.gradle.internal.api.BaseVariantOutputImpl) {
                output.outputFileName = fileName
            }
        }
    }
}

