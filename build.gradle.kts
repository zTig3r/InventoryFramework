plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "9.0.0"
    id ("xyz.jpenilla.run-paper") version "2.3.1"
    id ("com.vanniktech.maven.publish") version "0.35.0"
}

group = "io.github.ztig3r"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.10")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<Javadoc> {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
}

mavenPublishing {
    coordinates(group.toString(), name, version.toString())

    pom {
        name.set("Inventory Framework")
        description.set("A simple inventory framework for Minecraft plugins.")
        inceptionYear.set("2025")
        url.set("https://github.com/zTig3r/InventoryFramework/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("ztiger")
                name.set("zTig3r")
                url.set("https://github.com/zTig3r/")
            }
        }
        scm {
            url.set("https://github.com/zTig3r/InventoryFramework/")
            connection.set("scm:git:git://github.com/zTig3r/InventoryFramework.git")
            developerConnection.set("scm:git:ssh://git@github.com/zTig3r/InventoryFramework.git")
        }
    }
}