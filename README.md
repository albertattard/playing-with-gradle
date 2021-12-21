# Playing with Gradle

## References

- [Tom Gregory Gradle Hero course](https://learn.tomgregory.com/courses/gradle-hero)
- [Migrating build logic from Groovy to Kotlin](https://docs.gradle.org/current/userguide/migrating_from_groovy_to_kotlin_dsl.html)
- [Tasks](https://docs.gradle.org/current/dsl/org.gradle.api.Task.html)

## Tooling

- Phases

  | Phase          | Source                       |
    |----------------|------------------------------|
  | Initialisation | `settings.gradle` (required) |
  | Configuration  | `build.gradle` (optional)    |
  | Execution      | Tasks names (optional)       |

- Set gradle wrapper version

  ```shell
  $ ./gradlew wrapper --gradle-version=7.3.2
  ```

  See the [Gradle Wrapper page](https://docs.gradle.org/current/userguide/gradle_wrapper.html) for more information.

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

## Examples

- Custom Gradle tasks (based on [copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)
  and [zip](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Zip.html))

    - [theme-park-rides](theme-park-rides)
    - [theme-park-rides-kotlin](theme-park-rides-kotlin)

- Kotlin configuration

    - [theme-park-rides-kotlin](theme-park-rides-kotlin)

- Life cycle

    - [gradle-init-test](gradle-init-test)

- Minimal Gradle (no wrapper)

    - [30-second-project](30-second-project)
    - [gradle-init-test](gradle-init-test)

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
