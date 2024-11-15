import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization")
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()

if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}


kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }


    
    sourceSets {

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            // Koin
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            // MediaPlayer
            implementation(libs.androidx.media)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            // Koin
            api(libs.koin.core)
            implementation(libs.koin.compose)
            // ViewModel LifeCycle
            implementation(libs.lifecycle.viewmodel)
            // Navigation Compose
            implementation(libs.navigation.compose)
            // Serialization
            implementation(libs.kotlinx.serialization.json)
            // Room
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)

        }
        val iosMain by getting {
            dependsOn(commonMain.get())
        }

    }
}

android {
    namespace = "org.ivancaez.cooltimer"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.ivancaez.cooltimer"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "2.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties["KEYSTORE_FILE"] ?: System.getenv("KEYSTORE_FILE") ?: "$rootDir/keystore.jks" )
            storePassword = (localProperties["KEYSTORE_PASSWORD"]  ?: System.getenv("KEYSTORE_PASSWORD")).toString()
            keyAlias = (localProperties["KEY_ALIAS"] ?: System.getenv("KEYSTORE_PASSWORD")).toString()
            keyPassword = (localProperties["KEY_PASSWORD"]  ?: System.getenv("KEYSTORE_PASSWORD")).toString()
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.navigation.common.ktx)
    ksp(libs.room.compiler)
}
