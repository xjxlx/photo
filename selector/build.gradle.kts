@Suppress("DSL_SCOPE_VIOLATION") plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.io.github.xjxlx.publishing)
}

android {

    namespace = "com.android.selector"
    compileSdk = libs.versions.compileSdks.get()
        .toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get()
            .toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    // 更改缓存时间
    configurations.all {
        resolutionStrategy {
            cacheChangingModulesFor(0,"seconds")
        }
    }
}

dependencies {
    api(libs.pictureselector)// 图片选择库
    api(libs.compress)// 图片压缩
    // glide 图片加载库 ，尽量自己使用，避免版本冲突
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
    implementation(libs.exoplayer)// 视频加载
    implementation(libs.kotlin.reflect)
}
