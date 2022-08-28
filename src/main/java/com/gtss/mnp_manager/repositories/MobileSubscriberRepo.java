package com.gtss.mnp_manager.repositories;

import java.util.Optional;
import com.gtss.mnp_manager.models.MobileSubscriber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileSubscriberRepo
        extends CrudRepository<MobileSubscriber, Long> {

    /**
     * Get mobile subscriber by mobile number
     * 
     * @param mobileNumber
     */
    @Query("SELECT mobileSubscriber FROM MobileSubscriber mobileSubscriber "
            + "WHERE mobileSubscriber.mobileNumber = ?1")
    Optional<MobileSubscriber> findByMobileNumber(String mobileNumber);
}
