package aa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RideStatusServiceTest {
    @ParameterizedTest(name = "{index} gets {0} ride status")
    @ValueSource(strings = {"rollercoaster", "logflume", "teacups"})
    void getsRideStatus(String ride) {
        RideStatusService rideStatusService = new RideStatusService();
        String rideStatus = rideStatusService.getRideStatus(ride);
        assertNotNull(rideStatus);
    }

    @Test
    void unknownRideCausesFailure() {
        RideStatusService rideStatusService = new RideStatusService();

        assertThrows(IllegalArgumentException.class, () -> {
            rideStatusService.getRideStatus("dodgems");
        });
    }
}