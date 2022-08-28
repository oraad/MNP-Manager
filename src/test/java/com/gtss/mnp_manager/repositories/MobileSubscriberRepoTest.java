package com.gtss.mnp_manager.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.MobileSubscriberOperator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MobileSubscriberRepoTest {

    @Autowired
    private MobileSubscriberRepo underTest;

    @Autowired
    private MobileOperatorRepo mobileOperatorRepo;

    @Test
    void itShouldFindByMobileNumber() {

        String mobileNumber = "01001010";
        MobileOperator mobileOperator =
                new MobileOperator("OperatorA", "operatorA");

        MobileSubscriber mobileSubscriber = new MobileSubscriber(mobileNumber);
        MobileSubscriberOperator mobileSubscriberOperator =
                new MobileSubscriberOperator(mobileSubscriber, mobileOperator,
                        mobileOperator);
        mobileSubscriber.setMobileSubscriberOperator(mobileSubscriberOperator);

        mobileOperatorRepo.save(mobileOperator);
        underTest.save(mobileSubscriber);

        Optional<MobileSubscriber> optionalMobileSubscriber =
                underTest.findByMobileNumber(mobileNumber);

        assertThat(optionalMobileSubscriber).isPresent()
                .hasValueSatisfying(c -> {
                    assertThat(c).usingRecursiveComparison()
                            .isEqualTo(mobileSubscriber);
                });
    }
}
