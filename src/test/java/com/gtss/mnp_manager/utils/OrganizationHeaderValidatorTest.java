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
    @CsvSource({",false", "'',false", "' ',false", "operatorA,true",
            "OperatorA,false", "XYZ,false"})
    void itShouldValidateOrganizationHeader(String organizationHeader,
            Boolean expected) {

        given(mobileOperatorRepo
                .findByOrganizationHeader("operatorA"))
                        .willReturn(Optional.of(
                                new MobileOperator("OperatorA", "operatorA")));

        Boolean isValid = underTest.validate(organizationHeader);

        assertThat(isValid).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({",false", "'',false", "' ',false", "operatorA,true"})
    void itShouldCheckOrganizationHeaderExists(String organizationHeader,
            Boolean expected) {

        Boolean isValid = underTest.isHeaderExists(organizationHeader);

        assertThat(isValid).isEqualTo(expected);
    }

}
