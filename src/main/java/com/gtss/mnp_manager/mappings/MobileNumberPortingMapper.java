package com.gtss.mnp_manager.mappings;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.gtss.mnp_manager.dtos.MobileNumberPortingDto;
import com.gtss.mnp_manager.models.MobileNumberPorting;

@Mapper(componentModel = "spring")
public interface MobileNumberPortingMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "mobileSubscriber.mobileNumber", target = "mobileNumber")
    @Mapping(source = "donorMobileOperator.operatorName",
            target = "donorOperatorName")
    @Mapping(source = "recipientMobileOperator.operatorName",
            target = "recipientOperatorName")
    MobileNumberPortingDto toDto(MobileNumberPorting mobileNumberPorting);

    Iterable<MobileNumberPortingDto> toDto(
            Iterable<MobileNumberPorting> iterableMobileNumberPorting);

    default String fromJsonDtotoModelProperty(String property) {

        switch (property) {
            case "mobileNumber":
                return "mobileSubscriber.mobileNumber";
            case "donorOperator":
                return "donorMobileOperator.operatorName";
            case "recipientOperator":
                return "recipientMobileOperator.operatorName";
            default:
                return property;
        }

    }
}
