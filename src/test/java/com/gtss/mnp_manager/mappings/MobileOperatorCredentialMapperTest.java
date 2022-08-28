package com.gtss.mnp_manager.mappings;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.gtss.mnp_manager.dtos.MobileOperatorCredentialDto;
import com.gtss.mnp_manager.models.MobileOperatorCredential;

@SpringBootTest
public class MobileOperatorCredentialMapperTest {

    @Autowired
    private MobileOperatorCredentialMapper underTest;

    @Test
    void itShouldMaMobileOperatorCredentialDtoToMobileOperatorCredential() {

        MobileOperatorCredentialDto mobileOperatorCredentialDto =
                new MobileOperatorCredentialDto("OperatorA");

        MobileOperatorCredential mobileOperatorCredential =
                underTest.toModel(mobileOperatorCredentialDto);

        assertThat(mobileOperatorCredential).isNotNull();
        assertThat(mobileOperatorCredential.getOperatorName())
                .isEqualTo(mobileOperatorCredentialDto.getOperatorName());

    }

}
