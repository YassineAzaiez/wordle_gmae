
buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath (BuildPlugins.gradle)
        classpath (BuildPlugins.kotlin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
plugins {

    id("com.google.dagger.hilt.android") version "2.44" apply false

}

allprojects {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}


tasks.register("clean", Delete::class){
    delete(rootProject.buildDir)
}