@Suppress("DSL_SCOPE_VIOLATION") plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.android.app"
    compileSdk = libs.versions.compileSdks.get()
        .toInt()

    defaultConfig {
        applicationId = "com.android.app"
        minSdk = libs.versions.minSdk.get()
            .toInt()
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 28
        versionCode = 2
        versionName = "1.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isDebuggable = true
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(project(mapOf("path" to ":selector")))
}
