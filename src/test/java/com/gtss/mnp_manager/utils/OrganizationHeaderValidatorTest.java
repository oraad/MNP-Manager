package com.gtss.mnp_manager.utils;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


public class OrganizationHeaderValidatorTest {

    private OrganizationHeaderValidator underTest;

    @Mock
    private MobileOperatorRepo mobileOperatorRepo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new OrganizationHeaderValidator(mobileOperatorRepo);
    }

    @ParameterizedTest
    @CsvSource({",false", "vodafone,true", "Vodafone,false", "XYZ,false"})
    void itShouldValidateOrganizationHeader(String organizationHeader,
            Boolean expected) {

        // Given
        given(mobileOperatorRepo
                .findMobileOperatorByOrganizationHeader("vodafone"))
                        .willReturn(Optional.of(
                                new MobileOperator("Vodafone", "vodafone")));

        // When
        Boolean isValid = underTest.validate(organizationHeader);

        // Then
        assertThat(isValid).isEqualTo(expected);
    }

}
