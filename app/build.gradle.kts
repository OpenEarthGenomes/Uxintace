plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    // Ez legyen pontosan az, ami a csomagnév a fájljaid tetején
    namespace = "com.example.uxintace"
    compileSdk = 35 // Az Android 16-hoz jelenleg a 35-ös vagy "android-35" stabil SDK kell

    defaultConfig {
        applicationId = "com.example.uxintace"
        minSdk = 26
        targetSdk = 35 
        versionCode = 1
        versionName = "1.0-A16"

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

    // Ez kritikus az Android 16-hoz és a modern Kotlinhoz
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
