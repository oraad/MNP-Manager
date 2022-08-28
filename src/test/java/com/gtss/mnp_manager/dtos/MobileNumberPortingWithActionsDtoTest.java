package com.gtss.mnp_manager.dtos;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import com.gtss.mnp_manager.dtos.MobileNumberPortingWithActionsDto.MobileNumberPortingActions;
import com.gtss.mnp_manager.models.PortingStatus;

@SpringBootTest
public class MobileNumberPortingWithActionsDtoTest {

    @ParameterizedTest
    @CsvSource({"123,PENDING,mnp,true", "123,ACCEPTED,mnp,false",
            "123,PENDING,baseURI,true"})
    void itShouldGenerateActionsForMobileNumberPortingWithActionsDto(
            String mobileNumber, PortingStatus status, String baseUri,
            boolean expected) {

        MobileNumberPortingWithActionsDto mobileNumberPortingWithActionsDto =
                new MobileNumberPortingWithActionsDto();

        mobileNumberPortingWithActionsDto.setMobileNumber(mobileNumber);
        mobileNumberPortingWithActionsDto.setStatus(status);
        mobileNumberPortingWithActionsDto.generateActions(baseUri);

        if (!expected)
            assertThat(mobileNumberPortingWithActionsDto.getActions()).isNull();
        else {
            assertThat(mobileNumberPortingWithActionsDto.getActions())
                    .isNotNull();
            MobileNumberPortingActions mobileNumberPortingActions =
                    mobileNumberPortingWithActionsDto.getActions();
            assertThat(mobileNumberPortingActions.getAccept()).isEqualTo(
                    baseUri + "/" + mobileNumber + "/" + "accept");
            assertThat(mobileNumberPortingActions.getReject()).isEqualTo(
                    baseUri + "/" + mobileNumber + "/" + "reject");
        }
    }
}
