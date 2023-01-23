import pres.ketikai.hyper.gradle.util.hyperBukkit

buildscript {
    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }

    dependencies {
        classpath("pres.ketikai.hyper:hyper-gradle-util:1.0.6-SNAPSHOT")
    }
}

apply {
    if (project.name != "hyper-gradle-util") {
        try {
            plugin("hyper-gradle-util")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    maven {
        name = "spigotmc-snapshot"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    }
}

dependencies {
    // spigot-mc
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")

    // hyper-libs
    api(project(":hyper-libs:hyper-commons"))
    api(project(":hyper-libs:hyper-annotations"))
    api(project(":hyper-libs:hyper-commands"))
    api(project(":hyper-libs:hyper-events-listeners"))
    api(project(":hyper-libs:hyper-resources"))
    api(project(":hyper-libs:hyper-stores"))
    api(project(":hyper-libs:hyper-tasks-executors"))

    // security
    implementation("org.springframework.security:spring-security-crypto:6.0.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")

    // dev
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor:3.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.1")
}

val utf8 = "UTF-8"

tasks.withType<JavaCompile> {
    options.encoding = utf8
}

tasks.hyperBukkit {
    pluginYaml {
        main.set("pres.ketikai.hyper.manager.HyperManager")
        apiVersion.set("1.18")
        description.set("提供基于 Hyper 的插件增强管理")
        authors.add("ketikai")
        load.set("STARTUP")
    }
}

tasks.jar {
    dependsOn(tasks.hyperBukkit)
    val targetDir = file("D:/JetBrains/IDEA Projects/1.18.2/plugins")
    val targetFile = File(targetDir, "${project.name}-${project.version}.jar")
    if (targetFile.exists()) {
        targetFile.delete()
    }
    destinationDirectory.set(targetDir)
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

tasks.withType<Test> {
    useJUnitPlatform()
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
