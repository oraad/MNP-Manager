package com.gtss.mnp_manager.dtos;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gtss.mnp_manager.models.PortingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobileNumberPortingDto {

    protected Long id;

    protected String mobileNumber;

    @JsonProperty(value = "donorOperator")
    protected String donorOperatorName;

    @JsonProperty(value = "recipientOperator")
    protected String recipientOperatorName;
    protected PortingStatus status;
    protected LocalDateTime createdOn;
    protected LocalDateTime updatedOn;

}
