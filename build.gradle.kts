plugins {
    id("fabric-loom") version "0.10-SNAPSHOT"
}

val archiveBaseName = property("archives_base_name")!!
group = property("maven_group")!!
version = property("mod_version")!!

val mcVersion = property("minecraft_version")!!
val yarnVersion = property("yarn_mappings")!!
val loaderVersion = property("loader_version")!!
val apiVersion = property("fabric_version")!!

dependencies {
    minecraft("com.mojang:minecraft:${mcVersion}")
    mappings("net.fabricmc:yarn:${yarnVersion}:v2")
    modImplementation("net.fabricmc:fabric-loader:${loaderVersion}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${apiVersion}")
}

tasks {
    withType(ProcessResources::class) {
        inputs.property("version", version)
        filesMatching("fabric.mod.json") {
            expand("version" to version)
        }
    }

    withType(Jar::class) {
        from("LICENSE") {
            rename { "${it}_${archiveBaseName}" }
        }
    }

    withType(JavaCompile::class) {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
}

java {
    withSourcesJar()
}
