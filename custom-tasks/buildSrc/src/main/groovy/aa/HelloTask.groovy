package aa

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class HelloTask extends DefaultTask {

    @Input
    abstract Property<String> getCaption()

    HelloTask() {
        group = 'Greetings'
        description = 'Prints a greeting message'

        caption.convention('Gradle!!');
    }

    @TaskAction
    def greet() {
        println "Hello ${caption.get()}"
    }
}
