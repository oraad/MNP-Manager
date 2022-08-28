package com.gtss.mnp_manager.mappings;

import org.mapstruct.Mapper;
import com.gtss.mnp_manager.dtos.MobileOperatorCredentialDto;
import com.gtss.mnp_manager.models.MobileOperatorCredential;


@Mapper(componentModel = "spring")
public interface MobileOperatorCredentialMapper {

    MobileOperatorCredential toModel(MobileOperatorCredentialDto mobileOperatorCredentialDto);
}
