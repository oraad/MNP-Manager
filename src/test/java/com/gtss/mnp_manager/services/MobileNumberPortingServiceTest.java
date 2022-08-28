package com.gtss.mnp_manager.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.gtss.mnp_manager.exceptions.BadRequestException;
import com.gtss.mnp_manager.exceptions.ForbiddenException;
import com.gtss.mnp_manager.exceptions.NotFoundException;
import com.gtss.mnp_manager.exceptions.UnauthorizedException;
import com.gtss.mnp_manager.mappings.MobileNumberPortingMapper;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import com.gtss.mnp_manager.models.PortingStatus;
import com.gtss.mnp_manager.repositories.MobileNumberPortingRepo;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import com.gtss.mnp_manager.repositories.MobileSubscriberRepo;
import com.gtss.mnp_manager.utils.OrganizationHeaderValidator;
import com.gtss.mnp_manager.utils.TemporalComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@SpringBootTest
public class MobileNumberPortingServiceTest {

    @Autowired
    private MobileNumberPortingService underTest;

    @Autowired
    private MobileNumberPortingMapper mobileNumberPortingMapper;

    @Mock
    private MobileNumberPortingRepo mobileNumberPortingRepo;

    @Mock
    private MobileOperatorRepo mobileOperatorRepo;

    @Mock
    private MobileSubscriberRepo mobileSubscriberRepo;

    @Mock
    private OrganizationHeaderValidator organizationHeaderValidator;

    @Captor
    private ArgumentCaptor<MobileNumberPorting> mobileNumberPortingArgumentCaptor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        underTest = new MobileNumberPortingService(mobileNumberPortingRepo,
                mobileOperatorRepo, mobileSubscriberRepo,
                mobileNumberPortingMapper, organizationHeaderValidator);
    }

    @Test
    void itShouldCreateMobileNumberPorting() {

        String mobileNumber = "010000101";
        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting mobileNumberPorting = new MobileNumberPorting(
                mobileSubscriber, donorOperator, recipientOperator,
                LocalDateTime.now(), PortingStatus.PENDING);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                recipientOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(recipientOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        underTest.createMobileNumberPorting(organizationHeader, mobileNumber);

        then(mobileNumberPortingRepo).should()
                .save(mobileNumberPortingArgumentCaptor.capture());
        MobileNumberPorting mobileNumberPortingArgumentValue =
                mobileNumberPortingArgumentCaptor.getValue();

        assertThat(mobileNumberPortingArgumentValue)
                .usingComparatorForType(TemporalComparator.isCloseTo(2),
                        Temporal.class)
                .usingRecursiveComparison().isEqualTo(mobileNumberPorting);
    }

    @Test
    void itShouldThrowUnauthorizedExceptionWhenCreatingNewMobileNumberPortingForUnauthorizedOrganizationHeader() {
        String mobileNumber = "010000101";
        String organizationHeader = "operatorA";

        Throwable thrown = catchThrowable(() -> {
            underTest.createMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(thrown).isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Organization header is not valid.");
    }

    @Test
    void itShouldThrowNotFoundWhenCreatingNewMobileNumberPortingForInvalidMobileNumber() {

        String mobileNumber = "010000101";

        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                recipientOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(recipientOperator));

        Throwable thrown = catchThrowable(() -> {
            underTest.createMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Mobile number not found.");
    }

    @Test
    void itShouldThrowBadRequestWhenCreatingNewMobileNumberPortingWhereRecipientisDonor() {

        String mobileNumber = "010000101";
        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator = donorOperator;

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                recipientOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(recipientOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        Throwable thrown = catchThrowable(() -> {
            underTest.createMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(thrown).isInstanceOf(BadRequestException.class)
                .hasMessageContaining(
                        "Mobile number belongs to recipient operator.");
    }

    @Test
    void itShouldThrowBadRequestWhenCreatingNewMobileNumberPortingForPendingRequest() {

        String mobileNumber = "010000101";
        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting mobileNumberPorting = new MobileNumberPorting(
                mobileSubscriber, donorOperator, recipientOperator,
                LocalDateTime.now(), PortingStatus.PENDING);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                recipientOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(recipientOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        given(mobileNumberPortingRepo.findByMobileSubscriberAndStatus(
                mobileSubscriber, PortingStatus.PENDING))
                        .willReturn(Optional.of(mobileNumberPorting));

        Throwable duplicatedThrown = catchThrowable(() -> {
            underTest.createMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(duplicatedThrown).isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Pending mobile number porting exists.");

    }

    @Test
    void itShouldAcceptMobileNumberPorting() {

        String mobileNumber = "01011000";

        LocalDateTime now = LocalDateTime.now();

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = donorOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, PortingStatus.PENDING);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                donorOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(donorOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        given(mobileNumberPortingRepo.findByMobileSubscriberAndStatus(
                mobileSubscriber, PortingStatus.PENDING))
                        .willReturn(Optional.of(mobileNumberPorting));

        underTest.acceptMobileNumberPorting(organizationHeader, mobileNumber);

        then(mobileNumberPortingRepo).should()
                .save(mobileNumberPortingArgumentCaptor.capture());
        MobileNumberPorting mobileNumberPortingArgumentValue =
                mobileNumberPortingArgumentCaptor.getValue();

        assertThat(mobileNumberPortingArgumentValue)

                .extracting("updatedOn", "status").matches(fieldTuple -> {
                    LocalDateTime updatedOn = (LocalDateTime) fieldTuple.get(0);
                    PortingStatus status = (PortingStatus) fieldTuple.get(1);


                    boolean isUpdateOnCloseToNow = TemporalComparator
                            .isCloseTo(2).equals(updatedOn, now);
                    boolean isSameStatus =
                            status.equals(PortingStatus.ACCEPTED);

                    return isUpdateOnCloseToNow && isSameStatus;
                });
    }

    @Test
    void itShouldRejectMobileNumberPorting() {

        String mobileNumber = "01011000";

        LocalDateTime now = LocalDateTime.now();

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = donorOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting mobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, now, PortingStatus.PENDING);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                donorOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(donorOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        given(mobileNumberPortingRepo.findByMobileSubscriberAndStatus(
                mobileSubscriber, PortingStatus.PENDING))
                        .willReturn(Optional.of(mobileNumberPorting));

        underTest.rejectMobileNumberPorting(organizationHeader, mobileNumber);

        then(mobileNumberPortingRepo).should()
                .save(mobileNumberPortingArgumentCaptor.capture());
        MobileNumberPorting mobileNumberPortingArgumentValue =
                mobileNumberPortingArgumentCaptor.getValue();

        assertThat(mobileNumberPortingArgumentValue)
                .extracting("updatedOn", "status").matches(fieldTuple -> {
                    LocalDateTime updatedOn = (LocalDateTime) fieldTuple.get(0);
                    PortingStatus status = (PortingStatus) fieldTuple.get(1);


                    boolean isUpdateOnCloseToNow = TemporalComparator
                            .isCloseTo(2).equals(updatedOn, now);
                    boolean isSameStatus =
                            status.equals(PortingStatus.REJECTED);

                    return isUpdateOnCloseToNow && isSameStatus;
                });
    }

    @Test
    void itShouldThrowUnauthorizedExceptionWhenUsingInvalidOrganizationHeader() {

        String mobileNumber = "01011000";
        String organizationHeader = "operatorA";

        Throwable acceptingThrown = catchThrowable(() -> {
            underTest.acceptMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        Throwable rejectingThrown = catchThrowable(() -> {
            underTest.rejectMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(List.of(acceptingThrown, rejectingThrown))
                .allMatch(thrown -> thrown instanceof UnauthorizedException)
                .allMatch(thrown -> thrown.getMessage()
                        .contains("Organization header is not valid."));
    }

    @Test
    void itShouldThrowForbiddenExceptionWhenUsingWrongOrganizationHeader() {

        String mobileNumber = "01011000";

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");
        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = recipientOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                recipientOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(recipientOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        Throwable acceptingThrown = catchThrowable(() -> {
            underTest.acceptMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        Throwable rejectingThrown = catchThrowable(() -> {
            underTest.rejectMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(List.of(acceptingThrown, rejectingThrown))
                .allMatch(thrown -> thrown instanceof ForbiddenException)
                .allMatch(thrown -> thrown.getMessage().contains(
                        "Mobile operator is not mobile number donor"));
    }

    @Test
    void itShouldThrowNotFoundExceptionUsingInvalidMobileNumber() {

        String mobileNumber = "01011000";

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");

        String organizationHeader = donorOperator.getOrganizationHeader();

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                donorOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(donorOperator));

        Throwable acceptingThrown = catchThrowable(() -> {
            underTest.acceptMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        Throwable rejectingThrown = catchThrowable(() -> {
            underTest.rejectMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(List.of(acceptingThrown, rejectingThrown))
                .allMatch(thrown -> thrown instanceof NotFoundException)
                .allMatch(thrown -> thrown.getMessage()
                        .contains("Mobile number not found."));
    }

    @Test
    void itShouldThrowNotFoundExceptionForNonPendingRequest() {

        String mobileNumber = "01011000";

        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");

        String organizationHeader = donorOperator.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(1L,
                mobileNumber, new MobileSubscriberOperator());
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(1L, mobileSubscriber,
                        donorOperator, donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);

        given(mobileOperatorRepo.findByOrganizationHeader(
                donorOperator.getOrganizationHeader()))
                        .willReturn(Optional.of(donorOperator));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        given(mobileNumberPortingRepo.findByMobileSubscriberAndStatus(
                mobileSubscriber, PortingStatus.PENDING))
                        .willReturn(Optional.empty());


        Throwable acceptingThrown = catchThrowable(() -> {
            underTest.acceptMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        Throwable rejectingThrown = catchThrowable(() -> {
            underTest.rejectMobileNumberPorting(organizationHeader,
                    mobileNumber);
        });

        assertThat(List.of(acceptingThrown, rejectingThrown))
                .allMatch(thrown -> thrown instanceof NotFoundException)
                .allMatch(thrown -> thrown.getMessage()
                        .contains("No pending request for the mobile number."));

    }

    @Test
    void itShouldThrowUnauthorizedExceptionWhenGettingAllMobileNumberPortingByMobileOperatorForInvalidOrganizationHeader() {

        int pageNumber = 0;
        int pageSize = 2;

        String organizationHeader = "operatorA";

        Throwable thrown = catchThrowable(() -> {
            underTest.getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
                    organizationHeader, pageNumber, pageSize, null);
        });

        assertThat(thrown).isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Organization header is not valid.");
    }

    @Test
    void itShouldReturnMobileNumberPortingByMobileNumber() {

        int pageNumber = 0;
        int pageSize = 10;

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
        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);


        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);
        given(mobileOperatorRepo.findByOrganizationHeader(organizationHeader))
                .willReturn(Optional.of(operatorA));

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings,
                        pageable, 100);

        given(mobileNumberPortingRepo
                .findAllByMobileOperatorOrAcceptedStatus(operatorA, pageable))
                        .willReturn(pageableMobileNumberPorting);

        Page<MobileNumberPorting> results = underTest
                .getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
                        organizationHeader, pageNumber, pageSize, List.of());

        assertThat(results).containsOnlyOnce(
                pendingDonorIsOperatorAMobileNumberPorting,
                acceptedDonorIsOperatorAMobileNumberPorting,
                canceledDonorIsOperatorAMobileNumberPorting,
                pendingRecipientIsOperatorAMobileNumberPorting,
                acceptedRecipientIsOperatorAMobileNumberPorting,
                canceledRecipientIsOperatorAMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting);
    }

    @Test
    void itShouldReturnSortedMobileNumberPortingByMobileNumber() {

        int pageNumber = 0;
        int pageSize = 10;

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
                new MobileNumberPorting(1L, mobileSubscriber, operatorA,
                        operatorB, LocalDateTime.now(), null,
                        PortingStatus.PENDING);

        MobileNumberPorting acceptedDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(2L, mobileSubscriber, operatorA,
                        operatorB, LocalDateTime.now(), null,
                        PortingStatus.ACCEPTED);

        MobileNumberPorting canceledDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(3L, mobileSubscriber, operatorA,
                        operatorB, LocalDateTime.now(), null,
                        PortingStatus.CANCELED);

        // Recipient is Operator A
        MobileNumberPorting pendingRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(4L, mobileSubscriber, operatorB,
                        operatorA, LocalDateTime.now(), null,
                        PortingStatus.PENDING);

        MobileNumberPorting acceptedRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(5L, mobileSubscriber, operatorB,
                        operatorA, LocalDateTime.now(), null,
                        PortingStatus.ACCEPTED);

        MobileNumberPorting canceledRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(6L, mobileSubscriber, operatorB,
                        operatorA, LocalDateTime.now(), null,
                        PortingStatus.CANCELED);

        // Other than OperatorA
        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(7L, mobileSubscriber, operatorB,
                        operatorB, LocalDateTime.now(), null,
                        PortingStatus.ACCEPTED);


        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);
        given(mobileOperatorRepo.findByOrganizationHeader(organizationHeader))
                .willReturn(Optional.of(operatorA));

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting);

        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(Direction.ASC, "id"));

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings,
                        pageable, 100);

        given(mobileNumberPortingRepo
                .findAllByMobileOperatorOrAcceptedStatus(operatorA, pageable))
                        .willReturn(pageableMobileNumberPorting);

        String[] columnSort = {"id", "asc"};
        List<String[]> sort = new ArrayList<String[]>();
        sort.add(columnSort);

        Page<MobileNumberPorting> results = underTest
                .getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
                        organizationHeader, pageNumber, pageSize, sort);

        assertThat(results).containsOnlyOnce(
                pendingDonorIsOperatorAMobileNumberPorting,
                acceptedDonorIsOperatorAMobileNumberPorting,
                canceledDonorIsOperatorAMobileNumberPorting,
                pendingRecipientIsOperatorAMobileNumberPorting,
                acceptedRecipientIsOperatorAMobileNumberPorting,
                canceledRecipientIsOperatorAMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting);
    }

    @ParameterizedTest
    @CsvSource({"0,2", "2,3", "0,0", "10,0"})
    void itShouldReturnPagedMobileNumberPortingByMobileOperatorOrAcceptedStatus(
            int pageNumber, int pageSize) {

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
        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);


        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);
        given(mobileOperatorRepo.findByOrganizationHeader(organizationHeader))
                .willReturn(Optional.of(operatorA));

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting);


        Pageable pageable = pageSize > 0 ? PageRequest.of(pageNumber, pageSize)
                : Pageable.unpaged();

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings,
                        pageable, 100);

        given(mobileNumberPortingRepo
                .findAllByMobileOperatorOrAcceptedStatus(operatorA, pageable))
                        .willReturn(pageableMobileNumberPorting);

        Page<MobileNumberPorting> results = underTest
                .getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
                        organizationHeader, pageNumber, pageSize,
                        new ArrayList<>());

        assertThat(results.getSize())
                .isEqualTo(pageableMobileNumberPorting.getSize());
    }

    @Test
    void itShouldReturnLastMobileNumberPotingByMobileNumber() {

        String mobileNumber = "01110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = operatorA.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting canceledDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);


        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);
        given(mobileOperatorRepo.findByOrganizationHeader(organizationHeader))
                .willReturn(Optional.of(operatorA));

        given(mobileSubscriberRepo.findByMobileNumber(mobileNumber))
                .willReturn(Optional.of(mobileSubscriber));

        given(mobileNumberPortingRepo
                .findTopByMobileSubscriberOrderByIdDesc(mobileSubscriber))
                        .willReturn(Optional.of(
                                canceledDonorIsOperatorAMobileNumberPorting));

        MobileNumberPorting results =
                underTest.getMobileNumberPortingByMobileNumber(
                        organizationHeader, mobileNumber);

        assertThat(results)
                .isEqualTo(canceledDonorIsOperatorAMobileNumberPorting);

        given(mobileNumberPortingRepo
                .findTopByMobileSubscriberOrderByIdDesc(mobileSubscriber))
                        .willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            underTest.getMobileNumberPortingByMobileNumber(organizationHeader,
                    mobileNumber);
        });

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("No mobile number porting found.");
    }

    @Test
    void itShouldProcessSortParameter() {

        String[] sort0 = {};
        String[] sort1 = {"id", "asc"};
        String[] sort2 = {"id,asc"};
        String[] sort3 = {"id, asc"};
        String[] sort4 = {"id,asc", "name,desc"};

        List<String[]> processedSort0 = underTest.processSortParameter(sort0);
        assertThat(processedSort0).hasSize(0);

        List<String[]> processedSort1 = underTest.processSortParameter(sort1);
        assertThat(processedSort1).hasSize(1);
        assertThat(processedSort1).contains(new String[] {"id", "asc"});

        List<String[]> processedSort2 = underTest.processSortParameter(sort2);
        assertThat(processedSort2).hasSize(1);
        assertThat(processedSort2).contains(new String[] {"id", "asc"});

        List<String[]> processedSort3 = underTest.processSortParameter(sort3);
        assertThat(processedSort3).hasSize(1);
        assertThat(processedSort3).contains(new String[] {"id", "asc"});

        List<String[]> processedSort4 = underTest.processSortParameter(sort4);
        assertThat(processedSort4).hasSize(2);
        assertThat(processedSort4).contains(new String[] {"id", "asc"},
                new String[] {"name", "desc"});
    }

    @Test
    void itShouldReturnMobileNumberPortingByDonorMobileOperatorAndPendingStatus() {

        int pageNumber = 0;
        int pageSize = 10;

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

        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);
        given(mobileOperatorRepo.findByOrganizationHeader(organizationHeader))
                .willReturn(Optional.of(operatorA));

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(pendingDonorIsOperatorAMobileNumberPorting);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings,
                        pageable, 100);

        given(mobileNumberPortingRepo
                .findAllByDonorMobileOperatorAndPendingStatus(operatorA, pageable))
                        .willReturn(pageableMobileNumberPorting);

        Page<MobileNumberPorting> results = underTest
                .getAllMobileNumberPortingByDonorMobileOperatorAndPendingStatus(
                        organizationHeader, pageNumber, pageSize, List.of());

        assertThat(results).containsOnlyOnce(
                pendingDonorIsOperatorAMobileNumberPorting);

        // Unpageable

        Page<MobileNumberPorting> unpageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings);

        given(mobileNumberPortingRepo
                .findAllByDonorMobileOperatorAndPendingStatus(operatorA, Pageable.unpaged()))
                        .willReturn(unpageableMobileNumberPorting);

        Page<MobileNumberPorting> unpagedResults = underTest
                .getAllMobileNumberPortingByDonorMobileOperatorAndPendingStatus(
                        organizationHeader, pageNumber, 0, List.of());

        assertThat(unpagedResults).containsOnlyOnce(
                pendingDonorIsOperatorAMobileNumberPorting);
    }

    @Test
    void itShouldReturnMobileNumberPortingByRecipientMobileOperatorAndAcceptedStatus() {

        int pageNumber = 0;
        int pageSize = 10;

        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        String organizationHeader = operatorA.getOrganizationHeader();

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting acceptedDonorIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);


        given(organizationHeaderValidator.validate(organizationHeader))
                .willReturn(true);
        given(mobileOperatorRepo.findByOrganizationHeader(organizationHeader))
                .willReturn(Optional.of(operatorA));

        List<MobileNumberPorting> mobileNumberPortings =
                List.of(acceptedDonorIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings,
                        pageable, 100);

        given(mobileNumberPortingRepo
                .findAllByRecipientMobileOperatorAndAcceptedStatus(operatorA,
                        pageable)).willReturn(pageableMobileNumberPorting);

        Page<MobileNumberPorting> results = underTest
                .getAllMobileNumberPortingByRecipientMobileOperatorAndAcceptedStatus(
                        organizationHeader, pageNumber, pageSize, List.of());

        assertThat(results).containsOnlyOnce(
                acceptedDonorIsOperatorAMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting);

        // Unpaged

        Page<MobileNumberPorting> unpageableMobileNumberPorting =
                new PageImpl<MobileNumberPorting>(mobileNumberPortings);

        given(mobileNumberPortingRepo
                .findAllByRecipientMobileOperatorAndAcceptedStatus(operatorA,
                        Pageable.unpaged())).willReturn(unpageableMobileNumberPorting);

        Page<MobileNumberPorting> unpagedResults = underTest
                .getAllMobileNumberPortingByRecipientMobileOperatorAndAcceptedStatus(
                        organizationHeader, pageNumber, 0, List.of());

        assertThat(unpagedResults).containsOnlyOnce(
                acceptedDonorIsOperatorAMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting);
    }
}
