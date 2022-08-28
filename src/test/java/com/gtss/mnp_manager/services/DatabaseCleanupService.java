package com.gtss.mnp_manager.services;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseCleanupService implements InitializingBean {

    @Autowired
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        Metamodel metaModel = entityManager.getMetamodel();
        // Object a = metaModel.getEntities();

        tableNames = new ArrayList<String>();

        for (EntityType<?> entityType : metaModel.getEntities()) {
            String tableName =
                    entityType.getJavaType().getAnnotation(Table.class).name();
            tableNames.add(tableName);
        }
    }

    /**
     * Utility method that truncates all identified tables
     */
    @Transactional
    public void truncate() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE")
                .executeUpdate();
        tableNames.forEach(tableName -> entityManager
                .createNativeQuery("TRUNCATE TABLE " + tableName)
                .executeUpdate());
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE")
                .executeUpdate();
    }
}
