package aa

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class FileDiffTask extends DefaultTask {

    @InputFile
    abstract Property<File> getA()

    @InputFile
    abstract Property<File> getB()

    @TaskAction
    def diff() {
        if (a.get().size() == b.get().size()) {
            println "The files ${a.get().name} and ${b.get().name} have the same size"
        } else if (a.get().size() > b.get().size()) {
            println "The file ${a.get().name} is larger"
        } else {
            println "The file ${b.get().name} is larger"
        }
    }
}
