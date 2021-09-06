// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
        classpath("de.mannodermaus.gradle.plugins:android-junit5:1.7.1.1")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}