import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    id("org.jetbrains.compose") version "1.6.0"
}

group = "com.kevinchrist20"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)

    // Decompose
//    val decomposeVersion = "0.6.0"
//    implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
//    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")

    val voyagerVersion = "1.0.0"

    implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-screenmodel:$voyagerVersion")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

//    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
//    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "DevUtils"
            packageVersion = "1.0.0"
        }
    }
}