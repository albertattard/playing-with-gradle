import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    base
}

tasks.register<Copy>("generateDescriptions") {
    group = "Theme park"
    description = "Generates descriptions including token substitution"
    from("descriptions")
    into("$buildDir/descriptions")
    filter(ReplaceTokens::class, "tokens" to mapOf("THEME_PARK_NAME" to "Grelephant's Wonder World"))
}

tasks.register<Zip>("zipDescriptions") {
    group = "Theme park"
    description = "Compress the descriptions into a single ZIP file"
    from("$buildDir/descriptions")
    destinationDirectory.set(buildDir)
    archiveFileName.set("descriptions.zip")
    dependsOn.add("generateDescriptions")
}
