import groovy.lang.Closure
import org.gradle.api.publish.maven.MavenPom

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("multiplatform") version "1.4.10"
    id("com.palantir.git-version") version "0.12.0-rc2"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
}

repositories {
    mavenCentral()
}

val artifactId = "kase-format"
group = "io.dotcipher.kase"
// Use explicit cast for groovy call (see https://github.com/palantir/gradle-git-version/issues/105)
version = (extensions.extraProperties.get("gitVersion") as? Closure<*>)?.call() ?: "dirty"
description = "Multiplatform kotlin string case conversion and detection library"

// Kotlin multiplatform configuration
kotlin {
    jvm()
    // TODO
//    js {
//        browser {
//        }
//        nodejs {
//        }
//    }
//    macosX64()
    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
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
        url.set("https://github.com/dotCipher/kase-format")
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
            connection.set("scm:git:git://github.com/dotcipher/kase-format.git")
            developerConnection.set("scm:git:ssh://github.com/dotcipher/kase-format.git")
            url.set("https://github.com/dotCipher/kase-format")
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

afterEvaluate {
    project.publishing.publications.all {
        if (this is MavenPublication) {
            if (this.name.contains("metadata")) {
                this.artifactId = artifactId
            }
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
        setPublications("maven", "jvm", "metadata")
        vcsUrl = "https://github.com/dotCipher/kase-format"
        issueTrackerUrl = "https://github.com/dotCipher/kase-format/issues"
        githubRepo = "dotCipher/kase-format"
        setLabels("kotlin", "library", "multiplatform")
        publicDownloadNumbers = true
        with(version) {
            name = project.version.toString()
            desc = project.description
            vcsTag = project.version.toString()
        }
    }
    publish = true
    override = true
}

tasks.bintrayUpload.configure {
    dependsOn(tasks.publishToMavenLocal)
}
