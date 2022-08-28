package com.gtss.mnp_manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import com.gtss.mnp_manager.models.PortingStatus;
import com.gtss.mnp_manager.repositories.MobileNumberPortingRepo;
import com.gtss.mnp_manager.utils.TemporalComparator;

// @ExtendWith(SpringExtension.class)
// @TestPropertySource("classpath:application.properties")
// @SpringBootTest
// @EnableConfigurationProperties
public class CancelPendingRequestSchedulerTest {

    private CancelPendingRequestScheduler underTest;

    @Mock
    private MobileNumberPortingRepo mobileNumberPortingRepo;

    @Captor
    private ArgumentCaptor<Iterable<MobileNumberPorting>> mobileNumberPortingArgumentCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new CancelPendingRequestScheduler(4L,
                mobileNumberPortingRepo);
    }

    @Test
    void itShouldCancelExpiredPendingMobileNumberPorting() {

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        MobileSubscriber mobileSubscriber = new MobileSubscriber("0101110");
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime createdOnActive = now;
        LocalDateTime createdOnExpired = now.minusSeconds(4);

        MobileNumberPorting mobileNumberPortingActive = new MobileNumberPorting(
                mobileSubscriber, donorOperator, recipientOperator,
                createdOnActive, PortingStatus.PENDING);

        MobileNumberPorting mobileNumberPortingExpired =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, createdOnExpired,
                        PortingStatus.PENDING);

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(mobileNumberPortingActive, mobileNumberPortingExpired);

        given(mobileNumberPortingRepo.findAllByStatus(PortingStatus.PENDING))
                .willReturn(mobileNumberPortings);

        underTest.cancelPendingMobileNumberPortingSchedule();

        then(mobileNumberPortingRepo).should()
                .saveAll(mobileNumberPortingArgumentCaptor.capture());

        Iterable<MobileNumberPorting> mobileNumberPortingArgumentValue =
                mobileNumberPortingArgumentCaptor.getValue();

        assertThat(mobileNumberPortingArgumentValue)
                .containsOnlyOnce(mobileNumberPortingExpired)
                .doesNotContain(mobileNumberPortingActive)
                .extracting("updatedOn", "status").allMatch(fieldTuple -> {
                    List<Object> fieldList = fieldTuple.toList();
                    LocalDateTime updatedOn = (LocalDateTime) fieldList.get(0);
                    PortingStatus status = (PortingStatus) fieldList.get(1);

                    boolean isUpdateOnCloseToNow = TemporalComparator
                            .isCloseTo(2).equals(updatedOn, now);
                    boolean isSameStatus =
                            status.equals(PortingStatus.CANCELED);

                    return isUpdateOnCloseToNow && isSameStatus;
                });

    }

}
