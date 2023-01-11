plugins {
    id("java")
    id("java-library")
    id("maven-publish")
}

version = "0.0.2-SNAPSHOT"

dependencies {
    // spring
    api("org.springframework:spring-core:6.0.3")

    // jackson
    api("com.fasterxml.jackson.core:jackson-databind:2.14.1")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.14.1")
    api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.14.1")
    api("com.fasterxml.jackson.module:jackson-module-parameter-names:2.14.1")

    // snakeyaml
    api("org.yaml:snakeyaml:1.33")

    // maven resolver
    api("org.apache.maven.resolver:maven-resolver-api:1.9.2")
    api("org.apache.maven.resolver:maven-resolver-impl:1.9.2")
    api("org.apache.maven:maven-resolver-provider:3.8.6")
    api("org.apache.maven.resolver:maven-resolver-transport-http:1.9.2")
    api("org.apache.maven.resolver:maven-resolver-transport-file:1.9.2")
    api("org.apache.maven.resolver:maven-resolver-connector-basic:1.9.2")

    // log
    implementation("ch.qos.logback:logback-core:1.4.5")
    implementation("ch.qos.logback:logback-classic:1.4.5")
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
