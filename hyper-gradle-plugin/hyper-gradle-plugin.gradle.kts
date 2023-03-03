plugins {
    kotlin("jvm") version "1.7.22"
    id("java-gradle-plugin")
    id("maven-publish")
}

version = "1.0.8-SNAPSHOT"

dependencies {
    // snakeyaml
    api("org.yaml:snakeyaml:1.33")
}

gradlePlugin {
    plugins {
        create("HyperGradlePlugin") {
            id = "hyper-gradle-plugin"
            implementationClass = "pres.ketikai.hyper.gradle.plugin.HyperGradlePlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
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

tasks.create<Delete>("cleanBuildDir") {
    setDelete(file("$rootDir/build"))
}

tasks.clean {
    dependsOn(tasks.named("cleanBuildDir"))
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
