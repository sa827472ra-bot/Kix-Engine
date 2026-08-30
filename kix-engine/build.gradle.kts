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
            // Production sources live at repo root style paths inside this module
            java.srcDirs("src/main/kotlin", "src/main/java")
            res.srcDirs("src/main/res")
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

    // When integrated into NewCatroid/Catroid, replace stubs by:
    //   compileOnly(project(":catroid"))
    // and remove the stubs source set usage (see README).
}
