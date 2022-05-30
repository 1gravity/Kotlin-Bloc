import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

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
        classpath(Square.sqlDelight.gradlePlugin)
    }
}

// run ./gradlew dokkaHtmlMultiModule to create the documentation
plugins {
    id("org.jetbrains.dokka")
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("../docs/dokka"))
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
        google()
    }

    afterEvaluate {
        // eliminate log pollution until Android support for KMM improves
        val sets2BeRemoved = setOf(
            "androidAndroidTestRelease",
            "androidTestFixtures",
            "androidTestFixturesDebug",
            "androidTestFixturesRelease"
        )
        project.extensions.findByType<KotlinMultiplatformExtension>()?.let { kmpExt ->
            kmpExt.sourceSets.removeAll { sets2BeRemoved.contains(it.name) }
        }
    }
}
