package com.gtss.mnp_manager.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import com.gtss.mnp_manager.models.PortingStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MobileNumberPortingRepoTest {

    @Autowired
    private MobileNumberPortingRepo underTest;

    @Autowired
    private MobileOperatorRepo mobileOperatorRepo;

    @Autowired
    private MobileSubscriberRepo mobileSubscriberRepo;


    @Test
    void itShouldFindAllByStatus() {

        String mobileNumber = "010110";
        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");

        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting pendingMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, LocalDateTime.now(),
                        PortingStatus.PENDING);

        MobileNumberPorting canceledMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, LocalDateTime.now(),
                        PortingStatus.CANCELED);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        underTest.saveAll(List.of(pendingMobileNumberPorting,
                canceledMobileNumberPorting));

        Iterable<MobileNumberPorting> iterableMobileNumberPorting =
                underTest.findAllByStatus(PortingStatus.PENDING);

        assertThat(iterableMobileNumberPorting)
                .containsOnlyOnce(pendingMobileNumberPorting)
                .doesNotContain(canceledMobileNumberPorting);
    }

    @Test
    void itShouldFindByMobileSubscriberAndStatus() {

        String mobileNumber = "010110";
        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");

        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting pendingMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, LocalDateTime.now(),
                        PortingStatus.PENDING);

        MobileNumberPorting canceledMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, donorOperator,
                        recipientOperator, LocalDateTime.now(),
                        PortingStatus.CANCELED);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        underTest.saveAll(List.of(pendingMobileNumberPorting,
                canceledMobileNumberPorting));

        Optional<MobileNumberPorting> optionalMobileNumberPorting =
                underTest.findByMobileSubscriberAndStatus(mobileSubscriber,
                        PortingStatus.PENDING);

        assertThat(optionalMobileNumberPorting).isPresent().get()
                .isEqualTo(pendingMobileNumberPorting)
                .isNotEqualTo(canceledMobileNumberPorting);

    }

    @Test
    void itShouldFindAllByMobileOperatorOrAcceptedStatus() {

        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

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

        underTest.saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting,
                acceptedDonorIsOperatorAMobileNumberPorting,
                canceledDonorIsOperatorAMobileNumberPorting,
                pendingRecipientIsOperatorAMobileNumberPorting,
                acceptedRecipientIsOperatorAMobileNumberPorting,
                canceledRecipientIsOperatorAMobileNumberPorting,
                pendingOtherOperatorMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting,
                canceledOtherOperatorMobileNumberPorting));

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                underTest.findAllByMobileOperatorOrAcceptedStatus(operatorA,
                        Pageable.unpaged());

        assertThat(pageableMobileNumberPorting)
                .containsOnlyOnce(pendingDonorIsOperatorAMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        canceledDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        canceledRecipientIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting)
                .doesNotContain(pendingOtherOperatorMobileNumberPorting,
                        canceledOtherOperatorMobileNumberPorting);

    }


    @Test
    void itShouldFindByMobileSubscriber() {

        String mobileNumber = "010110";
        MobileOperator donorOperator =
                new MobileOperator("OperatorA", "operatorA");

        MobileOperator recipientOperator =
                new MobileOperator("OperatorB", "operatorB");

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, donorOperator,
                        donorOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting mobileNumberPorting = new MobileNumberPorting(
                mobileSubscriber, donorOperator, recipientOperator,
                LocalDateTime.now(), PortingStatus.PENDING);

        mobileOperatorRepo.saveAll(List.of(donorOperator, recipientOperator));
        mobileSubscriberRepo.save(mobileSubscriber);

        underTest.save(mobileNumberPorting);

        Optional<MobileNumberPorting> optionalMobileNumberPorting = underTest
                .findTopByMobileSubscriberOrderByIdDesc(mobileSubscriber);

        assertThat(optionalMobileNumberPorting).isPresent().get()
                .isEqualTo(mobileNumberPorting);

    }

    @Test
    void itShouldFindTopByMobileSubscriber() {
        String mobileNumber = "010110";

        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriber otherMobileSubscriber = new MobileSubscriber("00010");

        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, operatorA,
                        operatorA);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        MobileNumberPorting pendingMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        MobileNumberPorting canceledMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorA, operatorB,
                        LocalDateTime.now(), PortingStatus.CANCELED);

        MobileNumberPorting pendingOtherMobileNumberPorting =
                new MobileNumberPorting(otherMobileSubscriber, operatorA,
                        operatorB, LocalDateTime.now(), PortingStatus.PENDING);



        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo
                .saveAll(List.of(mobileSubscriber, otherMobileSubscriber));

        underTest.saveAll(List.of(pendingMobileNumberPorting,
                acceptedMobileNumberPorting, canceledMobileNumberPorting,
                pendingOtherMobileNumberPorting));

        Optional<MobileNumberPorting> optionalMobileNumberPorting = underTest
                .findTopByMobileSubscriberOrderByIdDesc(mobileSubscriber);

        assertThat(optionalMobileNumberPorting).isPresent();
        assertThat(optionalMobileNumberPorting.get())
                .isEqualTo(canceledMobileNumberPorting);
    }

    @Test
    void itShouldFindAllByDonorMobileOperatorAndPendingStatus() {
        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

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

        // Recipient is Operator A
        MobileNumberPorting pendingRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        // Other than OperatorA
        MobileNumberPorting pendingOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);

        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo.save(mobileSubscriber);

        underTest.saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting,
                acceptedDonorIsOperatorAMobileNumberPorting,
                pendingRecipientIsOperatorAMobileNumberPorting,
                acceptedRecipientIsOperatorAMobileNumberPorting,
                pendingOtherOperatorMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting));

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                underTest.findAllByDonorMobileOperatorAndPendingStatus(
                        operatorA, Pageable.unpaged());

        assertThat(pageableMobileNumberPorting)
                .containsOnlyOnce(pendingDonorIsOperatorAMobileNumberPorting)
                .doesNotContain(pendingOtherOperatorMobileNumberPorting,
                        pendingOtherOperatorMobileNumberPorting,
                        acceptedDonorIsOperatorAMobileNumberPorting,
                        acceptedRecipientIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting);

    }

    @Test
    void itShouldFindAllByRecipientMobileOperatorAndAcceptedStatus() {

        String mobileNumber = "010110";
        MobileOperator operatorA = new MobileOperator("OperatorA", "operatorA");

        MobileOperator operatorB = new MobileOperator("OperatorB", "operatorB");

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

        // Recipient is Operator A
        MobileNumberPorting pendingRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedRecipientIsOperatorAMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorA,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);


        // Other than OperatorA
        MobileNumberPorting pendingOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.PENDING);

        MobileNumberPorting acceptedOtherOperatorMobileNumberPorting =
                new MobileNumberPorting(mobileSubscriber, operatorB, operatorB,
                        LocalDateTime.now(), PortingStatus.ACCEPTED);


        mobileOperatorRepo.saveAll(List.of(operatorA, operatorB));
        mobileSubscriberRepo.save(mobileSubscriber);

        underTest.saveAll(List.of(pendingDonorIsOperatorAMobileNumberPorting,
                acceptedDonorIsOperatorAMobileNumberPorting,
                pendingRecipientIsOperatorAMobileNumberPorting,
                acceptedRecipientIsOperatorAMobileNumberPorting,
                pendingOtherOperatorMobileNumberPorting,
                acceptedOtherOperatorMobileNumberPorting));

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                underTest.findAllByRecipientMobileOperatorAndAcceptedStatus(
                        operatorB, Pageable.unpaged());

        assertThat(pageableMobileNumberPorting.getContent())
                .containsOnlyOnce(acceptedDonorIsOperatorAMobileNumberPorting,
                        acceptedOtherOperatorMobileNumberPorting)
                .doesNotContain(acceptedRecipientIsOperatorAMobileNumberPorting,
                        pendingDonorIsOperatorAMobileNumberPorting,
                        pendingRecipientIsOperatorAMobileNumberPorting,
                        pendingOtherOperatorMobileNumberPorting);

    }
}
