package com.gtss.mnp_manager.mappings;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.gtss.mnp_manager.dtos.MobileNumberPortingWithActionsDto;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import com.gtss.mnp_manager.models.PortingStatus;

@SpringBootTest
public class MobileNumberPortingWithActionsMapperTest {

    @Autowired
    private MobileNumberPortingWithActionsMapper underTest;

    @Test
    void itShouldMapMobileNumberPortingToMobileNumberPortingWithActionsDto() {

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        MobileSubscriber mobileSubscriber = new MobileSubscriber("010111101");
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        LocalDateTime now = LocalDateTime.now();
        PortingStatus status = PortingStatus.PENDING;

        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, status);

        MobileNumberPortingWithActionsDto mobileNumberPortingDto =
                underTest.toDto(mobileNumberPorting);

        assertThat(mobileNumberPortingDto).isNotNull();
        assertThat(mobileNumberPorting.getMobileSubscriber().getMobileNumber())
                .isEqualTo(mobileNumberPortingDto.getMobileNumber());
        assertThat(
                mobileNumberPorting.getDonorMobileOperator().getOperatorName())
                        .isEqualTo(
                                mobileNumberPortingDto.getDonorOperatorName());
        assertThat(mobileNumberPorting.getRecipientMobileOperator()
                .getOperatorName()).isEqualTo(
                        mobileNumberPortingDto.getRecipientOperatorName());
        assertThat(mobileNumberPorting.getCreatedOn())
                .isEqualTo(mobileNumberPortingDto.getCreatedOn());
        assertThat(mobileNumberPorting.getUpdatedOn())
                .isEqualTo(mobileNumberPortingDto.getUpdatedOn());
        assertThat(mobileNumberPorting.getStatus())
                .isEqualTo(mobileNumberPortingDto.getStatus());
    }
}
