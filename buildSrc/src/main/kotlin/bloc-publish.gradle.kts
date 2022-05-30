plugins {
    `maven-publish`
    signing
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

fun Project.get(name: String, def: String = "$name not found") =
    properties[name]?.toString() ?: System.getenv(name) ?: def

fun Project.getRepositoryUrl(): java.net.URI {
    val isReleaseBuild = !get("POM_VERSION_NAME").contains("SNAPSHOT")
    val releaseRepoUrl = get("RELEASE_REPOSITORY_URL", "https://oss.sonatype.org/service/local/staging/deploy/maven2/")
    val snapshotRepoUrl = get("SNAPSHOT_REPOSITORY_URL", "https://oss.sonatype.org/content/repositories/snapshots/")
    return uri(if (isReleaseBuild) releaseRepoUrl else snapshotRepoUrl)
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

    // 2.
    publications {
        create("mavenKotlin", MavenPublication::class.java) {
            from(components.getByName("kotlin"))
        }
    }

    publications.withType<MavenPublication> {
        pom {
            groupId = project.get("POM_GROUP")
            // the artifactId defaults to the project's/module's name
            version = project.get("POM_VERSION_NAME")

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
    val signingKeyPassword = project.get("GPG_SIGNING_PASSWORD")
    val signingKey = project.get("GPG_SECRET")
    useInMemoryPgpKeys(signingKey, signingKeyPassword)
    sign(publishing.publications)
}
