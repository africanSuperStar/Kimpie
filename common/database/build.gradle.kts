plugins {
    id("multiplatform-setup")
    id("android-setup")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("AstrocyteDatabase") {
        packageName = "healthcare.astrocyte.database"
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(Deps.Badoo.Reaktive.reaktive)
            }
        }

        androidMain {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.androidDriver)
                implementation(Deps.Squareup.SQLDelight.sqliteDriver)
            }
        }

        desktopMain {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.sqliteDriver)
            }
        }

        iosMain {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.nativeDriver)
            }
        }

        jsMain {
            dependencies {
                implementation(Deps.Squareup.SQLDelight.sqljsDriver)
            }
        }
    }
}
