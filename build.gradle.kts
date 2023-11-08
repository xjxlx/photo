buildscript {
    repositories {
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }
}

@Suppress("DSL_SCOPE_VIOLATION") plugins {
    // id("com.android.application") version "7.4.2" apply false
    // id("com.android.library") version "7.4.2" apply false
    // id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
}
true