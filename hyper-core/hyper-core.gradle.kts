import pres.ketikai.hyper.gradle.util.hyperBukkit
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

buildscript {
    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        val uname = "63a6e5de3615b942064ae00a"
        val pwd = "9]NhW119yofE"
        maven {
            name = "ketikai-aliyun-release"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-release-1uE4Wz")
            credentials {
                username = uname
                password = pwd
            }
        }
        maven {
            name = "ketikai-aliyun-snapshot"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-snapshot-OMR1CL")
            credentials {
                username = uname
                password = pwd
            }
        }
        mavenCentral()
    }

    dependencies {
        classpath("pres.ketikai.hyper:hyper-gradle-util:1.0.7")
    }
}

apply { plugin("hyper-gradle-util") }

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
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
    api(project(":hyper-libs:hyper-impl"))
}

val utf8 = "UTF-8"

tasks.withType<JavaCompile> {
    options.encoding = utf8
}

tasks.hyperBukkit {
    pluginYaml {
        main = "pres.ketikai.hyper.core.HyperCore"
        apiVersion = "1.18"
        description = "为基于 Hyper 的 bukkit 插件提供开发简化和功能增强"
        load = "STARTUP"
        authors.add("ketikai")

        libraries {
            filter {
                include { it ->
                    if (it.moduleArtifacts.isNotEmpty()) {
                        it.moduleArtifacts.forEach {
                            if (it.id.componentIdentifier.displayName.startsWith("project :")) {
                                return@include false
                            }
                        }
                    }
                    return@include true
                }
            }
        }
    }
}

tasks.shadowJar {
    dependsOn(tasks.hyperBukkit)

    archiveBaseName.set("Hyper")
    archiveClassifier.set("")

    val propsFile = File(rootDir, "gradle.properties")
    if (!propsFile.exists()) {
        throw FileNotFoundException(propsFile.absolutePath)
    }
    val props = Properties()
    val fis = FileInputStream(propsFile)
    props.load(fis)
    fis.close()
    val destDir = file(props.getProperty("hyper.all-in-one.dest-dir")!!)
    destinationDirectory.set(destDir)

    dependencies {
        include { it ->
            if (it.moduleArtifacts.isNotEmpty()) {
                it.moduleArtifacts.forEach {
                    if (it.id.componentIdentifier.displayName.startsWith("project :")) {
                        return@include true
                    }
                }
            }
            return@include false
        }
    }

    doLast {
        val agentProject = project(":hyper-libs:hyper-agent")
        val agentFile = File(
            agentProject.buildDir,
            "libs/${agentProject.name}-${agentProject.version}.jar"
        )
        if (!agentFile.exists() || agentFile.isDirectory) {
            throw FileNotFoundException()
        }

        val destFile = archiveFile.get().asFile
        val destJar = JarFile(destFile)
        val destEntryName = "resources/modules/${agentFile.name}"

        val bos = ByteArrayOutputStream(1024)
        val jos = JarOutputStream(bos)
        jos.putNextEntry(JarEntry(destEntryName))
        jos.write(agentFile.readBytes())
        jos.closeEntry()
        destJar.stream().forEach {
            it ?: return@forEach
            if (it.realName == destEntryName) {
                return@forEach
            }
            val inputStream = destJar.getInputStream(it)
            val entryBytes = inputStream.readBytes()
            inputStream.close()
            jos.putNextEntry(it)
            jos.write(entryBytes)
            jos.closeEntry()
        }
        jos.close()
        destJar.close()

        destFile.writeBytes(bos.toByteArray())

        println("\nall-in-one-jar build to path: $destFile")
    }
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
