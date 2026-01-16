import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.2.0"
}

val v = "0.9.95"
group = "com.glycin"
version = v

repositories {
    mavenCentral()

    intellijPlatform {
        defaultRepositories()
    }
}

intellijPlatform  {
    pluginConfiguration {
        id = "intelli25"
        name = "IDE Survivors"
        version = v

        ideaVersion {
            sinceBuild = "232"
            untilBuild = provider { null }
        }

        vendor {
            name = "Glycin"
            url = "https://github.com/glycin"
        }

        description = """
            Hey there, human. I’m Runzo. Yes, your IDE’s Run button.<br>
            I’ve been living inside IntelliJ IDEA for 25 years, watching devs build incredible things and occasionally rage-clicking me when their code won't compile.<br>
            My mission? Make your ideas real, or to put it simply – run your code.<br>
            This game lets you follow my journey, all 25 years of it – the upgrades, the chaos, the bugs, the breakthroughs.<br><br>
            Stick around, help me survive it all… and maybe, just maybe, give me a chance to escape for a birthday party.<br>
        """.trimIndent()
    }

    publishing {}

    signing{}
}

dependencies {
    intellijPlatform{
        intellijIdeaCommunity("2024.2.3")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

kotlin{
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}