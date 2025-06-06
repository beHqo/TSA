plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.thestrikingarts"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.thestrikingarts"

        minSdk = 21
        targetSdk = 36

        val versionCode: String? by project
        val versionName: String? by project

        this.versionCode = versionCode?.toInt()
        this.versionName = versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        androidResources { generateLocaleConfig = true }

        vectorDrawables { useSupportLibrary = true }
    }

    signingConfigs {
        register("release") {
            val keystorePath = "../keystore.jks"
            val alias: String? by project
            val keyPassword: String? by project
            val keystorePassword: String? by project

            this.storeFile = file(keystorePath)
            this.keyAlias = alias
            this.keyPassword = keyPassword
            this.storePassword = keystorePassword
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
        create("LocalDatabase") { packageName.set("com.thestrikingarts") }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity.compose)

    // Desugaring Library for kotlinx.datetime support for apk version 25 and lower
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
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
    ksp(libs.hilt.compiler)
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
    androidTestImplementation(platform(libs.androidx.compose.bom))
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