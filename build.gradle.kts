plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// ====== اینجا فقط این سه تا رو عوض کن ======
val clientPackage = "com.sampunity.game"       // SAMP Launcher Unity (OSC. Games)
val serverIp = "195.18.10.168"
val serverPort = "7777"
// ===========================================

android {
    namespace = "com.zentrarpg.launcher"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.zentrarpg.launcher"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "CLIENT_PACKAGE", "\"$clientPackage\"")
        buildConfigField("String", "SERVER_IP", "\"$serverIp\"")
        buildConfigField("String", "SERVER_PORT", "\"$serverPort\"")
        manifestPlaceholders["clientPackage"] = clientPackage
    }
    buildFeatures { buildConfig = true }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
}
