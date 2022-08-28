package com.gtss.mnp_manager.integrations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.gtss.mnp_manager.dtos.MobileOperatorCredentialDto;
import com.gtss.mnp_manager.dtos.MobileOperatorDto;
import com.gtss.mnp_manager.mappings.MobileOperatorMapper;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import com.gtss.mnp_manager.services.DatabaseCleanupService;
import com.gtss.mnp_manager.utils.JsonParser;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MobileOperatorRepo mobileOperatorRepo;

    @Autowired
    private DatabaseCleanupService databaseCleanupService;

    @Autowired
    private MobileOperatorMapper mobileOperatorMapper;

    @BeforeEach
    void setup() {

    }

    @AfterEach
    void cleanupAfterEach() {
        databaseCleanupService.truncate();
    }

    @ParameterizedTest
    @CsvSource({"'',false", "xyz,false", "OperatorA,true"})
    void itShouldAuthenticateOperatorByCredential(String operatorName,
            Boolean expected) throws Exception {

        String restEndpointUri = "/api/v1/auth/login";

        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        mobileOperatorRepo.save(mobileOperator);

        MobileOperatorCredentialDto mobileOperatorCredentialDto =
                new MobileOperatorCredentialDto(operatorName);

        ResultActions mobileOperatorAuthenticationResultActions =
                mockMvc.perform(post(restEndpointUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonParser.asJsonString(mobileOperatorCredentialDto)));

        mobileOperatorAuthenticationResultActions.andExpect(
                expected ? status().isOk() : status().isBadRequest());

        String content = mobileOperatorAuthenticationResultActions.andReturn()
                .getResponse().getContentAsString();

        if (expected) {

            MobileOperatorDto response =
                    JsonParser.asDtoObject(content, MobileOperatorDto.class);
            MobileOperatorDto mobileOperatorDto =
                    mobileOperatorMapper.toDto(mobileOperator);

            assertThat(response).usingRecursiveComparison()
                    .isEqualTo(mobileOperatorDto);
        } else {
            assertThat(content).isEmpty();
        }
    }

    @ParameterizedTest
    @CsvSource({"'',false", "xyz,false", "operatorA,true"})
    void itShouldAuthenticateOperatorByOrganizationHeader(
            String organizationHeader, Boolean expected) throws Exception {

        String restEndpointUri = "/api/v1/auth";

        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        mobileOperatorRepo.save(mobileOperator);

        ResultActions mobileOperatorAuthenticationResultActions =
                mockMvc.perform(post(restEndpointUri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("organization", organizationHeader));

        mobileOperatorAuthenticationResultActions.andExpect(
                expected ? status().isOk() : status().isBadRequest());

        String content = mobileOperatorAuthenticationResultActions.andReturn()
                .getResponse().getContentAsString();

        if (expected) {

            MobileOperatorDto response =
                    JsonParser.asDtoObject(content, MobileOperatorDto.class);
            MobileOperatorDto mobileOperatorDto =
                    mobileOperatorMapper.toDto(mobileOperator);

            assertThat(response).usingRecursiveComparison()
                    .isEqualTo(mobileOperatorDto);
        } else {
            assertThat(content).isEmpty();
        }

    }
}
