plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 23
        targetSdk = 30
        versionCode = 1
        versionName = "0.0.3"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
}

dependencies {
    implementation("com.acquired.paymentgateway:paymentgateway:0.0.3")

    implementation(Libraries.viewModelKTX)

    implementation("androidx.compose.ui:ui-viewbinding:1.0.0-rc01")
    implementation(Libraries.material)
    implementation(Libraries.composeViewModel)
    implementation(Libraries.composeUI)
    implementation(Libraries.composeMaterial)
    implementation(Libraries.composeUITooling)
    implementation(Libraries.lifeCycleKTX)
    implementation(Libraries.composeActivity)
    implementation(Libraries.composeLiveData)

    coreLibraryDesugaring(Libraries.desugar)

    implementation(Libraries.kotlinStdlib)

    testImplementation(Libraries.junit5)
    testRuntimeOnly(Libraries.junit5Engine)
    testImplementation(Libraries.kotest)
    testImplementation(Libraries.mockk)
}