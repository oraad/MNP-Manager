package com.gtss.mnp_manager.integrations;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import com.gtss.mnp_manager.services.CancelPendingRequestSchedulerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
public class SchedulerIntegrationTest {

    @SpyBean
    private CancelPendingRequestSchedulerTest cancelPendingRequestScheduler;

    @Test
    void itShouldTriggerCancelPendingMobileNumberPorting() {

        await().atMost(4, SECONDS)
                .untilAsserted(() -> verify(
                        cancelPendingRequestScheduler,
                        atLeast(4)));

    }

}
