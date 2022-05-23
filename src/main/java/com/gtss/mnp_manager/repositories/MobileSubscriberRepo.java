package com.gtss.mnp_manager.repositories;

import com.gtss.mnp_manager.models.MobileSubscriber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileSubscriberRepo
        extends CrudRepository<MobileSubscriber, Long> {

}
