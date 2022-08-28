package com.gtss.mnp_manager.repositories;

import java.util.Optional;
import com.gtss.mnp_manager.models.MobileOperator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MobileOperatorRepoTest {

    @Autowired
    private MobileOperatorRepo underTest;

    @Test
    void itShouldFindByOrganizationHeader() {

        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        String organizationHeader = mobileOperator.getOrganizationHeader();

        underTest.save(mobileOperator);

        Optional<MobileOperator> optionalMobileOperator = underTest
                .findByOrganizationHeader(organizationHeader);

        assertThat(optionalMobileOperator).isPresent()
                .hasValueSatisfying(foundMobileOperator -> {
                    assertThat(foundMobileOperator).usingRecursiveComparison()
                            .isEqualTo(mobileOperator);
                });
    }

    @Test
    void itShouldNotFindByOrganizationHeaderWhenDoesNotExists() {

        String organizationHeader = "operatorA";

        Optional<MobileOperator> optionalMobileOperator = underTest
                .findByOrganizationHeader(organizationHeader);

        assertThat(optionalMobileOperator).isNotPresent();
    }

    @Test
    void itShouldFindByOperatorName() {

        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        String operatorName = mobileOperator.getOperatorName();

        underTest.save(mobileOperator);

        Optional<MobileOperator> optionalMobileOperator = underTest
                .findByOperatorName(operatorName);

        assertThat(optionalMobileOperator).isPresent()
                .hasValueSatisfying(foundMobileOperator -> {
                    assertThat(foundMobileOperator).usingRecursiveComparison()
                            .isEqualTo(mobileOperator);
                });
    }

    @Test
    void itShouldNotFindByOperatorNameWhenDoesNotExists() {

        String operatorName = "OperatorA";

        Optional<MobileOperator> optionalMobileOperator =
                underTest.findByOperatorName(operatorName);

        assertThat(optionalMobileOperator).isNotPresent();
    }
}
