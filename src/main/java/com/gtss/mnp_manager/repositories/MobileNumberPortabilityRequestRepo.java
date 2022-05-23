package com.gtss.mnp_manager.repositories;

import com.gtss.mnp_manager.models.MobileNumberPortabilityRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MobileNumberPortabilityRequestRepo
        extends CrudRepository<MobileNumberPortabilityRequest, Long> {

}
