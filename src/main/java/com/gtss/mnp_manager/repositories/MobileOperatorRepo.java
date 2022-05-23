package com.gtss.mnp_manager.repositories;

import com.gtss.mnp_manager.models.MobileOperator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileOperatorRepo
        extends CrudRepository<MobileOperator, Short> {

}
