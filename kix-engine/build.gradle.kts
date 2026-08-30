plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "org.catrobat.catroid.kix"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    sourceSets {
        getByName("main") {
            // Sources live at repository root (shared layout)
            java.srcDirs("src/main/kotlin", "../src/main/kotlin")
            res.srcDirs("src/main/res", "../res")
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("androidx.annotation:annotation:1.7.1")

    // Integration into NewCatroid/Catroid:
    // 1. Comment out or delete the stubs under src/main/kotlin/org/catrobat/catroid/content
    // 2. Uncomment:
    //    compileOnly(project(":catroid"))
}
