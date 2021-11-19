
object Versions {
    const val kotlin = "1.5.31"
    const val compose = "1.0.5"
}

object Libraries {
    private object LibVersions {
        //run "gradlew dependencyUpdates" "./gradlew dependencyUpdates" (mac/linux) to check if there are updates to the dependencies
        const val composeActivity = "1.4.0"
        const val composeLiveData = "1.0.0"
        const val composeViewModel = "2.4.0"
        const val composeNavigation = "2.4.0-beta02"
        const val desugar = "1.1.5"
        const val junit5 = "5.8.1"
        const val lifeCycleKTX = "2.4.0"
        const val kotest = "4.6.3"
        const val material = "1.4.0"
        const val mockk = "1.12.0"
        const val viewModelKTX = "2.4.0"
    }

    //android
    const val desugar = "com.android.tools:desugar_jdk_libs:${LibVersions.desugar}"

    //android ui
    const val composeActivity = "androidx.activity:activity-compose:${LibVersions.composeActivity}"
    const val composeLiveData = "androidx.compose.runtime:runtime-livedata:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeNavigation = "androidx.navigation:navigation-compose:${LibVersions.composeNavigation}"
    const val composeUI = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeUITooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeViewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.compose}"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${LibVersions.composeViewModel}"
    const val lifeCycleKTX = "androidx.lifecycle:lifecycle-runtime-ktx:${LibVersions.lifeCycleKTX}"
    const val material = "com.google.android.material:material:${LibVersions.material}"
    const val viewModelKTX = "androidx.lifecycle:lifecycle-viewmodel-ktx:${LibVersions.viewModelKTX}"

    //kotlin
    const val kotlinStdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

    //test libraries
    const val junit5 = "org.junit.jupiter:junit-jupiter:${LibVersions.junit5}"
    const val junit5Engine = "org.junit.jupiter:junit-jupiter-engine:${LibVersions.junit5}"
    const val kotest = "io.kotest:kotest-assertions-core-jvm:${LibVersions.kotest}"
    const val mockk = "io.mockk:mockk:${LibVersions.mockk}"
}

