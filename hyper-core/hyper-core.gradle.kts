import pres.ketikai.hyper.gradle.util.bukkit.boot.BukkitLoadType
import pres.ketikai.hyper.gradle.util.dependencyReport
import pres.ketikai.hyper.gradle.util.hyperBukkit

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
    api(project(":hyper-libs:hyper-annotations"))
    api(project(":hyper-libs:hyper-commands"))
    api(project(":hyper-libs:hyper-commons"))
    api(project(":hyper-libs:hyper-events-listeners"))
    api(project(":hyper-libs:hyper-resources"))
    api(project(":hyper-libs:hyper-stores"))
    api(project(":hyper-libs:hyper-tasks-executors"))

    // aop
    implementation("org.springframework:spring-aop:6.0.3")

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
    dependsOn(tasks.classes)
    enabled = true
    hyper.set(true)
    main.set("pres.ketikai.hyper.manager.HyperManager")
    apiVersion.set("1.18")
    description.set("提供基于 Hyper 的插件增强管理")
    authors.add("ketikai")
    load.set(BukkitLoadType.STARTUP)
}

tasks.dependencyReport {
    dependsOn(tasks.classes)
}

tasks.jar {
    dependsOn(tasks.hyperBukkit, tasks.dependencyReport)
    destinationDirectory.set(file("D:/JetBrains/IDEA Projects/1.18.2/plugins"))
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

tasks.withType<Test> {
    useJUnitPlatform()
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
