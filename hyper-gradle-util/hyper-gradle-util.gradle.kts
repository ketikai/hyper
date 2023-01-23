plugins {
    kotlin("jvm") version "1.7.22"
    id("java-gradle-plugin")
    id("maven-publish")
}

version = "1.0.6-SNAPSHOT"

dependencies {
    // maven-resolver
    api("org.apache.maven.resolver:maven-resolver-api:1.9.4")

    // snakeyaml
    api("org.yaml:snakeyaml:1.33")

    // asm
    api("org.ow2.asm:asm-commons:9.4")
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

tasks.create<Jar>("sourcesJar") {
    dependsOn(tasks.classes)
    charset(utf8)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
        create<MavenPublication>("Java") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
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
