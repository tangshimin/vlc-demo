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

tasks.register<Exec>("createMsi") {
    group = "distribution"
    description = "Creates an MSI package using jpackage and a custom .wix file"

    // Add the WiX toolset to the system path
    environment("{Path}", "${projectDir}/build/wix311:${System.getenv("Path")}")

    // The command to execute
    commandLine("jpackage",
        "--input", "build/compose/jars/",
        "--name", "打包测试",
        "--main-jar", "打包测试-windows-x64-1.0.0.jar",
        "--resource-dir", "src/main/resources",
        "--win-dir-chooser",
        "--type","msi"
    )

    // The working directory for the command
    workingDir(projectDir)

    // Log the output to the console
    doLast {
        println("MSI package created successfully")
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "打包测试"
            modules("java.compiler","java.instrument","java.prefs",  "jdk.unsupported","jdk.accessibility")
            packageVersion = "1.0.0"
        }
    }
}
