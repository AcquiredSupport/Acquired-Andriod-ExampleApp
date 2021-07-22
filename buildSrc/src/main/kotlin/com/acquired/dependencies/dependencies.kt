
object Versions {
    const val kotlin = "1.5.10"
    const val compose = "1.0.0-rc01"
}

object SDKInfo {
    const val version = "0.0.1"
    const val group = "com.acquired.paymentgateway"
}

object Libraries {
    private object LibVersions {
        //run "gradlew dependencyUpdates" "./gradlew dependencyUpdates" (mac/linux) to check if there are updates to the dependencies
        const val composeActivity = "1.3.0-rc01"
        const val composeLiveData = "1.0.0-rc01"
        const val composeViewModel = "1.0.0-alpha07"
        const val coroutine = "1.5.0"
        const val desugar = "1.1.5"
        const val junit5 = "5.7.2"
        const val json = "20210307"
        const val libPhoneNumber = "8.12.27"
        const val lifeCycleKTX = "2.3.1"
        const val kotest = "4.6.0"
        const val material = "1.3.0"
        const val mockk = "1.12.0"
        const val mockwebserver = "4.9.1"
        const val playService = "18.1.3"
        const val retrofit = "2.9.0"
        const val serialization = "1.2.1"
        const val serialConverter = "0.8.0"
        const val viewModelKTX = "2.3.1"

    }

    //android
    const val desugar = "com.android.tools:desugar_jdk_libs:${LibVersions.desugar}"

    //android ui
    const val composeActivity = "androidx.activity:activity-compose:${LibVersions.composeActivity}"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${LibVersions.composeViewModel}"
    const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeUITooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata:${LibVersions.composeLiveData}"
    const val lifeCycleKTX = "androidx.lifecycle:lifecycle-runtime-ktx:${LibVersions.lifeCycleKTX}"
    const val material = "com.google.android.material:material:${LibVersions.material}"
    const val viewModelKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibVersions.viewModelKTX}"

    //google
    const val playService = "com.google.android.gms:play-services-wallet:${LibVersions.playService}"

    //network
    const val retrofit = "com.squareup.retrofit2:retrofit:${LibVersions.retrofit}"
    const val serialConverter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${LibVersions.serialConverter}"
    const val mockwebserver = "com.squareup.okhttp3:mockwebserver:${LibVersions.mockwebserver}"

    //kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:${LibVersions.serialization}"
    const val coroutine = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${LibVersions.coroutine}"
    const val coroutinePlayService = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${LibVersions.coroutine}"

    //other
    const val libPhoneNumber = "com.googlecode.libphonenumber:libphonenumber:${LibVersions.libPhoneNumber}"

    //test libraries
    const val junit5 = "org.junit.jupiter:junit-jupiter:${LibVersions.junit5}"
    const val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${LibVersions.junit5}"
    const val json = "org.json:json:${LibVersions.json}"
    const val kotest = "io.kotest:kotest-assertions-core-jvm:${LibVersions.kotest}"
    const val mockk = "io.mockk:mockk:${LibVersions.mockk}"
}

