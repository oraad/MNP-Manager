package com.gtss.mnp_manager.mappings;

import org.mapstruct.Mapper;
import com.gtss.mnp_manager.dtos.MobileOperatorDto;
import com.gtss.mnp_manager.models.MobileOperator;

@Mapper(componentModel = "spring")
public interface MobileOperatorMapper {
    
    MobileOperatorDto toDto(MobileOperator mobileOperator);
}
