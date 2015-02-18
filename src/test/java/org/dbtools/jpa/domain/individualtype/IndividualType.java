/*
 * IndividualType.java
 *
 * GENERATED FILE - DO NOT EDIT
 * CHECKSTYLE:OFF
 * 
 */



package org.dbtools.jpa.domain.individualtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("all")
public enum IndividualType {
HEAD, SPOUSE, CHILD;

    private static Map<IndividualType, String> enumStringMap = new HashMap<IndividualType, String>();
    private static List<String> stringList = new ArrayList<String>();
    public static final String TABLE = "INDIVIDUAL_TYPE";
    public static final String TABLE_CLASSNAME = "IndividualType";
    public static final String PRIMARY_KEY_COLUMN = "id";
    public static final String C_ID = "id";
    public static final String FULL_C_ID = "INDIVIDUAL_TYPE.id";
    public static final String P_ID = "id";
    public static final String C_NAME = "NAME";
    public static final String FULL_C_NAME = "INDIVIDUAL_TYPE.NAME";
    public static final String P_NAME = "name";

    static {
        enumStringMap.put(HEAD, "Head");
        stringList.add("Head");
        
        enumStringMap.put(SPOUSE, "Spouse");
        stringList.add("Spouse");
        
        enumStringMap.put(CHILD, "Child");
        stringList.add("Child");
        

    }

    public static String getString(IndividualType key) {
        return enumStringMap.get(key);
    }

    public static List<String> getList() {
        return stringList;
    }


}