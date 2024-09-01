plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.parcelize) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kapt) apply false
//    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.sqldelight) apply false
}

// compose metrics
//subprojects {
//    val compose_metrics_path = "$buildDir/compose_metrics"
//
//    tasks.withType(KotlinCompile).configureEach {
//        kotlinOptions {
//            freeCompilerArgs += listOf("-P",
//                                 "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$compose_metrics_path")
//            freeCompilerArgs += listOf("-P",
//                                 "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$compose_metrics_path")
//        }
//    }
//}