pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "Astrocyte"
include(":android")
include(":shared")
include(":compose-desktop")