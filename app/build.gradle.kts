plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.yjotdev.accidentreporter"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.yjotdev.accidentreporter"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.2"
        testInstrumentationRunner = "com.yjotdev.accidentreporter.CustomTestRunner"
        // Variables globales en gradle
        val apiDomain = project.findProperty("APP_API_DOMAIN") as? String
            ?: error("La propiedad 'APP_API_DOMAIN' no se encontró en gradle.properties")
        val certPinIntermediate = project.findProperty("APP_CERT_PIN_INTERMEDIATE") as? String
            ?: error("La propiedad 'APP_CERT_PIN_INTERMEDIATE' no se encontró en gradle.properties")
        val certPinLeaf = project.findProperty("APP_CERT_PIN_LEAF") as? String
            ?: error("La propiedad 'APP_CERT_PIN_LEAF' no se encontró en gradle.properties")
        // Variables en BuildConfig
        buildConfigField("String", "API_DOMAIN", "\"$apiDomain\"")
        buildConfigField("String", "CERT_PIN_INTERMEDIATE", "\"$certPinIntermediate\"")
        buildConfigField("String", "CERT_PIN_LEAF", "\"$certPinLeaf\"")
        manifestPlaceholders.putAll(
            mapOf(
                "MAPS_API_KEY" to (project.findProperty("MAPS_API_KEY") ?: "")
            )
        )
    }
    signingConfigs {
        create("release") {
            keyAlias = project.findProperty("APP_KEY_ALIAS") as? String
            keyPassword = project.findProperty("APP_KEY_PASSWORD") as? String
            storePassword = project.findProperty("APP_STORE_PASSWORD") as? String
            storeFile = project.findProperty("APP_STORE_FILE")?.let { rootProject.file(it) }
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
}

dependencies {
    //UI
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.kotlin.metadata.jvm)
    //Maps
    implementation(libs.android.play.services.maps)
    implementation(libs.android.maps.compose)
    //Navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    //Retrofit
    implementation(libs.squareup.retrofit2)
    implementation(libs.squareup.retrofit2.gson)
    implementation(libs.google.code.gson)
    //Logging Interceptor
    implementation(libs.squareup.okhttp3.logging.interceptor)
    //Hilt
    implementation(libs.dagger.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.dagger.hilt.android.compiler)
    //Test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.core.ktx)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.androidx.navigation.testing)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}