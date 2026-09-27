import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

configure<ApplicationExtension> {
    namespace = "com.yjotdev.accidentreporter"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.yjotdev.accidentreporter"
        minSdk = 24
        targetSdk = 37
        versionCode = 5
        versionName = "1.5"
        testInstrumentationRunner = "com.yjotdev.accidentreporter.CustomTestRunner"
        androidResources.localeFilters += setOf("en", "es")
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
            // Key de Google enviada al manifesto
            val mapsApiKey = project.findProperty("MAPS_API_KEY_DEBUG") as String
            manifestPlaceholders.putAll(mapOf("MAPS_API_KEY" to mapsApiKey))
            buildConfigField("String", "API_BASE_URL", "\"${project.findProperty("API_BASE_URL_DEBUG")}\"")
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
            // Key de Google enviada al manifesto
            val mapsApiKey = project.findProperty("MAPS_API_KEY_RELEASE") as String
            manifestPlaceholders.putAll(mapOf("MAPS_API_KEY" to mapsApiKey))
            buildConfigField("String", "API_BASE_URL", "\"${project.findProperty("API_BASE_URL_RELEASE")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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
    testOptions {
        animationsDisabled = true
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
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.dagger.hilt.android.testing)
    androidTestImplementation(libs.androidx.navigation.testing)
    kspAndroidTest(libs.dagger.hilt.android.compiler)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

configurations.all {
    resolutionStrategy {
        force("androidx.concurrent:concurrent-futures:1.1.0")
    }
}