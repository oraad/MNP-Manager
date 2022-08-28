package com.gtss.mnp_manager.repositories;

import java.util.Optional;
import com.gtss.mnp_manager.models.MobileOperator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileOperatorRepo
        extends CrudRepository<MobileOperator, Short> {
    /**
     * Get Mobile operator by organization header
     * 
     * @param organizationHeader
     */
    @Query("SELECT mobileOperator FROM MobileOperator mobileOperator "
            + "WHERE mobileOperator.organizationHeader = ?1")
    Optional<MobileOperator> findByOrganizationHeader(
            String organizationHeader);

    /**
     * Get Mobile operator by operator name
     * 
     * @param operatorName
     */
    @Query("SELECT mobileOperator FROM MobileOperator mobileOperator "
            + "WHERE mobileOperator.operatorName = ?1")
    Optional<MobileOperator> findByOperatorName(String operatorName);
}
