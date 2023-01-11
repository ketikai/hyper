buildscript {
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
}

tasks.create<Delete>("cleanBuild") {
    setDelete(file("$rootDir/build"))
}
