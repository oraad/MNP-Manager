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
    void itShouldFindOperatorByOrganizationHeader() {
        // Given
        String organizationHeader = "vodafone";
        MobileOperator mobileOperator =
                new MobileOperator("Vodafone", organizationHeader);

        // When
        underTest.save(mobileOperator);

        Optional<MobileOperator> optionalMobileOperator =
                underTest.findMobileOperatorByOrganizationHeader(organizationHeader);

        // Then
        assertThat(optionalMobileOperator).isPresent().hasValueSatisfying(c -> {
            assertThat(c).usingRecursiveComparison().isEqualTo(mobileOperator);
        });
    }

    @Test
    void itShouldNotFindOperatorByOrganizationHeaderWhenDoesNotExists() {
        // Given
        String organizationHeader = "vodafone";

        // When
        Optional<MobileOperator> optionalMobileOperator =
                underTest.findMobileOperatorByOrganizationHeader(organizationHeader);

        // Then
        assertThat(optionalMobileOperator).isNotPresent();
    }
}
