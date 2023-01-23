group = "pres.ketikai.hyper"

subprojects {
    group = rootProject.group

    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        mavenCentral()
    }
}

tasks.create<Delete>("cleanBuild") {
    setDelete(file("$rootDir/build"))
}
