plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

version = "0.0.1-SNAPSHOT"

dependencies {
    api(project(":hyper-libs:hyper-annotations"))
    api(project(":hyper-libs:hyper-commons"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val utf8 = "UTF-8"
tasks.withType<JavaCompile> {
    options.encoding = utf8
}

tasks.withType<Javadoc> {
    options {
        encoding(utf8)
        charset(utf8)
    }
    source(sourceSets.main.get().allJava)

    isFailOnError = false
}

tasks.create<Jar>("sourcesJar") {
    dependsOn(tasks.classes)
    charset(utf8)
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

tasks.create<Jar>("javadocJar") {
    dependsOn(tasks.javadoc)
    charset(utf8)
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

publishing {
    publications {
        create<MavenPublication>("Java") {
            from(components["java"])

            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
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
