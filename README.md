# Playing with Gradle

## References

- [Tom Gregory Gradle Hero course](https://learn.tomgregory.com/courses/gradle-hero)

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

## Examples

- Custom Gradle tasks (based on [copy](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Copy.html)
  and [zip](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.bundling.Zip.html))

    - [theme-park-rides](theme-park-rides)

- Minimal Gradle (no wrapper)

    - [30-second-project](30-second-project)

## Pending questions

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
