package com.gtss.mnp_manager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gtss.mnp_manager.dtos.MobileOperatorCredentialDto;
import com.gtss.mnp_manager.dtos.MobileOperatorDto;
import com.gtss.mnp_manager.mappings.MobileOperatorCredentialMapper;
import com.gtss.mnp_manager.mappings.MobileOperatorMapper;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileOperatorCredential;
import com.gtss.mnp_manager.services.AuthenticationService;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Authentication Controller takes care of authenticating users using rest endpoints
 * 
 */
@Slf4j
@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MobileOperatorCredentialMapper mobileOperatorCredentialMapper;

    @Autowired
    private MobileOperatorMapper mobileOperatorMapper;


    /**
     * Authenticate user by credentials as request body
     */
    @PostMapping(path = "/login")
    public MobileOperatorDto authenticateOperatorByCredential(@RequestBody(
            required = true) MobileOperatorCredentialDto mobileOperatorCredentialDto) {

        MobileOperatorCredential mobileOperatorCredential =
                mobileOperatorCredentialMapper
                        .toModel(mobileOperatorCredentialDto);

        log.info(String.format(
                "Login request by credential for mobile operator \"%s\"",
                mobileOperatorCredential.getOperatorName()));
        MobileOperator mobileOperator = authenticationService
                .authenticateOperatorByCredential(mobileOperatorCredential);

        log.info(String.format(
                "Login request by credential for mobile operator \"%s\"",
                mobileOperatorCredential.getOperatorName()));
        return mobileOperatorMapper.toDto(mobileOperator);

    }

    /**
     * Authenticate user by request header containing "organization" key
     */
    @PostMapping
    public MobileOperatorDto authenticateOperatorByOrganizationHeader(
            @RequestHeader(name = "organization") String organizationHeader) {


        log.info(String.format("Login request by organization header"));
        MobileOperator mobileOperator = authenticationService
                .authenticateOperatorByOrganizationHeader(organizationHeader);

        log.info(String.format(
                "Login request by organization header for mobile operator \"%s\" succeeded",
                mobileOperator.getOperatorName()));
        return mobileOperatorMapper.toDto(mobileOperator);

    }

}
