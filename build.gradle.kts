group = "pres.ketikai.hyper"

subprojects {
    group = rootProject.group

    repositories {
        mavenLocal()
        maven {
            name = "aliyun-public"
            url = uri("https://maven.aliyun.com/repository/public")
        }
        val propsFile = File(rootDir, "gradle.properties")
        if (!propsFile.exists()) {
            throw java.io.FileNotFoundException(propsFile.absolutePath)
        }
        val props = java.util.Properties()
        var fis: java.io.FileInputStream? = null
        try {
            fis = java.io.FileInputStream(propsFile)
            props.load(fis)
        } finally {
            fis?.close()
        }
        maven {
            name = "ketikai-aliyun-release"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-release-1uE4Wz")
            credentials {
                username = props.getProperty("aliyun.ketikai.repo.username")!!
                password = props.getProperty("aliyun.ketikai.repo.password")!!
            }
        }
        maven {
            name = "ketikai-aliyun-snapshot"
            url = uri("https://packages.aliyun.com/maven/repository/2316884-snapshot-OMR1CL")
            credentials {
                username = props.getProperty("aliyun.ketikai.repo.username")!!
                password = props.getProperty("aliyun.ketikai.repo.password")!!
            }
        }
        mavenCentral()
    }
}
