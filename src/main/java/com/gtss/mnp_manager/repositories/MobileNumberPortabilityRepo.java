package com.gtss.mnp_manager.repositories;

import com.gtss.mnp_manager.models.MobileNumberPortability;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MobileNumberPortabilityRepo
        extends CrudRepository<MobileNumberPortability, Long> {

}
