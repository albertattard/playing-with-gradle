package aa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RideStatusService {
    public static void main(final String[] args) {
        if (args.length != 1) {
            System.out.println("A single ride name must be passed");
            System.exit(1);
        }

        String rideName = args[0];
        String rideStatus = getRideStatus(rideName);

        System.out.printf("Current status of %s is '%s'%n", rideName, rideStatus);
    }

    static String getRideStatus(final String ride) {
        final List<String> rideStatuses = readFile(String.format("%s.txt", ride));
        return rideStatuses.get(new Random().nextInt(rideStatuses.size()));
    }

    private static List<String> readFile(final String filename) {
        final InputStream resourceStream = RideStatusService.class.getClassLoader().getResourceAsStream(filename);
        if (resourceStream == null) {
            throw new IllegalArgumentException("Ride not found");
        }

        final List<String> result = new ArrayList<>();
        try (BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(resourceStream, StandardCharsets.UTF_8))) {
            while (bufferedInputStream.ready()) {
                result.add(bufferedInputStream.readLine());
            }
        } catch (final IOException exception) {
            throw new RuntimeException("Couldn't read file", exception);
        }

        return result;
    }
}
