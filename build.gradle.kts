buildscript {
    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            name = "ketikai-aliyun-release"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-release-1uE4Wz")
            credentials {
                username = "63a6e5de3615b942064ae00a"
                password = "9]NhW119yofE"
            }
        }
        maven {
            name = "ketikai-aliyun-snapshot"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-snapshot-OMR1CL")
            credentials {
                username = "63a6e5de3615b942064ae00a"
                password = "9]NhW119yofE"
            }
        }
        mavenCentral()
    }

    dependencies {
        classpath("pres.ketikai.hyper:hyper-gradle-util:1.0.6-SNAPSHOT")
    }
}

group = "pres.ketikai.hyper"

subprojects {

    apply {
        if (project.name != "hyper-gradle-util") {
            try {
                plugin("hyper-gradle-util")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    group = rootProject.group

    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        maven {
            name = "ketikai-aliyun-release"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-release-1uE4Wz")
            credentials {
                username = "63a6e5de3615b942064ae00a"
                password = "9]NhW119yofE"
            }
        }
        maven {
            name = "ketikai-aliyun-snapshot"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-snapshot-OMR1CL")
            credentials {
                username = "63a6e5de3615b942064ae00a"
                password = "9]NhW119yofE"
            }
        }
        mavenCentral()
    }
}

tasks.create<Delete>("cleanBuild") {
    setDelete(file("$rootDir/build"))
}
