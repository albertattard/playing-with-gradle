package aa;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HashFileTaskTest {

    @TempDir
    File testProjectDir;

    private File settingsFile;
    private File buildScriptFile;
    private File buildScrDir;
    private File fixturesFile;

    @BeforeEach
    void setup() {
        settingsFile = new File(testProjectDir, "settings.gradle");
        buildScriptFile = new File(testProjectDir, "build.gradle");
        buildScrDir = new File(testProjectDir, "buildSrc");
        fixturesFile = new File(testProjectDir, "fixtures/sample.txt");
    }

    @Test
    void computesHashOfFile() {
        final String buildScriptContent = """
                      file = project.file('fixtures/sample.txt')
                      output = project.layout.buildDirectory.file('fixtures/sample.txt.sha256').get().asFile
                """;

        runTask(buildScriptContent);

        final File shaFile = new File(testProjectDir, "build/fixtures/sample.txt.sha256");
        assertHash(shaFile);
    }

    @Test
    void createsTheOutputFile() {
        final String buildScriptContent = """
                      file = project.file('fixtures/sample.txt')
                """;

        runTask(buildScriptContent);

        final File shaFile = new File(testProjectDir, "build/fixtures/sample.txt.SHA-256");
        assertHash(shaFile);
    }

    private void assertHash(final File file) {
        assertTrue(file.isFile());
        final String hash = readFile(file);
        assertEquals("d54c08b1a0ec3053a154ab73c7d3ef5e5d1d9e75f5ba294e5956015e6891c981", hash);
    }

    private void runTask(final String configuration) {
        final String script = """
                import aa.HashFileTask
                                
                tasks.register('test', HashFileTask) {
                %s
                }
                """;

        writeFile(settingsFile, "rootProject.name = 'hash-file'");
        writeFile(buildScriptFile, String.format(script, configuration));
        writeFile(fixturesFile, readFile(new File("src/test/resources/fixtures/sample.txt")));
        copyDir(new File("src/main"), new File(buildScrDir, "src/main"));

        final BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)
                .withArguments("test")
                .build();

        final BuildTask task = result.task(":test");

        assertNotNull(task);
        assertEquals(SUCCESS, task.getOutcome());
    }

    private static void copyDir(final File source, final File destination) {
        copyDir(source.toPath(), destination.toPath());
    }

    private static void copyDir(final Path source, final Path destination) {
        checkedIo(() -> Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {
                final Path resolve = destination.resolve(source.relativize(dir));
                Files.createDirectories(resolve);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {
                final Path resolve = destination.resolve(source.relativize(file));
                Files.copy(file, resolve);
                return FileVisitResult.CONTINUE;
            }
        }));
    }

    private static void writeFile(final File file, final String content) {
        ensureParentDirectoryExists(file);
        checkedIo(() -> Files.writeString(file.toPath(), content, StandardCharsets.UTF_8));
    }

    private static String readFile(final File file) {
        return checkedIo(() -> Files.readString(file.toPath(), StandardCharsets.UTF_8));
    }

    private static void ensureParentDirectoryExists(final File file) {
        final File directory = file.getParentFile();
        assertTrue(directory.isDirectory() || directory.mkdirs());
    }

    private static void checkedIo(final Task task) {
        try {
            task.execute();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static <T> T checkedIo(final ValueSupplier<T> supplier) {
        try {
            return supplier.execute();
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @FunctionalInterface
    private interface Task {
        void execute() throws IOException;
    }

    @FunctionalInterface
    private interface ValueSupplier<T> {
        T execute() throws IOException;
    }
}

/* openssl dgst -sha256 <file> */
/* openssl dgst -sha256 fixtures/a.txt */
/* d54c08b1a0ec3053a154ab73c7d3ef5e5d1d9e75f5ba294e5956015e6891c981 */

