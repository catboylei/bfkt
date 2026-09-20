// NOTE: Minimal file for the lsp to recognise imports
// This project is only tested with kotlin-toolchain, NOT gradle 

plugins {
    kotlin("jvm") version "2.4.0" // jvm build because the lsp only works for jvm ??
}

repositories {
    mavenCentral()
}

val moduleYaml = providers
    .fileContents(layout.projectDirectory.file("module.yaml"))
    .asText.get()

// Pulls deps from module.yaml
val yamlDependencies: List<String> = run {
    val coordinate = Regex("""^\s*-\s+([^\s:]+:[^\s:]+:[^\s:]+)""")
    moduleYaml.lineSequence()
        .dropWhile { it.trim() != "dependencies:" }
        .drop(1)
        .takeWhile { line ->
            line.isBlank() ||
                line.trimStart().startsWith("#") ||
                line.first().isWhitespace() ||
                line.startsWith("-")
        }
        .mapNotNull { coordinate.find(it)?.groupValues?.get(1) }
        .toList()
}

dependencies {
	yamlDependencies.forEach { implementation(it) }
}

kotlin {
    sourceSets["main"].kotlin.srcDir("src")
}
