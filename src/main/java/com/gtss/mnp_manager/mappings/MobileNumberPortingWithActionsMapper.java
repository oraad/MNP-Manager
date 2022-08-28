package com.gtss.mnp_manager.mappings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gtss.mnp_manager.dtos.MobileNumberPortingWithActionsDto;
import com.gtss.mnp_manager.models.MobileNumberPorting;

@Mapper(componentModel = "spring")
public interface MobileNumberPortingWithActionsMapper {
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "mobileSubscriber.mobileNumber",
                    target = "mobileNumber")
    @Mapping(source = "donorMobileOperator.operatorName",
                    target = "donorOperatorName")
    @Mapping(source = "recipientMobileOperator.operatorName",
                    target = "recipientOperatorName")
    MobileNumberPortingWithActionsDto toDto(MobileNumberPorting mobileNumberPorting);
}
