package com.gtss.mnp_manager.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity(name = "MobileSubscriberOperator")
@Table(name = "mobile_subscriber_operator")
public class MobileSubscriberOperator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @NonNull
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "mobile_subscriber_id", referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "mobile_subscriber_id_fk"))
    private MobileSubscriber mobileSubscriber;

    @NonNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "current_operator_id", referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "current_operator_id_fk"))
    private MobileOperator currentMobileOperator;

    @NonNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "original_operator_id", referencedColumnName = "id",
            nullable = false,
            foreignKey = @ForeignKey(name = "original_operator_id_fk"))
    private MobileOperator originalMobileOperator;

}
