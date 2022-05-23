package com.gtss.mnp_manager.utils;

import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class OrganizationHeaderValidator {

    private final MobileOperatorRepo mobileOperatorRepo;

    @Autowired
    public OrganizationHeaderValidator(MobileOperatorRepo mobileOperatorRepo) {

        this.mobileOperatorRepo = mobileOperatorRepo;
    }

    public Boolean validate(String organizationHeader) {

        return mobileOperatorRepo
                .findMobileOperatorByOrganizationHeader(organizationHeader)
                .isPresent();
    }

}
