package com.gtss.mnp_manager.services;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.gtss.mnp_manager.exceptions.BadRequestException;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileOperatorCredential;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import lombok.extern.slf4j.Slf4j;

/**
 * Authentication Service
 * BUsiness logic for Authentication Controller
 */
@Slf4j
@Service
public class AuthenticationService {

    private final MobileOperatorRepo mobileOperatorRepo;

    public AuthenticationService(MobileOperatorRepo mobileOperatorRepo) {
        this.mobileOperatorRepo = mobileOperatorRepo;
    }

    /**
     * Authneticate Operator by credential
     * @param mobileOperatorCredential
     */
    public MobileOperator authenticateOperatorByCredential(
            MobileOperatorCredential mobileOperatorCredential) {

        Optional<MobileOperator> optionalMobileOperator = mobileOperatorRepo
                .findByOperatorName(mobileOperatorCredential.getOperatorName());

        if (!optionalMobileOperator.isPresent()) {
            log.info(String.format(
                    "Mobile operator \"%s\" failed to authenticated by credential",
                    mobileOperatorCredential.getOperatorName()));
            throw new BadRequestException("Operator not found.");
        }

        log.info(String.format("Mobile operator \"%s\" authenticated by credential",
                mobileOperatorCredential.getOperatorName()));
        return optionalMobileOperator.get();
    }

    /**
     * Authenticate Operator by organization header
     * @param organizationHeader
     * @return
     */
    public MobileOperator authenticateOperatorByOrganizationHeader(
            String organizationHeader) {

        Optional<MobileOperator> optionalMobileOperator =
                mobileOperatorRepo.findByOrganizationHeader(organizationHeader);

        if (!optionalMobileOperator.isPresent()) {
            log.info(String.format(
                    "Unknown Mobile operator failed authenticated by organization header"));
            throw new BadRequestException("Operator not found.");
        }

        MobileOperator mobileOperator = optionalMobileOperator.get();
        log.info(String.format(
                "Mobile operator \"%s\" authenticated by organization header",
                mobileOperator.getOperatorName()));
        return mobileOperator;
    }

}
