/*
 * IndividualBaseManager.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.jpa.domain.individual;

import org.dbtools.jpa.domain.JPABaseManager;

import javax.persistence.EntityManager;


@SuppressWarnings("all")
public class IndividualBaseManager extends JPABaseManager<Individual> {

    @javax.inject.Inject
     EntityManager entityManager;

    public IndividualBaseManager() {
    }

    public Class<Individual> getRecordClass() {
        return Individual.class;
    }

    public String getTableName() {
        return Individual.TABLE;
    }

    public String getTableClassName() {
        return Individual.TABLE_CLASSNAME;
    }

    public String getPrimaryKey() {
        return Individual.PRIMARY_KEY_COLUMN;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


}