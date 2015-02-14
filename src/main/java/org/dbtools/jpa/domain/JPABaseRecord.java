package org.dbtools.jpa.domain;

import javax.persistence.EntityManager;

public abstract class JPABaseRecord {
    public abstract void cleanupOrphans(EntityManager entityManager);
    public abstract boolean isNewRecord();
    public abstract String getIdColumnName();
}
