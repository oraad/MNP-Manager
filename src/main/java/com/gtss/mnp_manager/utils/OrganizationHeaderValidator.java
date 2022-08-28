package com.gtss.mnp_manager.utils;

import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import org.springframework.stereotype.Component;

/**
 * Organization header vaildator
 */
@Component
public class OrganizationHeaderValidator {

    private final MobileOperatorRepo mobileOperatorRepo;

    public OrganizationHeaderValidator(MobileOperatorRepo mobileOperatorRepo) {

        this.mobileOperatorRepo = mobileOperatorRepo;
    }

    /**
     * Check whether organization header is provied or not
     * @param organizationHeader
     */
    public Boolean isHeaderExists(String organizationHeader) {

        return organizationHeader != null
                && !organizationHeader.trim().isEmpty();
    }

    /**
     * Validate organization header is for an existing mobile operator
     * @param organizationHeader
     */
    public Boolean validate(String organizationHeader) {

        return mobileOperatorRepo
                .findByOrganizationHeader(organizationHeader)
                .isPresent();
    }

}
