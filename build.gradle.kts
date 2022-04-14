buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        google()
    }
    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath(AndroidX.navigation.safeArgsGradlePlugin)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
        classpath("org.jetbrains.compose:compose-gradle-plugin:_")
        classpath(Android.tools.build.gradlePlugin)
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        google()
    }
}
