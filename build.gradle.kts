@file:Suppress("ktPropBy")

import com.modrinth.minotaur.TaskModrinthUpload
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import net.neoforged.gradle.common.tasks.PotentiallySignJar

class ModDependency(
    val modId: String,
    val required: Boolean = true,
    val side: String = "BOTH", // "CLIENT", "SERVER", "BOTH"
    val order: String = "BEFORE", // "AFTER"
    val type: String = "modrinth", // "curseforge"
    val projectId: String = modId,
    val fileId: String = ""
) {
    fun getDependency(): String {
        if (type == "curseforge") {
            return "curse.maven:${projectId}:${fileId}"
        }

        return "modrinth:${projectId}"
    }
}

val minecraftVersion: String by project
val modVersion: String by project
val modGroupId: String by project
val modId: String by project
val modName: String by project
val modAuthors: String by project
val modDescription: String by project
val modLicense: String by project
val loaderVersionRange: String by project
val minecraftVersionRange: String by project
val neoVersion: String by project
val neoVersionRange: String by project
val kffVersion: String by project
val jeiVersion: String by project
val jdkVersion: String by project
val adornedFileId: String by project

val dependendMods: List<ModDependency> = listOf(
    // We are using Adorned but the mod id is still curious
    ModDependency(
        "curious", false,
        type = "curseforge",
        projectId = "1036809",
        fileId = adornedFileId
    )
)

plugins {
    `java-library`
    eclipse
    idea
    `maven-publish`
    id("net.neoforged.gradle.userdev") version "7.0.162"
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.7"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("com.modrinth.minotaur") version "2.+"
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("org.jetbrains.dokka") version "1.9.20"
}

version = "${minecraftVersion}-${modVersion}"
group = modGroupId

repositories {
    mavenLocal()
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
        content { includeGroup("thedarkcolour") }
    }
    maven {
        name = "Curse maven"
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        // location of the maven that hosts JEI files since January 2023
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        // location of a maven mirror for JEI files, as a fallback
        name = "ModMaven"
        url = uri("https://modmaven.dev")
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    mavenCentral()
}

base {
    archivesName.set(modId)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jdkVersion))
    }
}

kotlin {
    jvmToolchain(jdkVersion.toInt())
}

runs {
    configureEach {
        systemProperty("forge.logging.markers", "REGISTRIES,SCAN,CLASSLOADING")
        systemProperty("neoforge.logging.console.level", "warn")
        modSource(project.sourceSets.main.get())
        devLogin {
            enabled(false)
        }
    }

    create("client") {
        systemProperty("neoforge.enabledGameTestNamespaces", modId)
        devLogin {
            enabled(true)
        }
    }

    create("server") {
        systemProperty("neoforge.enabledGameTestNamespaces", modId)
        //programArgument("--nogui")
        argument("--nogui")
    }

    create("gameTestServer") {
        systemProperty("neoforge.enabledGameTestNamespaces", modId)
    }

    create("data") {
        arguments.addAll(
            "--mod", modId,
            "--all",
            "--output", file("src/generated/resources/").absolutePath,
            "--existing", file("src/main/resources/").absolutePath
        )
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
        }
    }
}

dependencies {
    detektPlugins("com.braisgabin.detekt:kotlin-compiler-wrapper:0.0.4")
    detektPlugins("com.faire:faire-detekt-rules:0.4.0")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:1.23.7")

    implementation("net.neoforged:neoforge:${neoVersion}")
    implementation("thedarkcolour:kotlinforforge-neoforge:${kffVersion}")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4") { version { strictly("5.0.4") } }

    // JEI mod for testing the client
    compileOnly("mezz.jei:jei-${minecraftVersion}-common-api:${jeiVersion}")
    compileOnly("mezz.jei:jei-${minecraftVersion}-neoforge-api:${jeiVersion}")
    runtimeOnly("mezz.jei:jei-${minecraftVersion}-neoforge:${jeiVersion}")

    // Adorned mod, a replacement for Curious
    runtimeOnly("curse.maven:adorned-1036809:${adornedFileId}")
    compileOnly("curse.maven:adorned-1036809:${adornedFileId}")
}

tasks.withType<ProcessResources>().configureEach {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraftVersion,
        "minecraft_version_range" to minecraftVersionRange,
        "neo_version" to neoVersion,
        "neo_version_range" to neoVersionRange,
        "loader_version_range" to loaderVersionRange,
        "mod_id" to modId,
        "mod_name" to modName,
        "mod_license" to modLicense,
        "mod_version" to modVersion,
        "mod_authors" to modAuthors,
        "mod_description" to modDescription
    )
    inputs.properties(replaceProperties)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(replaceProperties)
        filter { line ->
            var newLine = line
            if (line.contains("description = '''${modDescription}'''")) {
                val additionalContent = dependendMods.joinToString("\n") { dependendMod ->
                    val type = if (dependendMod.required) "required" else "optional"
                    """
                    [[dependencies."$modId"]]
                    modId = "${dependendMod.modId}"
                    type = "$type"
                    versionRange = "$minecraftVersionRange"
                    ordering = "${dependendMod.order}"
                    side = "${dependendMod.side}"
                    """.trimIndent()
                }
                newLine = "$line\n$additionalContent"
            }
            newLine
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

tasks.configureEach {
    if (name.startsWith("testJunit")) {
        enabled = false
    }
}

tasks {
    register<PotentiallySignJar>("signJar") {
        dependsOn(jar)
        onlyIf {
            hasProperty("keyStore") || System.getenv("KEYSTORE") != null
        }
        /*
        keyStore = findProperty("keyStore") ?: System.getenv("KEYSTORE")
        alias = findProperty("keyStoreAlias") ?: System.getenv("KEYSTORE_ALIAS")
        storePass = findProperty("keyStorePass") ?: System.getenv("KEYSTORE_PASS")
        */
        input.set(jar.get().archiveFile)
    }
    named<Jar>("jar") {
        dependsOn(withType<Detekt>())
        finalizedBy(named<PotentiallySignJar>("signJar"))
    }
    withType<TaskModrinthUpload> {
        dependsOn(named<PotentiallySignJar>("signJar"))
        dependsOn(modrinthSyncBody)
    }

    withType<Detekt>().configureEach {
        reports {
            html.required.set(true) // observe findings in your browser with structure and code snippets
            xml.required.set(true) // checkstyle like format mainly for integrations like Jenkins
            sarif.required.set(true) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
            md.required.set(true) // simple Markdown format
        }
    }

    withType<Detekt>().configureEach {
        jvmTarget = jdkVersion
    }
    withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = jdkVersion
    }
}

modrinth {
    token.set(env.fetch("MODRINTH_TOKEN"))
    projectId.set("omni-craft")
    versionType.set("alpha")
    uploadFile.set(tasks.jar.get().archiveFile)
    changelog.set(rootProject.file("CHANGELOG.md").readText())
    syncBodyFrom.set(rootProject.file("README.md").readText())
    dependencies {
        required.project("neoforge")
        dependendMods.forEach { dependendMod ->
            if (dependendMod.required) {
                required.project(dependendMod.modId)
            } else {
                optional.project(dependendMod.modId)
            }
        }
    }
}

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    ignoreFailures = false
    //baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}