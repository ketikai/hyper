plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

version = "1.0.0"

dependencies {
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

val utf8 = "UTF-8"
tasks.withType<JavaCompile> {
    options.encoding = utf8
}

tasks.jar {
    manifest {
        attributes(
            "Agent-Class" to "pres.ketikai.hyper.agent.HyperAgent",
            "Can-Redefine-Classes" to true,
            "Can-Retransform-Classes" to true,
            "Can-Set-Native-Method-Prefix" to true
        )
    }
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
