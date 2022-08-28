package com.gtss.mnp_manager.mappings;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.gtss.mnp_manager.dtos.MobileNumberPortingDto;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import com.gtss.mnp_manager.models.PortingStatus;

@SpringBootTest
public class MobileNumberPortingMapperTest {

    @Autowired
    private MobileNumberPortingMapper underTest;

    @Test
    void itShouldMapMobileNumberPortingToMobileNumberPortingDto() {

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

        MobileNumberPortingDto mobileNumberPortingDto =
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

    @Test
    void itShouldMapIterableMobileNumberPortingToIterableMobileNumberPortingDto() {

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

        MobileNumberPorting mobileNumberPorting1 =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, status);

        MobileNumberPorting mobileNumberPorting2 =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, status);

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(mobileNumberPorting1, mobileNumberPorting2);

        Iterable<MobileNumberPortingDto> iterableMobileNumberPortingDto =
                underTest.toDto(mobileNumberPortings);

        assertThat(iterableMobileNumberPortingDto).hasSize(2);
    }

    @ParameterizedTest
    @CsvSource({"mobileNumber, mobileSubscriber.mobileNumber",
            "donorOperator, donorMobileOperator.operatorName",
            "recipientOperator, recipientMobileOperator.operatorName",
            "createdOn, createdOn"})
    void itShouldMapDtoJsonPropertyToModelProperty(String jsonProperty,
            String expectedProperty) {

        String modelProperty =
                underTest.fromJsonDtotoModelProperty(jsonProperty);
        assertThat(modelProperty).isEqualTo(expectedProperty);
    }
}
