package com.gtss.mnp_manager.models;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity(name = "MobileNumberPorting")
@Table(name = "mobile_number_porting")
public class MobileNumberPorting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "mobile_subscriber_id", referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "mnp_mobile_subscriber_id_fk"))
    private MobileSubscriber mobileSubscriber;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "donor_operator_id", referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "mnp_donor_operator_id_fk"))
    private MobileOperator donorMobileOperator;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "recipient_operator_id", referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "mnp_recipient_operator_id_fk"))
    private MobileOperator recipientMobileOperator;

    @NonNull
    @Column(name = "created_on", nullable = false, updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false,
            columnDefinition = "ENUM('UNKNOWN', 'PENDING', 'ACCEPTED', 'REJECTED', 'CANCELED')")
    private PortingStatus status;
}
