package com.gtss.mnp_manager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "MobileOperator")
@Table(name = "mobile_operator")
public class MobileOperator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private int id;

    @NonNull
    @Column(name = "operator_name", nullable = false, length = 30)
    private String operatorName;

    @NonNull
    @Column(name = "organization_header", nullable = false, length = 30)
    private String organizationHeader;
    
}
