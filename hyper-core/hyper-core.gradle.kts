import pres.ketikai.hyper.gradle.plugin.hyper
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
        classpath("pres.ketikai.hyper:hyper-gradle-plugin:1.0.8-SNAPSHOT")
    }
}

apply { plugin("hyper-gradle-plugin") }

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = sourceCompatibility
}

repositories {
    maven {
        name = "spigotmc-snapshot"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    }
}

dependencies {
//    // spigot-mc
//    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    // bukkit
    compileOnly(fileTree("D:/JetBrains/IDEA Projects/1.18.2/bundler/versions"))
    // bukkit libs
    compileOnly(fileTree("D:/JetBrains/IDEA Projects/1.18.2/bundler/libraries"))

    // hyper-libs
    api(project(":hyper-libs:hyper-impl"))
}

val utf8 = "UTF-8"

tasks.withType<JavaCompile> {
    options.encoding = utf8
}

tasks.hyper {
    plugin {
        libraries {
            filter {
                include { it ->
                    if (it.parents.stream().anyMatch { it.name.startsWith("pres.ketikai.hyper:hyper-") }) {
                        if (it.moduleArtifacts.isNotEmpty() &&
                            it.moduleArtifacts.stream().allMatch {
                                !it.id.componentIdentifier.displayName.startsWith("project :")
                            }
                        ) {
                            return@include true
                        }
                    }
                    return@include false
                }
            }
        }
    }
}

tasks.shadowJar {
    dependsOn(tasks.hyper)

    archiveBaseName.set("Hyper")
    archiveClassifier.set("")

    val propsFile = File(rootDir, "gradle.properties")
    if (!propsFile.exists()) {
        throw FileNotFoundException(propsFile.absolutePath)
    }
    val props = Properties()
    var fis: FileInputStream? = null
    try {
        fis = FileInputStream(propsFile)
        props.load(fis)
    } finally {
        fis?.close()
    }
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
        val agentProject = project(":hyper-agent")
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

        val bos: ByteArrayOutputStream
        var jos: JarOutputStream? = null
        try {
            bos = ByteArrayOutputStream(1024)
            jos = JarOutputStream(bos)
            jos.putNextEntry(JarEntry(destEntryName))
            jos.write(agentFile.readBytes())
            jos.closeEntry()
            destJar.stream().forEach {
                it ?: return@forEach
                if (it.name == destEntryName) {
                    return@forEach
                }
                val inputStream = destJar.getInputStream(it)
                val entryBytes = inputStream.readBytes()
                inputStream.close()
                jos.putNextEntry(it)
                jos.write(entryBytes)
                jos.closeEntry()
            }
        } finally {
            jos?.close()
            destJar.close()
        }

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
