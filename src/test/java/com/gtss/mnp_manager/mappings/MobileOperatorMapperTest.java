package com.gtss.mnp_manager.mappings;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.gtss.mnp_manager.dtos.MobileOperatorDto;
import com.gtss.mnp_manager.models.MobileOperator;

@SpringBootTest
public class MobileOperatorMapperTest {

    @Autowired
    private MobileOperatorMapper underTest;

    @Test
    void itShouldMaMobileOperatorToMobileOperatorDto() {

        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        MobileOperatorDto mobileOperatorDto = underTest.toDto(mobileOperator);

        assertThat(mobileOperatorDto).isNotNull();
        assertThat(mobileOperatorDto.getOperatorName())
                .isEqualTo(mobileOperatorDto.getOperatorName());
        assertThat(mobileOperatorDto.getOrganizationHeader())
                .isEqualTo(mobileOperatorDto.getOrganizationHeader());
    }

}
