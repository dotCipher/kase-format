plugins {
    kotlin("multiplatform") version "1.3.50"
}

repositories {
    mavenCentral()
}

group = "io.dotcipher.kase"
//version '0.0.1'

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
    iosArm32()
    iosArm64()
    linuxX64()
    mingwX64()
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
