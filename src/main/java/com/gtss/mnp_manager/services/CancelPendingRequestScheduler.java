package com.gtss.mnp_manager.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.PortingStatus;
import com.gtss.mnp_manager.repositories.MobileNumberPortingRepo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Cancel Pending Request Scheduler
 * Scheduler service to cancel pending request after a preconfigured delay
 * Requires two property to be defined in the configuration file
 * app.property.cancellationDelay: Cancellation delay in seconds
 * app.property.cancellationSchedulerRate: Scheduler rate in seconds
 */
@Slf4j
@Service
@NoArgsConstructor
@AllArgsConstructor
public class CancelPendingRequestScheduler {

    /* Mobile Number Porting request cancelation delay in seconds */
    @Value("${app.properties.cancellationDelay}")
    private long cancellationDelayInSeconds;

    @Autowired
    private MobileNumberPortingRepo mobileNumberPortingRepo;

    @Scheduled(fixedRateString = "${app.properties.cancellationSchedulerRate}",
            timeUnit = TimeUnit.SECONDS)
    public void cancelPendingMobileNumberPortingSchedule() {

        List<MobileNumberPorting> canceledMobileNumberPortingItems =
                new ArrayList<MobileNumberPorting>();

        Iterable<MobileNumberPorting> iterablePendingMobileNumberPorting =
                mobileNumberPortingRepo.findAllByStatus(PortingStatus.PENDING);

        LocalDateTime now = LocalDateTime.now();

        int totalPendingRequestCount = 0;
        int expiredPendingRequestCount = 0;

        for (MobileNumberPorting pendingMobileNumberPorting : iterablePendingMobileNumberPorting) {
            totalPendingRequestCount += 1;

            LocalDateTime createdOn = pendingMobileNumberPorting.getCreatedOn();
            boolean isExpired = Duration.between(createdOn, now)
                    .getSeconds() >= cancellationDelayInSeconds;

            if (!isExpired)
                continue;

            pendingMobileNumberPorting.setStatus(PortingStatus.CANCELED);
            pendingMobileNumberPorting.setUpdatedOn(now);
            canceledMobileNumberPortingItems.add(pendingMobileNumberPorting);

            expiredPendingRequestCount += 1;
        }

        log.info(String.format("Found %d / %d expired pending requests",
                expiredPendingRequestCount, totalPendingRequestCount));
        mobileNumberPortingRepo.saveAll(canceledMobileNumberPortingItems);

    }
}
