package aa;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Transformer;
import org.gradle.api.file.RegularFile;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.Optional;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class HashFileTask extends DefaultTask {

    @InputFile
    abstract Property<File> getFile();

    @OutputFile
    @Optional
    abstract Property<File> getOutput();

    @Input
    abstract Property<String> getAlgorithm();

    public HashFileTask() {
        setGroup("Hash Files");
        setDescription("Creates a new file containing the hash for teh given file");

        getAlgorithm().convention("SHA-256");
    }

    @TaskAction
    void execute() {
        try {
            final String hex = computeHash();
            writeHashToFile(hex);
        } catch (final IOException | NoSuchAlgorithmException e) {
            throw new GradleException("Failed to run task", e);
        }
    }

    private String computeHash() throws IOException, NoSuchAlgorithmException {
        final byte[] encoded = readHash();
        final String hex = bytesToHex(encoded);
        getLogger().info("Hash value {}", hex);
        return hex;
    }

    private byte[] readHash() throws NoSuchAlgorithmException, IOException {
        final File file = getFile().get();
        final String algorithm = getAlgorithm().get();
        getLogger().info("Hashing file {} using {}", file, algorithm);

        final MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (DigestInputStream dis = new DigestInputStream(new BufferedInputStream(new FileInputStream(file)), digest)) {
            while (dis.read() != -1) {/* Read the file and compute the hash */}
        }

        return digest.digest();
    }

    private void writeHashToFile(final String hex) throws IOException {
        final File file = getOutput().orElse(createOutputFile()).get();
        getLogger().info("Writing the hash value to {}", file);
        ensureDirectoryExists(file);
        writeToFile(hex, file);
    }

    private void writeToFile(final String text, final File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(text);
        }
    }

    private void ensureDirectoryExists(final File file) throws FileNotFoundException {
        final File dir = file.getParentFile();
        if (!dir.isDirectory()) {
            getLogger().info("Creating output directory {}", dir);
            if (!dir.mkdirs()) {
                throw new FileNotFoundException("Failed to create the output directory " + dir);
            }
        }
    }

    private Provider<File> createOutputFile() {
        final Path projectDir = getProject().getProjectDir().toPath();
        final Path relativePath = projectDir.relativize(getFile().get().toPath());

        return getProject().getLayout().getBuildDirectory().file(relativePath.toString())
                .map(RegularFile::getAsFile)
                .map(ensureDirectoryExists())
                .map(suffixWithAlgorithmName());
    }

    private Transformer<File, File> ensureDirectoryExists() {
        return file -> {
            final File dir = file.getParentFile();
            if (!dir.isDirectory()) {
                getLogger().info("Creating output directory {}", dir);
                if (!dir.mkdirs()) {
                    throw new GradleException("Failed to create the output directory " + dir);
                }
            }
            return file;
        };
    }

    private Transformer<File, File> suffixWithAlgorithmName() {
        return file -> {
            final String prefix = getAlgorithm().get();
            final String fileName = String.format("%s.%s", file.getName(), prefix);
            final File dir = file.getParentFile();
            return new File(dir, fileName);
        };
    }

    private static String bytesToHex(final byte[] bytes) {
        final StringBuilder buffer = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            final String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                buffer.append('0');
            }
            buffer.append(hex);
        }

        return buffer.toString();
    }
}
