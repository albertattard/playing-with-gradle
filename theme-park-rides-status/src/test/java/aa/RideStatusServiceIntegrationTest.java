package aa;

import org.junit.jupiter.api.Test;

class RideStatusServiceIntegrationTest {
    @Test
    void noError() {
        RideStatusService.main(new String[]{"logflume"});
    }
}
