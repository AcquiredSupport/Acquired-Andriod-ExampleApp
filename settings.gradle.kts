dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/AcquiredSupport/Acquired-Android")
            credentials {
                username = "RonniCellpoint"
                password = "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"
            }
        }
    }
}

include(":exampleapp")