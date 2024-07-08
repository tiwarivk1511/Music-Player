import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.compose"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality

    implementation(compose.desktop.currentOs)
    // Database
    implementation("com.squareup.sqldelight:runtime-jvm:1.5.3")
    implementation("com.squareup.sqldelight:sqlite-driver:1.5.3")
    // Audio
    implementation ("com.googlecode.soundlibs:mp3spi:1.9.5-1")
    implementation ("com.googlecode.soundlibs:tritonus-share:0.3.6-2")
    implementation ("com.googlecode.soundlibs:jlayer:1.0.1.4")
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
    implementation("org.jetbrains.compose.desktop:desktop-jvm:1.2.0")
    // Video
    implementation("uk.co.caprica:vlcj:4.7.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MusicPlayer"
            packageVersion = "1.0.0"

            macOS{
                iconFile.set(project.file("resources/drawable/ic_album.png"))
            }

            windows{
                iconFile.set(project.file("resources/drawable/ic_album.png"))
            }

            linux{
                iconFile.set(project.file("resources/drawable/ic_album.png"))
            }

        }
    }
}
