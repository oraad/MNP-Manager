package com.gtss.mnp_manager.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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
@Entity(name = "MobileSubscriber")
@Table(name = "mobile_subscriber")
public class MobileSubscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NonNull
    @Column(name = "mobile_number", nullable = false, length = 20)
    private String mobileNumber;

    @OneToOne(mappedBy = "mobileSubscriber", orphanRemoval = true,
            cascade = {CascadeType.ALL})
    private MobileSubscriberOperator mobileSubscriberOperator;

    public MobileOperator getMobileOperator() {
        return this.mobileSubscriberOperator.getCurrentMobileOperator();
    }

    public void setMobileOperator(MobileOperator mobileOperator) {
        this.mobileSubscriberOperator.setCurrentMobileOperator(mobileOperator);
    }

}
