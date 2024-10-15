plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
//    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.example.android.strikingarts"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.android.strikingarts"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.findByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

hilt {
    enableAggregatingTask = true
}

sqldelight {
    databases {
        create("LocalDatabase") { packageName.set("com.example.android.strikingarts") }
    }
}

dependencies {
    val composeBom = libs.androidx.compose.bom

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    // Desugaring Library for kotlinx.datetime support for apk version 25 and lower
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Compose
    implementation(platform(composeBom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.material3)
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.androidx.foundation)
    // Material design icons
    implementation(libs.androidx.material.icons.core)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.lifecycle.viewmodel.compose)
    // for collectAsStateWithLifecycle
    implementation(libs.androidx.lifecycle.runtime.compose)
    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // SQLDelight
    implementation(libs.android.driver)
    implementation(libs.coroutines.extensions)
    implementation(libs.primitive.adapters)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    // Navigation
    implementation(libs.androidx.navigation.compose)

    // PreferencesDataStore
    implementation(libs.androidx.datastore.preferences)

    // ColorPicker
    implementation(libs.colorpicker.compose)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Compose UI Tests
    androidTestImplementation(platform(composeBom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    //SqlDelight
    testImplementation(libs.sqlite.driver)
    // Hilt's instrumentation tests
    androidTestImplementation(libs.hilt.android.testing)
    androidTestAnnotationProcessor(libs.hilt.compiler)
    // Hilt's Unit Test
    testImplementation(libs.hilt.android.testing)
    testAnnotationProcessor(libs.hilt.compiler)
    // Square's Library for testing Flows
    testImplementation(libs.turbine)
    // More readable assertions
    testImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.kotest.assertions.core)
}