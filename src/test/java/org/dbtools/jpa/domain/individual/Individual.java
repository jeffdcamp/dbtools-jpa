/*
 * Individual.java
 *
 * Created: 02/16/2015 07:28:32
 */



package org.dbtools.jpa.domain.individual;

import javax.persistence.Table;


@javax.persistence.Entity()
@Table(name=IndividualBaseRecord.TABLE)
public class Individual extends IndividualBaseRecord {


    public Individual() {
    }
}