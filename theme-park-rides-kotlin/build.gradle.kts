import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    base
}

defaultTasks.add("zipDescriptions")

// ---------------------------------------------------------------------------------------------------------------------
// Class based tasks
// ---------------------------------------------------------------------------------------------------------------------

// ---
// **NOTE** about performance!
// ---
// This is an older approach (referred to as legacy) and it's not recommended anymore as it is slower and included here
// for completion. When using the legacy approach, the task is configure even when this is not used, as the
// configuration of the task happens during the Gradle configuration phase.  Using the new approach, the task
// configuration is delayed to the Gradle execution phase and is only done if the task needs to run.
// ---
// Prefer the `tasks.register()` or the `tasks.registering()` instead
// task<Copy>("generateDescriptions") {
// Both options will provide the same result
// tasks.register<Copy>("generateDescriptions") {
val generateDescriptionsTask: TaskProvider<Copy> = tasks.register<Copy>("generateDescriptions") {
// val generateDescriptions by tasks.registering(Copy::class) {
    group = "Theme park"
    description = "Generates descriptions including token substitution"
    from("descriptions")
    into("$buildDir/descriptions")
    filter(ReplaceTokens::class, "tokens" to mapOf("THEME_PARK_NAME" to "Grelephant's Wonder World"))
}

// Both options will provide the same result
// tasks.register<Zip>("zipDescriptions") {
val zipDescriptions by tasks.registering(Zip::class) {
    group = "Theme park"
    description = "Compress the descriptions into a single ZIP file"
    from("$buildDir/descriptions")
    destinationDirectory.set(buildDir)
    archiveFileName.set("descriptions.zip")
    // Now we can refer to the task directly, and not just as a string
    // dependsOn.add("generateDescriptions")
    dependsOn.add(generateDescriptionsTask)
    // dependsOn.add(generateDescriptions)

    // The task is defined later on.
    finalizedBy(tasks.named("confirmFinish"))
}


// ---------------------------------------------------------------------------------------------------------------------
// Ad-hoc  tasks
// ---------------------------------------------------------------------------------------------------------------------
tasks.register("sayHello") {
    doLast {
        println("Hello!!")
    }
}


// ---------------------------------------------------------------------------------------------------------------------
// Disable tasks (these are always skipped even when called directly)
//  $ ./gradlew sB --console=verbose
// ---------------------------------------------------------------------------------------------------------------------
tasks.register("sayBye") {
    doLast {
        println("Bye!!")
    }
    enabled = false
}


// ---------------------------------------------------------------------------------------------------------------------
// Enable the task based on a condition
//  $ ./gradlew sW --console=verbose
// ---------------------------------------------------------------------------------------------------------------------
tasks.register("sayWellDone") {
    val grade = 79
    doLast {
        println("Well done!!")
    }
    onlyIf {
        grade >= 80
    }
}


// ---------------------------------------------------------------------------------------------------------------------
// Fetch a task by its name, without creating it if missing.  This is ideal for configuring existing tasks, such as
// change the directory where the files are saved
// ---------------------------------------------------------------------------------------------------------------------

// This returns the task and configures it during the Gradle configuration phase (even when the task is not required),
// making it slower (see note at the top).
// tasks.getByName<Copy>("generateDescriptions") {
tasks.named<Copy>("generateDescriptions") {
    into("$buildDir/interpolated")
}
tasks.named<Zip>("zipDescriptions") {
    from("$buildDir/interpolated")
    archiveFileName.set("interpolated.zip")
}

// Default tasks can be configured through their name, but this will configure the task during the Gradle configuration
// phase (even when the task is not required), making it slower (see note at the top).
tasks.clean {
    doLast {
        println("Spic and Span!!")
    }
}


// ---------------------------------------------------------------------------------------------------------------------
// Run a task (referred to as final-task) once another task (referred to as main-task) finishes, even if the final-task
// was never invoked.  The final-task is always executed, even if the main-task fails.  This is very similar to the Java
// `finally` block in the try/catch statement.  Refer to Example 2 in the the
// [Jacoco Plugin documentation page](https://docs.gradle.org/current/userguide/jacoco_plugin.html) for a practical
// example of this.
// ---------------------------------------------------------------------------------------------------------------------
tasks.register("confirmFinish") {
    doLast {
        println("Finished!!")
    }

    // Add this to make a bidirectional relation.  When the zipDescriptions tasks is executed, it is finalized by this
    // task (defined in the zipDescriptions configuration), and if the confirmFinish task is executed, the
    // zipDescriptions task is executed first (even if it is not included in the tasks to be executed) and then it runs
    // this task.
    dependsOn.add(tasks.named("zipDescriptions"))
}
