# Playing with Gradle

## References

- [Tom Gregory Gradle Hero course](https://learn.tomgregory.com/courses/gradle-hero)
- [Migrating build logic from Groovy to Kotlin](https://docs.gradle.org/current/userguide/migrating_from_groovy_to_kotlin_dsl.html)
- [Tasks](https://docs.gradle.org/current/dsl/org.gradle.api.Task.html)
- Annotation Processors
    - [Lombok](https://projectlombok.org/) - a java library that automatically plugs into your editor and build tools,
      spicing up your java
    - [MapStruct](https://mapstruct.org/) - a code generator that greatly simplifies the implementation of mappings
      between Java bean types based on a convention over configuration approach
- [Gradle TestSets plugin](https://github.com/unbroken-dome/gradle-testsets-plugin)
- [Build Environment](https://docs.gradle.org/current/userguide/build_environment.html)
- [The Java Library Plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html)

## Tooling

- Phases

  | Phase          | Source                       |
  |----------------|------------------------------|
  | Initialisation | `settings.gradle` (required) |
  | Configuration  | `build.gradle` (optional)    |
  | Execution      | Tasks names (optional)       |

- Set gradle wrapper version

  ```shell
  $ ./gradlew wrapper --gradle-version=7.3.3
  ```

  See the [Gradle Wrapper page](https://docs.gradle.org/current/userguide/gradle_wrapper.html) for more information.

- List the Gradle and Java versions

  ```shell
  $ ./gradlew --version
  ```

- List the tasks that are executed (`--console=verbose`)

  ```shell
  $ ./gradlew build --console=verbose
  ```

- Gradle daemons

  List the running (`--status`)

  ```shell
  $ ./gradlew --status
  ```

  Stop the running daemons (`--stop`)

  ```shell
  $ ./gradlew --stop
  ```

  Force the build (or any other task) not to use the daemon (`--no-daemon`)

  ```shell
  $ ./gradlew --no-daemon
  ```

- Inspect the contents of a ZIP/JAR file

  ```shell
  $ jar tf file.zip
  ```

- Start application in debug mode (`--debug-jvm`)

  ```shell
  $ ./gradlew <task> --debug-jvm
  ```

- Pass project properties

  ```shell
  $ ./gradlew <task> -P<property-name>=<property-value>
  ```

- List projects

  ```shell
  $ ./gradlew projects
  ```

- Run command on a single project (in multi-project)

  ```shell
  $ ./gradlew :app:clean
  ```

  or (without the starting `:`)

  ```shell
  $ ./gradlew app:clean
  ```

## Examples

- Configure task options

    - [theme-park-rides-status](theme-park-rides-status)

- Custom Gradle tasks (based on [copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)
  and [zip](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Zip.html))

    - [theme-park-rides](theme-park-rides)
    - [theme-park-rides-kotlin](theme-park-rides-kotlin)

- Dependencies

    - [repositories-dependencies](repositories-dependencies)

- Inputs and outputs

    - [tasks-inputs-outputs](tasks-inputs-outputs)

- Integration tests

    - [theme-park-rides-status](theme-park-rides-status)

- Kotlin configuration

    - [theme-park-rides-kotlin](theme-park-rides-kotlin)
    - [build-script-demo-kotlin](build-script-demo-kotlin)
    - [repositories-dependencies-kotlin](repositories-dependencies-kotlin)

- Life cycle

    - [gradle-init-test](gradle-init-test)

- Maven publish

    - [theme-park-rides-status](theme-park-rides-status)

- Minimal Gradle (no wrapper)

    - [30-second-project](30-second-project)
    - [gradle-init-test](gradle-init-test)

- Multi-project

    - [multi-project-example](multi-project-example)
    - [theme-park-manager](theme-park-manager)

- Project properties

    - [project-properties](project-properties)

## Questions

1. Why some properties need and equals while others not?

   ```groovy
   tasks.register('zipDescriptions', Zip) {
     from "$buildDir/descriptions"
     destinationDirectory = buildDir
     archiveFileName = 'descriptions.zip'
     dependsOn 'generateDescriptions'
   }
   ```

   Copied from [theme-park-rides/build.gradle](theme-park-rides/build.gradle) file.

   **Possible answer**

   The `from "$buildDir/descriptions"` is a method call, equivalent to `from("$buildDir/descriptions")`,
   while `destinationDirectory = buildDir` is simply assigning a property.
