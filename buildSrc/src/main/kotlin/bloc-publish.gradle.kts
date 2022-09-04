// publish release version locally:
// ./gradlew -Dorg.gradle.parallel=false publish -PPUBLISH_AS_SNAPSHOT=false

plugins {
    `maven-publish`
    signing
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun Project.get(name: String, def: String = "$name not found") =
    properties[name]?.toString() ?: System.getenv(name) ?: def

val isSnapshot = get("PUBLISH_AS_SNAPSHOT", "true").toBoolean()

fun String.version(snapshot: Boolean): String {
    val regex = "-SNAPSHOT$".toRegex()
    val version = toUpperCase().replace(regex, "")
    return if (snapshot) "$version-SNAPSHOT" else version
}

fun Project.getRepositoryUrl(): java.net.URI {
    val releaseRepoUrl = get("RELEASE_REPOSITORY_URL", "https://oss.sonatype.org/service/local/staging/deploy/maven2/")
    val snapshotRepoUrl = get("SNAPSHOT_REPOSITORY_URL", "https://oss.sonatype.org/content/repositories/snapshots/")
    return uri(if (isSnapshot) snapshotRepoUrl else releaseRepoUrl)
}

publishing {
    // 1. configure repositories
    repositories {
        maven {
            url = getRepositoryUrl()
            // credentials are stored in ~/.gradle/gradle.properties with ~ being the path of the home directory
            credentials {
                username = project.get("SONATYPE_NEXUS_USERNAME")
                password = project.get("SONATYPE_NEXUS_PASSWORD")
            }
        }
    }

    // 2. Configure publications
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())

        pom {
            groupId = project.get("POM_GROUP")
            artifactId = project.get("POM_ARTIFACT_ID", artifactId)
            version = project.get("POM_VERSION_NAME").version(isSnapshot)

            name.set(project.name)
            description.set(project.get("POM_DESCRIPTION"))
            url.set(project.get("POM_URL"))

            scm {
                url.set(project.get("POM_SCM_URL"))
                connection.set(project.get("POM_SCM_CONNECTION"))
                developerConnection.set(project.get("POM_SCM_DEV_CONNECTION"))
            }

            developers {
                developer {
                    id.set(project.get("POM_DEVELOPER_ID"))
                    name.set(project.get("POM_DEVELOPER_NAME"))
                    email.set(project.get("POM_DEVELOPER_EMAIL"))
                }
            }

            licenses {
                license {
                    name.set(project.get("POM_LICENCE_NAME"))
                    url.set(project.get("POM_LICENCE_URL"))
                    distribution.set(project.get("POM_LICENCE_DIST"))
                }
            }
        }

    }
}

// 3. sign the artifacts
signing {
    val signingKey = project.get("GPG_SIGNING_KEY")
    val signingPassword = project.get("GPG_SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
    sign(publishing.publications)
}
