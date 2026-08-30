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
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
            java.srcDirs("src/main/kotlin", "../src/main/kotlin")
            res.srcDirs("src/main/res", "../res")
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
        getByName("test") {
            java.srcDirs("src/test/java")
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = false
        unitTests.isReturnDefaultValues = true
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

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.9.22")

    // Integration into NewCatroid:
    // compileOnly(project(":catroid"))
}
