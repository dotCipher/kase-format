import groovy.lang.Closure
import org.gradle.api.publish.maven.MavenPom

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("multiplatform") version "1.3.50"
    id("com.palantir.git-version") version "0.12.0-rc2"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

repositories {
    mavenCentral()
}

group = "io.dotcipher.kase"
// Use explicit cast for groovy call (see https://github.com/palantir/gradle-git-version/issues/105)
version = (extensions.extraProperties.get("gitVersion") as? Closure<*>)?.call() ?: "dirty"
description = "Multiplatform kotlin string case detection, conversion and formatting library"

// Kotlin multiplatform configuration
kotlin {
    jvm()
    // TODO
    js {
        browser {
        }
        nodejs {
        }
    }
//    macosX64()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        // Delegate to JVM target for tests
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
    }
}

//tasks.create("show") {
//    doLast {
//        components.forEach {
//            it as KotlinSoftwareComponentWithCoordinatesAndPublication
//            it.variants.forEach { variant ->
//                variant as AbstractKotlinTarget
//                variant.
//                println(variant)
//            }
//        }
//    }
//}

val stubSources = tasks.create("stubSources", Jar::class) {
    archiveClassifier.set("sources")
}

val stubJavadoc = tasks.create("stubJavadoc", Jar::class) {
    archiveClassifier.set("javadoc")
}

val sourcesJar = tasks.create("sourcesJar", Jar::class) {
    archiveClassifier.set("sources")
    from(kotlin.sourceSets.commonMain.get().kotlin)
}

tasks.withType<Jar> {
    manifest.attributes.apply {
        put("Implementation-Title", project.name)
        put("Implementation-Version", project.version)
    }
}

val mavenPomConfiguration: ((MavenPom).() -> Unit) =
    {
        url.set("https://github.com/dotCipher/${project.name}")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("dotCipher")
                name.set("Cody Moore")
                email.set("cody at dotcipher.io")
            }
        }
        scm {
            connection.set("scm:git:git://github.com/dotcipher/${project.name}.git")
            developerConnection.set("scm:git:ssh://github.com/dotcipher/${project.name}.git")
            url.set("https://github.com/dotCipher/${project.name}")
        }
    }


// Publication distribution configuration
publishing {
    publications {
        getByName<MavenPublication>("jvm") {
            pom { mavenPomConfiguration.invoke(this) }
        }
        create("maven", MavenPublication::class) {
            artifactId = project.name
            from(components["kotlin"])
            artifact(sourcesJar)
            pom { mavenPomConfiguration.invoke(this) }
        }
    }
}

// Bintray upload configuration
bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    with(pkg) {
        userOrg = "dotcipher"
        repo = "maven"
        name = project.name
        setLicenses("Apache-2.0")
        setPublications("maven", "jvm")
        vcsUrl = "https://github.com/dotCipher/${project.name}"
        issueTrackerUrl = "https://github.com/dotCipher/${project.name}/issues"
        githubRepo = "dotcipher/${project.name}"
        setLabels("kotlin", "library", "multiplatform")
        publicDownloadNumbers = true
        with(version) {
            name = project.version.toString()
            desc = "${project.description} ${project.version}"
            vcsTag = project.version.toString()
        }
    }
    publish = true
    override = true
}

tasks.bintrayUpload.configure {
    dependsOn(tasks.publishToMavenLocal)
}
