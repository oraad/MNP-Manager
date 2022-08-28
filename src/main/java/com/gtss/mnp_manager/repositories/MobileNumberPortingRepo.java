package com.gtss.mnp_manager.repositories;

import java.util.Optional;
import com.gtss.mnp_manager.models.MobileNumberPorting;
import com.gtss.mnp_manager.models.MobileOperator;
import com.gtss.mnp_manager.models.MobileSubscriber;
import com.gtss.mnp_manager.models.PortingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileNumberPortingRepo
        extends PagingAndSortingRepository<MobileNumberPorting, Long> {

    /**
     * Get all mobile number porting by status
     * 
     * @param status Porting Status
     */
    @Query("SELECT mnp FROM MobileNumberPorting mnp WHERE mnp.status=?1")
    Iterable<MobileNumberPorting> findAllByStatus(PortingStatus status);

    /**
     * Get all mobile number porting where mobile operator is either a donor or recipient or has
     * 'Accepted' status
     * 
     * @param mobileOperator Donor or Recipient mobile operator
     * @param pageable paging conditions
     */
    @Query("SELECT mnp FROM MobileNumberPorting mnp "
            + "WHERE mnp.status=com.gtss.mnp_manager.models.PortingStatus.ACCEPTED "
            + "OR mnp.donorMobileOperator=?1 "
            + "OR mnp.recipientMobileOperator=?1")
    Page<MobileNumberPorting> findAllByMobileOperatorOrAcceptedStatus(
            MobileOperator mobileOperator, Pageable pageable);

    /**
     * Get latest mobile subscriber details
     * 
     * @param mobileSubscriber Mobile subscriber
     */
    Optional<MobileNumberPorting> findTopByMobileSubscriberOrderByIdDesc(
            MobileSubscriber mobileSubscriber);

    /**
     * Get mobile number porting by mobile subscriber and status
     * 
     * @param mobileSubscriber Mobile subscriber
     * @param status Porting Status
     */
    @Query("SELECT mnp FROM MobileNumberPorting mnp WHERE mnp.mobileSubscriber=?1 AND mnp.status=?2")
    Optional<MobileNumberPorting> findByMobileSubscriberAndStatus(
            MobileSubscriber mobileSubscriber, PortingStatus status);

    /**
     * Get all pending mobile number porting by donor mobile operator
     * 
     * @param mobileOperator Donor Mobile Operator
     * @param pageable paging conditions
     */
    @Query("SELECT mnp FROM MobileNumberPorting mnp "
            + "WHERE mnp.status=com.gtss.mnp_manager.models.PortingStatus.PENDING "
            + "AND mnp.donorMobileOperator=?1")
    Page<MobileNumberPorting> findAllByDonorMobileOperatorAndPendingStatus(
            MobileOperator mobileOperator, Pageable pageable);

    /**
     * Get all pending mobile number porting by recipient mobile operator
     * 
     * @param mobileOperator Recipient Mobile Operator
     * @param pageable paging conditions
     */
    @Query("SELECT mnp FROM MobileNumberPorting mnp "
            + "WHERE mnp.status=com.gtss.mnp_manager.models.PortingStatus.ACCEPTED "
            + "AND mnp.recipientMobileOperator=?1")
    Page<MobileNumberPorting> findAllByRecipientMobileOperatorAndAcceptedStatus(
            MobileOperator mobileOperator, Pageable pageable);
}
