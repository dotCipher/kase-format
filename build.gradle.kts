import groovy.lang.Closure

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("multiplatform") version "1.3.50"
    id("com.palantir.git-version") version "0.12.0-rc2"
}

repositories {
    mavenCentral()
}

group = "io.dotcipher.kase"
// Use explicit cast for groovy call (see https://github.com/palantir/gradle-git-version/issues/105)
version = (extensions.extraProperties.get("gitVersion") as? Closure<*>)?.call() ?: "dirty"

//apply plugin: 'maven-publish'


kotlin {
    jvm()
    js {
        browser {
        }
        nodejs {
        }
    }
    macosX64()
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
