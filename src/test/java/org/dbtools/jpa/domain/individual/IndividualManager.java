/*
 * IndividualManager.java
 *
 * Generated on: 02/16/2015 07:28:32
 *
 */



package org.dbtools.jpa.domain.individual;


import java.util.List;

@javax.inject.Singleton
public class IndividualManager extends IndividualBaseManager {


    @javax.inject.Inject
    public IndividualManager() {
    }

    public List<Individual> findAllById(long id) {
        return findAllBySelection(Individual.C_ID + " = ?0", new Object[]{id});
    }

    public List<Individual> findAllByIdRaw(long id) {
        return findAllByRawQuery("SELECT * FROM " + Individual.TABLE + " WHERE " + Individual.C_ID + " = ?0 ", new Object[]{id});
    }

    public long findCountBySelectionByRowId(long id) {
        return findCountBySelection(Individual.C_ID + " = ?0 ", new Object[]{id});
    }

    public String findNameByIdRaw(long id) {
        return findValueByRawQuery(String.class, "SELECT " + Individual.C_NAME + " FROM " + Individual.TABLE + " WHERE " + Individual.C_ID + " = ?0 ", new Object[]{id}, "None");
    }

    public String findNameByIdSelection(long id) {
        return findValueBySelection(String.class, Individual.C_NAME, Individual.C_ID + " = ?0 ", new Object[]{id}, "None");
    }

    public Individual findIndividualByName(String name) {
        return findBySelection(Individual.C_NAME + " = ?0 ", new Object[]{name}, null);
    }

    public Individual findIndividualByNameRaw(String name) {
        return findByRawQuery("SELECT * FROM " + Individual.TABLE + " WHERE " + Individual.C_NAME + " = ?0 ", new Object[]{name});
    }
}