package com.gtss.mnp_manager.integrations;

import static org.awaitility.Awaitility.await;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import com.gtss.mnp_manager.models.PortingStatus;
import com.gtss.mnp_manager.repositories.MobileNumberPortingRepo;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import com.gtss.mnp_manager.repositories.MobileSubscriberRepo;
import com.gtss.mnp_manager.services.DatabaseCleanupService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MobileNumberPortingIntegrationTest {

    private String baseRestEndpointUri = "/api/v1";
    private String restEndpointUri = "/api/v1/mnp";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MobileOperatorRepo mobileOperatorRepo;

    @Autowired
    private MobileSubscriberRepo mobileSubscriberRepo;

    @Autowired
    private MobileNumberPortingRepo mobileNumberPortingRepo;

    @Autowired
    private DatabaseCleanupService databaseCleanupService;

    @BeforeEach
    void setup() {

    }

    @AfterEach
    void cleanupAfterEach() {
        databaseCleanupService.truncate();
    }

    @ParameterizedTest
    @CsvSource({"'',false", "xyz,false", "operatorA,true"})
    void itShouldValidateAuthorizedOrganizationForMNPRequests(
            String organizationHeader, Boolean authorized) throws Exception {

        String requestUri = restEndpointUri + "/test";

        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        mobileOperatorRepo.save(mobileOperator);

        ResultActions mobileNumberPortingResultActions =
                mockMvc.perform(get(requestUri).header("organization",
                        organizationHeader));

        mobileNumberPortingResultActions.andExpect(
                authorized ? status().isNotFound() : status().isUnauthorized());
    }

    @Test
    void itShouldIgnoreOrganizationHeaderValidationForNonMNPApi()
            throws Exception {

        String requestUri = baseRestEndpointUri;

        ResultActions mobileNumberPortingResultActions =
                mockMvc.perform(get(requestUri));

        mobileNumberPortingResultActions.andExpect(status().isNotFound());
    }

    @Test
    void itShouldCreateNewRequestSuccessfully() throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isCreated());

    }

    @Test
    void itShouldNotCreateNewRequestWhenPendingRequestExists()
            throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        LocalDateTime now = LocalDateTime.now();
        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, PortingStatus.PENDING);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo.save(mobileNumberPorting);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isBadRequest());

    }

    @Test
    void itShouldNotCreateNewRequestForInvalidMobileNumber() throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isNotFound());

    }

    @Test
    void itShouldNotCreateNewRequestWhenRecipientIsDonor() throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator = donorOperator;

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        mobileOperatorRepo.save(donorOperator);
        mobileSubscriberRepo.save(mobileSubscriber);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isBadRequest());

    }

    @Test
    void itShouldCancelRequestOlderThanPredefinedDuration() throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");;

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isCreated());

        await().timeout(Duration.ofSeconds(6))
                .pollDelay(Duration.ofSeconds(4)).until(() -> {
                    Optional<MobileNumberPorting> optionalMobileNumberPorting =
                            mobileNumberPortingRepo
                                    .findByMobileSubscriberAndStatus(
                                            mobileSubscriber,
                                            PortingStatus.CANCELED);

                    return optionalMobileNumberPorting.isPresent();
                });

    }

    @ParameterizedTest
    @CsvSource({"accept", "reject"})
    void itShouldAcceptOrRejectRequestSuccessfully(String action)
            throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber + "/" + action;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = donorOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        LocalDateTime now = LocalDateTime.now();
        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, PortingStatus.PENDING);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo.save(mobileNumberPorting);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isOk());

    }

    @ParameterizedTest
    @CsvSource({"accept", "reject"})
    void itShouldNotAcceptOrRejectRequestIfOrganizationHeaderNotDonorOperator(
            String action) throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber + "/" + action;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        LocalDateTime now = LocalDateTime.now();
        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, PortingStatus.PENDING);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo.save(mobileNumberPorting);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isForbidden());

    }

    @ParameterizedTest
    @CsvSource({"accept", "reject"})
    void itShouldNotAcceptOrRejectRequestForInvalidMobileNumber(String action)
            throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber + "/" + action;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = donorOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isNotFound());

    }

    @ParameterizedTest
    @CsvSource({"accept", "reject"})
    void itShouldNotAcceptOrRejectRequestForNonPendingRequest(String action)
            throws Exception {

        String mobileNumber = "01100000000";
        String requestUri = restEndpointUri + "/" + mobileNumber + "/" + action;

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = donorOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        LocalDateTime now = LocalDateTime.now();
        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, PortingStatus.CANCELED);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo.save(mobileNumberPorting);

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                post(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions.andExpect(status().isNotFound());

    }

    @Test
    void itShouldReturnAllByMobileOperatorOrAcceptedStatus()
            throws Exception {

        String requestUri = restEndpointUri;

        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = operatorA.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        // Donor is Operator A
        MobileNumberPorting pendingDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        // Recipient is Operator A
        MobileNumberPorting pendingRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        // Other than OperatorA
        MobileNumberPorting pendingOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo
                .saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        pendingOtherOperatorMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting,
                        canceledOtherOperatorMobileNumberPorting));

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                get(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void itShouldReturnAllByDonorMobileOperatorAndPendingStatus()
            throws Exception {

        String requestUri = restEndpointUri + "/pending";

        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = operatorA.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        // Donor is Operator A
        MobileNumberPorting pendingDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        // Recipient is Operator A
        MobileNumberPorting pendingRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        // Other than OperatorA
        MobileNumberPorting pendingOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo
                .saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        pendingOtherOperatorMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting,
                        canceledOtherOperatorMobileNumberPorting));

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                get(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void itShouldReturnAllByRecipientMobileOperatorAndAcceptedStatus()
            throws Exception {

        String requestUri = restEndpointUri + "/accepted";

        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = operatorA.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        // Donor is Operator A
        MobileNumberPorting pendingDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        // Recipient is Operator A
        MobileNumberPorting pendingRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        // Other than OperatorA
        MobileNumberPorting pendingOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo
                .saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        pendingOtherOperatorMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting,
                        canceledOtherOperatorMobileNumberPorting));

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                get(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void itShouldReturnMobileNumberPortabilityByMobileNumber()
            throws Exception {

        String mobileNumber = "010110";
        String requestUri = restEndpointUri + "/" + mobileNumber;

        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = operatorA.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting pendingDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo.save(mobileSubscriber);

        mobileNumberPortingRepo
                .saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting));

        ResultActions mobileNumberPortingResultActions = mockMvc.perform(
                get(requestUri).header("organization", organizationHeader));

        mobileNumberPortingResultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

}
