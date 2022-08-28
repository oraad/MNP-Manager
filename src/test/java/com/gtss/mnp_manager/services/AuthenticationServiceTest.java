package com.gtss.mnp_manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import com.gtss.mnp_manager.exceptions.BadRequestException;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileOperatorCredential;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;

public class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService underTest;

    @Mock
    private MobileOperatorRepo mobileOperatorRepo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new AuthenticationService(mobileOperatorRepo);
    }

    @Test
    void itShouldAuthenticateOperatorByOperatorName() {

        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperatorCredential mobileOperatorCredential =
                new MobileOperatorCredential(operatorA.getOperatorName());

        given(mobileOperatorRepo
                .findByOperatorName(operatorA.getOperatorName()))
                        .willReturn(Optional.of(operatorA));

        MobileOperator resultMobileOperator = underTest
                .authenticateOperatorByCredential(mobileOperatorCredential);

        assertThat(resultMobileOperator)
                .isEqualTo(operatorA);
    }

    @Test
    void itShouldNotAuthenticateOperatorUsingInvalidName() {

        MobileOperatorCredential mobileOperatorCredential =
                new MobileOperatorCredential();

        Throwable thrown = catchThrowable(() -> {
            underTest.authenticateOperatorByCredential(mobileOperatorCredential);
        });

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Operator not found.");
        
    }

    @Test
    void itShouldAuthenticateOperatorByOrganizationHeader() {

        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        String organizationHeader = operatorA.getOrganizationHeader();

        given(mobileOperatorRepo
                .findByOrganizationHeader(organizationHeader))
                        .willReturn(Optional.of(operatorA));

        MobileOperator resultMobileOperator = underTest
                .authenticateOperatorByOrganizationHeader(organizationHeader);

        assertThat(resultMobileOperator)
                .isEqualTo(operatorA);
    }

    @Test
    void itShouldNotAuthenticateOperatorUsingInvalidOrganizationHeader() {

        String organizationHeader = "operatorA";

        Throwable thrown = catchThrowable(() -> {
            underTest.authenticateOperatorByOrganizationHeader(organizationHeader);
        });

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Operator not found.");
        
    }
}
