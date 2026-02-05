plugins {

    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.kotlin.compose)

    alias(libs.plugins.kotlin.parcelize)

    alias(libs.plugins.ksp)

    alias(libs.plugins.hilt.android)

}

android {
    namespace = "uz.coderqwerty.taskwithgooglemap"
    compileSdk = 35

    defaultConfig {
        applicationId = "uz.coderqwerty.taskwithgooglemap"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // MANA SHU BLOKNI QO'SHING:
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

// ===================> I added <========================

// GoogleMap
    // Android Maps Compose composables for the Maps SDK for Android
    implementation("com.google.maps.android:maps-compose:6.4.1")
    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    // Clustering uchun utils
    implementation("com.google.maps.android:maps-compose-utils:6.5.1")

// Hilt
    implementation(libs.hilt.android)
    implementation(libs.play.services.location)
    ksp(libs.hilt.android.compiler)

//Timber
    implementation(libs.timber)

//Voyager
    // Navigator
    implementation(libs.voyager.navigator)
    // BottomSheetNavigator
    implementation(libs.voyager.bottom.sheet.navigator)
    // TabNavigator
    implementation(libs.voyager.tab.navigator)
    // Transitions
    implementation(libs.voyager.transitions)
    // Hilt integration
    implementation(libs.voyager.hilt)

// ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)


// ========================> Automatic added <====================================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}