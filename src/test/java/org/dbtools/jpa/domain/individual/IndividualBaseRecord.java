/*
 * IndividualBaseRecord.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.jpa.domain.individual;

import org.dbtools.jpa.domain.JPABaseRecord;
import org.dbtools.jpa.domain.individualtype.IndividualType;

import javax.persistence.*;


@SuppressWarnings("all")
@javax.persistence.MappedSuperclass()
public class IndividualBaseRecord extends JPABaseRecord implements java.io.Serializable {

    public static final String TABLE = "INDIVIDUAL";
    public static final String TABLE_CLASSNAME = "Individual";
    public static final String PRIMARY_KEY_COLUMN = "id";
    public static final String PRIMARY_KEY_COLUMN_PROPERTY = "id";
    public static final String C_ID = "id";
    public static final String FULL_C_ID = "INDIVIDUAL.id";
    public static final String P_ID = "id";
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id", nullable=false)
    private long id = 0;
    public static final String C_INDIVIDUAL_TYPE = "INDIVIDUAL_TYPE_ID";
    public static final String FULL_C_INDIVIDUAL_TYPE = "INDIVIDUAL.INDIVIDUAL_TYPE_ID";
    public static final String P_INDIVIDUAL_TYPE = "individualType";
    @Enumerated(EnumType.ORDINAL)
    @Column(name="INDIVIDUAL_TYPE_ID", nullable=false)
    private IndividualType individualType = IndividualType.HEAD;
    public static final String C_NAME = "NAME";
    public static final String FULL_C_NAME = "INDIVIDUAL.NAME";
    public static final String P_NAME = "name";
    @Column(name="NAME", length=255, nullable=false)
    private String name = "";
    public static final String C_BIRTH_DATE = "BIRTH_DATE";
    public static final String FULL_C_BIRTH_DATE = "INDIVIDUAL.BIRTH_DATE";
    public static final String P_BIRTH_DATE = "birthDate";
    @Column(name="BIRTH_DATE", nullable=false)
    @Temporal(value = TemporalType.DATE)
    private java.util.Date birthDate = null;
    public static final String C_NUMBER = "NUMBER";
    public static final String FULL_C_NUMBER = "INDIVIDUAL.NUMBER";
    public static final String P_NUMBER = "number";
    @Column(name="NUMBER", nullable=false)
    private int number = 0;
    public static final String C_PHONE = "PHONE";
    public static final String FULL_C_PHONE = "INDIVIDUAL.PHONE";
    public static final String P_PHONE = "phone";
    @Column(name="PHONE", length=255, nullable=false)
    private String phone = "";
    public static final String C_EMAIL = "EMAIL";
    public static final String FULL_C_EMAIL = "INDIVIDUAL.EMAIL";
    public static final String P_EMAIL = "email";
    @Column(name="EMAIL", length=255, nullable=false)
    private String email = "";

    public IndividualBaseRecord() {
    }

    @Override
    public String getIdColumnName() {
        return C_ID;
    }

    @Override
    public long getPrimaryKeyId() {
        return id;
    }

    @Override
    public void setPrimaryKeyId(long id) {
        this.id = id;
    }

    public org.joda.time.DateTime getBirthDate() {
        return new org.joda.time.DateTime(birthDate);
    }

    public void setBirthDate(org.joda.time.DateTime birthDate) {
        this.birthDate = birthDate.toDate();
    }

    public void cleanupOrphans(javax.persistence.EntityManager em) {
    }

    @Override
    public String toString() {
        String text = "\n";
        text += "id = "+ id +"\n";
        text += "individualType = "+ individualType +"\n";
        text += "name = "+ name +"\n";
        text += "birthDate = "+ birthDate +"\n";
        text += "number = "+ number +"\n";
        text += "phone = "+ phone +"\n";
        text += "email = "+ email +"\n";
        return text;
    }

    public boolean isNewRecord() {
        return getPrimaryKeyId() <= 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IndividualType getIndividualType() {
        return individualType;
    }

    public void setIndividualType(IndividualType individualType) {
        this.individualType = individualType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}