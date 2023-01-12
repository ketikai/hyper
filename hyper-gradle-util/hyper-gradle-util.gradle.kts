plugins {
    kotlin("jvm") version "1.7.22"
    id("org.jetbrains.dokka") version "1.7.20"
    id("java-gradle-plugin")
    id("maven-publish")
}

version = "1.0.6-SNAPSHOT"

dependencies {
    api(project(":hyper-libs:hyper-commons"))
}

gradlePlugin {
    plugins {
        create("HyperGradleUtil") {
            id = "hyper-gradle-util"
            implementationClass = "pres.ketikai.hyper.gradle.util.HyperGradleUtil"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val utf8 = "UTF-8"

tasks.withType<JavaCompile> {
    options.encoding = utf8
}

tasks.dokkaHtml {
    charset(utf8)
}

tasks.create<Jar>("sourcesJar") {
    dependsOn(tasks.classes)
    charset(utf8)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.create<Jar>("dokkadocJar") {
    dependsOn(tasks.dokkaHtml)
    charset(utf8)
    archiveClassifier.set("dokkadoc")
    from(tasks.dokkaHtml.get().outputDirectory.get().absoluteFile)
}

publishing {
    publications {
        create<MavenPublication>("Java") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["dokkadocJar"])
        }
    }

    repositories {
        maven {
            name = "MyAliyun"
            val releasesRepoUrl = "https://packages.aliyun.com/maven/repository/2316884-release-1uE4Wz"
            val snapshotsRepoUrl = "https://packages.aliyun.com/maven/repository/2316884-snapshot-OMR1CL"
            url = uri(if (version.toString().endsWith("SNAPSHOT", true)) snapshotsRepoUrl else releasesRepoUrl)
            credentials {
                username = property("pres.ketikai.maven.aliyun.username").toString()
                password = property("pres.ketikai.maven.aliyun.password").toString()
            }
        }
    }
}
