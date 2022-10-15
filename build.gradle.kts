import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import io.gitlab.arturbosch.detekt.Detekt

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(Android.tools.build.gradlePlugin)
        classpath(AndroidX.navigation.safeArgsGradlePlugin)
        classpath(libs.kotlin.gradle.plugin)
        classpath(libs.compose.gradle.plugin)
        classpath(Square.sqlDelight.gradlePlugin)
    }
}

// run ./gradlew dokkaHtmlMultiModule to create the documentation
// then deploy the website (cd website, yarn deploy)
plugins {
    id("org.jetbrains.dokka")
    id("io.gitlab.arturbosch.detekt")
    id("com.louiscad.complete-kotlin")
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("../website/static/dokka"))
}

detekt {
    config = files("${rootProject.projectDir}/config/detekt/detekt.yml")
    // activate all available (even unstable) rules.
    source = files("${rootProject.projectDir}")
    allRules = false
    ignoreFailures = false
}

tasks.withType<Detekt>().configureEach {
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
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
