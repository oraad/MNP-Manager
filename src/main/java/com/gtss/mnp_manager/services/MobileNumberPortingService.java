package com.gtss.mnp_manager.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import com.gtss.mnp_manager.exceptions.BadRequestException;
import com.gtss.mnp_manager.exceptions.ForbiddenException;
import com.gtss.mnp_manager.exceptions.NotFoundException;
import com.gtss.mnp_manager.exceptions.UnauthorizedException;
import com.gtss.mnp_manager.mappings.MobileNumberPortingMapper;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.PortingStatus;
import com.gtss.mnp_manager.repositories.MobileNumberPortingRepo;
import com.gtss.mnp_manager.repositories.MobileOperatorRepo;
import com.gtss.mnp_manager.repositories.MobileSubscriberRepo;
import com.gtss.mnp_manager.utils.OrganizationHeaderValidator;

@Service
public class MobileNumberPortingService {

    private final MobileNumberPortingRepo mobileNumberPortingRepo;
    private final MobileOperatorRepo mobileOperatorRepo;
    private final MobileSubscriberRepo mobileSubscriberRepo;
    private final OrganizationHeaderValidator organizationHeaderValidator;
    private final MobileNumberPortingMapper mobileNumberPortingMapper;

    public MobileNumberPortingService(
            MobileNumberPortingRepo mobileNumberPortingRepo,
            MobileOperatorRepo mobileOperatorRepo,
            MobileSubscriberRepo mobileSubscriberRepo,
            MobileNumberPortingMapper mobileNumberPortingMapper,
            OrganizationHeaderValidator organizationHeaderValidator) {
        this.mobileNumberPortingRepo = mobileNumberPortingRepo;
        this.mobileOperatorRepo = mobileOperatorRepo;
        this.mobileSubscriberRepo = mobileSubscriberRepo;
        this.mobileNumberPortingMapper = mobileNumberPortingMapper;
        this.organizationHeaderValidator = organizationHeaderValidator;
    }

    /**
     * Get all mobile number porting by donor/recipient mobile operator or has accepted status
     * @param organizationHeader donor/recipient mobile operator
     * @param pageNumber paging number
     * @param pageSize paging size
     * @param sort paging sort
     * @return
     */
    public Page<MobileNumberPorting> getAllMobileNumberPortingByMobileOperatorOrAcceptedStatus(
            String organizationHeader, int pageNumber, int pageSize,
            List<String[]> sort) {

        MobileOperator mobileOperator = getMobileOperator(organizationHeader);

        List<Order> orders = getSortingOrder(sort);

        // if pageSize is less than zero, do not page results
        Pageable pageable = pageSize > 0
                ? PageRequest.of(pageNumber, pageSize, Sort.by(orders))
                : Pageable.unpaged();

        return mobileNumberPortingRepo.findAllByMobileOperatorOrAcceptedStatus(
                mobileOperator, pageable);
    }

    /**
     *  Get all mobile number porting by donor organization header and pending status
     * @param organizationHeader donor organization header
     * @param pageNumber paging number
     * @param pageSize paging size
     * @param sort paging sort
     * @return
     */
    public Page<MobileNumberPorting> getAllMobileNumberPortingByDonorMobileOperatorAndPendingStatus(
            String organizationHeader, int pageNumber, int pageSize,
            List<String[]> sort) {

        MobileOperator mobileOperator = getMobileOperator(organizationHeader);

        List<Order> orders = getSortingOrder(sort);

        // if pageSize is less than zero, do not page results
        Pageable pageable = pageSize > 0
                ? PageRequest.of(pageNumber, pageSize, Sort.by(orders))
                : Pageable.unpaged();

        return mobileNumberPortingRepo
                .findAllByDonorMobileOperatorAndPendingStatus(mobileOperator,
                        pageable);
    }

    /**
     * Get mobile number porting by recipient mobile operator and accepted status
     * @param organizationHeader recipient organization header
     * @param pageNumber page number
     * @param pageSize page size
     * @param sort paging sort
     * @return
     */
    public Page<MobileNumberPorting> getAllMobileNumberPortingByRecipientMobileOperatorAndAcceptedStatus(
            String organizationHeader, int pageNumber, int pageSize,
            List<String[]> sort) {

        MobileOperator mobileOperator = getMobileOperator(organizationHeader);

        List<Order> orders = getSortingOrder(sort);

        // if pageSize is less than zero, do not page results
        Pageable pageable = pageSize > 0
                ? PageRequest.of(pageNumber, pageSize, Sort.by(orders))
                : Pageable.unpaged();

        return mobileNumberPortingRepo
                .findAllByRecipientMobileOperatorAndAcceptedStatus(
                        mobileOperator, pageable);
    }

    /**
     * Get latest mobile number porting by mobile number
     * @param organizationHeader
     * @param mobileNumber
     */
    public MobileNumberPorting getMobileNumberPortingByMobileNumber(
            String organizationHeader, String mobileNumber) {

        checkOrganizationHeaderIsValid(organizationHeader);
        MobileSubscriber mobileSubscriber = getMobileSubscriber(mobileNumber);

        Optional<MobileNumberPorting> optionalMobileNumberPorting =
                mobileNumberPortingRepo.findTopByMobileSubscriberOrderByIdDesc(
                        mobileSubscriber);

        if (!optionalMobileNumberPorting.isPresent())
            throw new NotFoundException("No mobile number porting found.");

        return optionalMobileNumberPorting.get();
    }

    /**
     * Create new mobile number porting request using organization header and mobile number
     * @param organizationHeader recipient mobile operator
     * @param mobileNumber
     */
    public void createMobileNumberPorting(String organizationHeader,
            String mobileNumber) {

        MobileOperator recipientOperator =
                getMobileOperator(organizationHeader);
        MobileSubscriber mobileSubscriber = getMobileSubscriber(mobileNumber);

        throwBadRequestIfPendingMobileNumberPortingExists(mobileSubscriber);

        MobileOperator donorOperator = mobileSubscriber.getMobileOperator();

        throwBadRequestIfRecipientOperatorIsDonorOperator(recipientOperator,
                donorOperator);

        MobileNumberPorting mobileNumberPorting = new MobileNumberPorting(
                mobileSubscriber, donorOperator, recipientOperator,
                LocalDateTime.now(), PortingStatus.PENDING);

        mobileNumberPortingRepo.save(mobileNumberPorting);
    }

    /**
     * Accept mobile number porting using organization header and mobile number
     * @param organizationHeader expect donor mobile operator
     * @param mobileNumber
     */
    public void acceptMobileNumberPorting(String organizationHeader,
            String mobileNumber) {

        MobileOperator mobileOperator = getMobileOperator(organizationHeader);
        MobileSubscriber mobileSubscriber = getMobileSubscriber(mobileNumber);
        MobileOperator donorOperator = mobileSubscriber.getMobileOperator();

        throwForbiddenIfMobileOperatorIsNotDonorOperator(mobileOperator,
                donorOperator);

        MobileNumberPorting mobileNumberPorting =
                getPendingMobileNumberPorting(mobileSubscriber);

        LocalDateTime now = LocalDateTime.now();
        mobileNumberPorting.setStatus(PortingStatus.ACCEPTED);
        mobileNumberPorting.setUpdatedOn(now);

        MobileOperator recipientOperator =
                mobileNumberPorting.getRecipientMobileOperator();
        mobileSubscriber.setMobileOperator(recipientOperator);

        mobileNumberPortingRepo.save(mobileNumberPorting);
        mobileSubscriberRepo.save(mobileSubscriber);

    }

    /**
     * Reject mobile number porting using organization header and mobile number
     * @param organizationHeader expect donor mobile operator
     * @param mobileNumber
     */
    public void rejectMobileNumberPorting(String organizationHeader,
            String mobileNumber) {

        MobileOperator mobileOperator = getMobileOperator(organizationHeader);
        MobileSubscriber mobileSubscriber = getMobileSubscriber(mobileNumber);
        MobileOperator donorOperator = mobileSubscriber.getMobileOperator();

        throwForbiddenIfMobileOperatorIsNotDonorOperator(mobileOperator,
                donorOperator);

        MobileNumberPorting mobileNumberPorting =
                getPendingMobileNumberPorting(mobileSubscriber);

        LocalDateTime now = LocalDateTime.now();

        mobileNumberPorting.setStatus(PortingStatus.REJECTED);
        mobileNumberPorting.setUpdatedOn(now);

        mobileNumberPortingRepo.save(mobileNumberPorting);
    }

    /**
     * Process Sort paramter into list of [column, direction]
     * @param sort list of "column,direction"
     */
    public List<String[]> processSortParameter(String[] sort) {

        List<String[]> newSort = new ArrayList<String[]>();

        if (sort.length == 0)
            return newSort;

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                _sort[0] = _sort[0].trim();
                _sort[0] = mobileNumberPortingMapper
                        .fromJsonDtotoModelProperty(_sort[0]);
                _sort[1] = _sort[1].trim();
                newSort.add(_sort);
            }
        } else {
            // sort=[field, direction]
            String[] _sort = {mobileNumberPortingMapper
                    .fromJsonDtotoModelProperty(sort[0]), sort[1]};
            newSort.add(_sort);
        }

        return newSort;
    }

    /**
     * Throw error if organiation header is not valid
     * 
     * @param organizationHeader
     */
    private void checkOrganizationHeaderIsValid(String organizationHeader) {

        if (!organizationHeaderValidator.validate(organizationHeader))
            throw new UnauthorizedException(
                    "Organization header is not valid.");
    }

    /**
     * Get mobile operator using organization header
     * 
     * @param organizationHeader
     */
    private MobileOperator getMobileOperator(String organizationHeader) {

        checkOrganizationHeaderIsValid(organizationHeader);

        Optional<MobileOperator> optionalMobileOperator =
                mobileOperatorRepo.findByOrganizationHeader(organizationHeader);

        return optionalMobileOperator.get();
    }

    /**
     * Get mobile subscriber by mobile number
     * 
     * @param mobileNumber
     */
    private MobileSubscriber getMobileSubscriber(String mobileNumber) {
        Optional<MobileSubscriber> optionalMobileSubscriber =
                mobileSubscriberRepo.findByMobileNumber(mobileNumber);

        if (!optionalMobileSubscriber.isPresent())
            throw new NotFoundException("Mobile number not found.");

        return optionalMobileSubscriber.get();
    }

    /**
     * Throw BadRequest error if there is an existing pending mobile number
     * 
     * @param mobileSubscriber
     */
    private void throwBadRequestIfPendingMobileNumberPortingExists(
            MobileSubscriber mobileSubscriber) {
        Optional<MobileNumberPorting> optionalMobileNumberPorting =
                mobileNumberPortingRepo.findByMobileSubscriberAndStatus(
                        mobileSubscriber, PortingStatus.PENDING);

        if (optionalMobileNumberPorting.isPresent())
            throw new BadRequestException(
                    "Pending mobile number porting exists.");
    }

    /**
     * Throw BadRequest error if recipient is the same as donor
     * 
     * @param recipientOperator
     * @param donorOperator
     */
    private void throwBadRequestIfRecipientOperatorIsDonorOperator(
            MobileOperator recipientOperator, MobileOperator donorOperator) {
        if (recipientOperator.equals(donorOperator))
            throw new BadRequestException(
                    "Mobile number belongs to recipient operator.");
    }

    /**
     * Throw Forbidden error if mobile operator is not donor
     * 
     * @param mobileOperator mobile operator to test
     * @param donorOperator expected donor mobile operator
     */
    private void throwForbiddenIfMobileOperatorIsNotDonorOperator(
            MobileOperator mobileOperator, MobileOperator donorOperator) {
        if (!mobileOperator.equals(donorOperator)) {
            throw new ForbiddenException(
                    "Mobile operator is not mobile number donor.");
        }
    }

    /**
     * Get pending mobile number porting for mobile subscriber
     * 
     * @param mobileSubscriber
     * @return
     */
    private MobileNumberPorting getPendingMobileNumberPorting(
            MobileSubscriber mobileSubscriber) {

        Optional<MobileNumberPorting> optionalMobileNumberPorting =
                mobileNumberPortingRepo.findByMobileSubscriberAndStatus(
                        mobileSubscriber, PortingStatus.PENDING);

        if (!optionalMobileNumberPorting.isPresent())
            throw new NotFoundException(
                    "No pending request for the mobile number.");

        return optionalMobileNumberPorting.get();
    }

    /**
     * Convert sorting list into order list
     * 
     * @param sort "column,direction" list
     */
    private List<Order> getSortingOrder(List<String[]> sort) {
        List<Order> orders = new ArrayList<Order>();

        if (sort.size() == 0)
            return orders;

        for (String[] sortItem : sort) {
            String property = sortItem[0];
            Direction direction = Direction.fromString(sortItem[1]);
            Order order = new Order(direction, property);
            orders.add(order);
        }

        return orders;
    }

}
