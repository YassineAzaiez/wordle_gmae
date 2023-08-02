
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}
@Suppress("UnstableApiUsage")
android {
    compileSdk =ConfigData.compileSdkVersion
    buildToolsVersion = ConfigData.buildToolsVersion



    defaultConfig {
        applicationId = ConfigData.appId
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName
        resValue( "string", "app_name", rootProject.name)
        buildConfigField("String","BASE_URL","\"https://random-word-api.herokuapp.com\"")
    }

    buildTypes {

        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }

}

dependencies {


    implementation(Deps.kotlin)
    implementation(Deps.appCompat)
    implementation(Deps.materialDesign)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    testImplementation(project(":app"))
    implementation(Deps.timber)
    implementation(Deps.constraintLayout)
    implementation(Deps.coroutines)

    // testing dependencies
    testImplementation(Deps.coroutinesTest)
    androidTestImplementation(Deps.coroutinesTest)
    testImplementation(Deps.mockServer)
    testImplementation(Deps.truth)
    testImplementation(Deps.junit)
    kaptTest(Deps.hiltTest)
    androidTestImplementation(Deps.junitInst)
    androidTestImplementation(Deps.espresso)
    androidTestImplementation(Deps.truth)
    kaptAndroidTest(Deps.hiltTest)
    testImplementation(Deps.hiltTest)
    testImplementation(Deps.mockk)
    //hilt for DI
    implementation(Deps.hilt)
    kapt(Deps.hiltCompiler)

  //Retrofit
    implementation(Deps.retrofit)
    implementation(Deps.gson)
    implementation(Deps.logginfInterceptor)

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
}
kapt {
    correctErrorTypes = true
}