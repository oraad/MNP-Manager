package com.gtss.mnp_manager.repositories;

import java.util.Optional;
import com.gtss.mnp_manager.models.MobileOperator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileOperatorRepo
        extends CrudRepository<MobileOperator, Short> {

    @Query("SELECT mobileOperator FROM MobileOperator mobileOperator WHERE organizationHeader = ?1")
    Optional<MobileOperator> findMobileOperatorByOrganizationHeader(
            String organizationHeader);
}
