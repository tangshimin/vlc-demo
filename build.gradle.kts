import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
    google()
}
dependencies {
    implementation(compose.desktop.currentOs)


    implementation("uk.co.caprica:vlcj:4.8.2"){
        exclude(group = "net.java.dev.jna", module = "jna")
    }
    implementation("net.java.dev.jna:jna:5.14.0")

}



compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "vlc-player"
            modules("java.compiler","java.instrument","java.prefs",  "jdk.unsupported","jdk.accessibility")
            packageVersion = "1.0.0"
        }
    }
}
