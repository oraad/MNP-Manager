package com.gtss.mnp_manager.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.gtss.mnp_manager.dtos.MobileNumberPortingDto;
import com.gtss.mnp_manager.dtos.MobileNumberPortingWithActionsDto;
import com.gtss.mnp_manager.mappings.MobileNumberPortingMapper;
import com.gtss.mnp_manager.mappings.MobileNumberPortingWithActionsMapper;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.services.MobileNumberPortingService;

/**
 * 
 * Mobile Number Porting Controller handles mobile number porting requests using rest endpoints.
 * This controller is processed after the request pass through the OrganizationHeaderAuthenticationFilter,
 * the filter should be using the same mapping path.
 * The organization header is validated by OrganizationHeaderAuthenticationFilter
 * 
 */
@RestController
@RequestMapping(path = "api/v1/mnp")
public class MobileNumberPortingController {

    @Autowired
    private MobileNumberPortingService mobileNumberPortingService;

    @Autowired
    private MobileNumberPortingMapper mobileNumberPortingMapper;

    @Autowired
    private MobileNumberPortingWithActionsMapper mobileNumberPortingWithActionsMapper;

    /**
     * Rest endpoint to get all 'mobile number porting' matching 'mobile operator' as donor or recipient
     * or requests with 'Accepted' status
     */
    @GetMapping
    public Page<MobileNumberPortingDto> getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
            @RequestHeader(name = "organization") String organizationHeader,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "-1") int pageSize,
            @RequestParam(value = "sort",
                    defaultValue = "id,asc") String[] sort) {

        List<String[]> processedSort =
                mobileNumberPortingService.processSortParameter(sort);

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                mobileNumberPortingService
                        .getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
                                organizationHeader, pageNumber, pageSize,
                                processedSort);

        Page<MobileNumberPortingDto> pageableMobileNumberPortingDto =
                pageableMobileNumberPorting
                        .map(mobileNumberPortingMapper::toDto);

        return pageableMobileNumberPortingDto;
    }

    /**
     * Rest endpoint to get all pending 'mobile number porting' matching 'mobile operator' as donor 
     * and has 'Pending' status
     */
    @GetMapping(path = "/pending")
    public Page<MobileNumberPortingWithActionsDto> getAllMobileNumberPortingByDonorMobileOperatorAndPendingStatus(
            @RequestHeader(name = "organization") String organizationHeader,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "-1") int pageSize,
            @RequestParam(value = "sort",
                    defaultValue = "createdOn,desc") String[] sort) {

        List<String[]> processedSort =
                mobileNumberPortingService.processSortParameter(sort);
        Page<MobileNumberPorting> pageableMobileNumberPorting =
                mobileNumberPortingService
                        .getAllMobileNumberPortingByDonorMobileOperatorAndPendingStatus(
                                organizationHeader, pageNumber, pageSize,
                                processedSort);

        Page<MobileNumberPortingWithActionsDto> pageableMobileNumberPortingDto =
                pageableMobileNumberPorting
                        .map(mobileNumberPortingWithActionsMapper::toDto);

        pageableMobileNumberPortingDto.forEach(mobileNumberPortingDto -> {
            mobileNumberPortingDto.generateActions("mnp");
        });

        return pageableMobileNumberPortingDto;
    }

    /**
     * Rest endpoint to get all accepted 'mobile number porting' matching 'mobile operator' as recipient 
     * and has 'Accepted' status
     */
    @GetMapping(path = "/accepted")
    public Page<MobileNumberPortingDto> getAllMobileNumberPortingByRecipientMobileOperatorAndAcceptedStatus(
            @RequestHeader(name = "organization") String organizationHeader,
            @RequestParam(value = "page", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "-1") int pageSize,
            @RequestParam(value = "sort",
                    defaultValue = "id,asc") String[] sort) {

        List<String[]> processedSort =
                mobileNumberPortingService.processSortParameter(sort);

        Page<MobileNumberPorting> pageableMobileNumberPorting =
                mobileNumberPortingService
                        .getAllMobileNumberPortingByRecipientMobileOperatorAndAcceptedStatus(
                                organizationHeader, pageNumber, pageSize,
                                processedSort);

        Page<MobileNumberPortingDto> pageableMobileNumberPortingDto =
                pageableMobileNumberPorting
                        .map(mobileNumberPortingMapper::toDto);

        return pageableMobileNumberPortingDto;
    }

    /**
     * Rest endpoint to get latest 'mobile number' info
     */
    @GetMapping(path = "/{mobileNumber}")
    public MobileNumberPortingDto getMobileNumberPorting(
            @RequestHeader(name = "organization") String organizationHeader,
            @PathVariable(value = "mobileNumber") String mobileNumber) {

        MobileNumberPorting mobileNumberPorting =
                mobileNumberPortingService.getMobileNumberPortingByMobileNumber(
                        organizationHeader, mobileNumber);

        MobileNumberPortingDto mobileNumberPortingDto =
                mobileNumberPortingMapper.toDto(mobileNumberPorting);

        return mobileNumberPortingDto;
    }

    /**
     * Rest endpoint to create new 'mobile number porting' request
     * using the authenticated operator as recipient
     */
    @PostMapping(path = "/{mobileNumber}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createMobileNumberPorting(
            @RequestHeader(name = "organization") String organizationHeader,
            @PathVariable(value = "mobileNumber") String mobileNumber) {
        mobileNumberPortingService.createMobileNumberPorting(organizationHeader,
                mobileNumber);
    }

    /**
     * Rest endpoint to accept pending 'mobile number porting'
     * used by donor 'mobile operator'
     */
    @PostMapping(path = "/{mobileNumber}/accept")
    public void acceptMobileNumberPorting(
            @RequestHeader(name = "organization") String organizationHeader,
            @PathVariable(value = "mobileNumber") String mobileNumber) {
        mobileNumberPortingService.acceptMobileNumberPorting(organizationHeader,
                mobileNumber);
    }

    /**
     * Rest endpoint to reject pending 'mobile number porting'
     * used by donor 'mobile operator'
     */
    @PostMapping(path = "/{mobileNumber}/reject")
    public void rejectMobileNumberPorting(
            @RequestHeader(name = "organization") String organizationHeader,
            @PathVariable(value = "mobileNumber") String mobileNumber) {
        mobileNumberPortingService.rejectMobileNumberPorting(organizationHeader,
                mobileNumber);
    }

}
