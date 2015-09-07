package org.dbtools.jpa.domain;


import org.dbtools.query.jpa.JPAQueryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public abstract class JPABaseManager<T extends JPABaseRecord> {

    public abstract Class<T> getRecordClass();
    public abstract String getTableName();
    public abstract String getTableClassName();
    public abstract String getPrimaryKey();
    public abstract String getPrimaryKeyProperty();
    public abstract EntityManager getEntityManager();

    public void create(T record) {
        getEntityManager().persist(record);
    }

    public void update(T record) {
        T mergedRecord = getEntityManager().merge(record);
        mergedRecord.cleanupOrphans(getEntityManager());  // work-around till CascadeType.DELETE-ORPHAN is supported
    }

    public void delete(T record) {
        T mergedRecord = getEntityManager().merge(record);
        mergedRecord.cleanupOrphans(getEntityManager());  // work-around till CascadeType.DELETE-ORPHAN is supported
        getEntityManager().remove(mergedRecord);
    }

    public void save(T record) {
        if (record.isNewRecord()) {
            create(record);
        } else {
            update(record);
        }
    }

    public void deleteAll() {
        Query q = getEntityManager().createNativeQuery("DELETE FROM " + getTableName());
        q.executeUpdate();
    }

    public int update(@Nonnull ContentValues values, long rowId) {
        int parameterPosition = values.size();
        return update(values, getPrimaryKeyProperty() + " = ?" + parameterPosition, new Object[]{rowId});
    }

    /**
     * Update content values based on where and whereArgs
     * @param values Key value pair to be update (example: ("ID", 3) would set column ID to value 3)
     * @param where contains where clause with optional where parameters (be sure where parameters start at value.size()+)
     * @param whereArgs where clause parameter values
     * @return count of records updated
     */
    public int update(@Nonnull ContentValues values, @Nullable String where, @Nullable Object[] whereArgs) {
        StringBuilder rawQuery = new StringBuilder();
        rawQuery.append("UPDATE ").append(getTableName());

        int paramCount = 0;
        for (String column : values.keySet()) {
            if (paramCount == 0) {
                rawQuery.append(" SET ");
            } else {
                rawQuery.append(", ");
            }
            rawQuery.append(column).append(" = ?").append(paramCount);

            paramCount++;
        }

        rawQuery.append(" WHERE ").append(where);

        Query query = getEntityManager().createNativeQuery(rawQuery.toString());

        int currentParam = 0;
        // add value parameters
        for (Object value : values.values()) {
            query.setParameter(currentParam, value);

            currentParam++;
        }

        // add where claus parameters
        for (int i = 0; whereArgs != null && i < whereArgs.length; i++, currentParam++) {
            query.setParameter(currentParam, whereArgs[i]);
        }

        // for write to database and clear cache
        getEntityManager().flush();
        getEntityManager().clear();

        return query.executeUpdate();
    }

    public int delete(long rowId) {
        return delete(getPrimaryKeyProperty() + " = ?0", new Object[]{rowId});
    }

    public int delete(@Nullable String where, @Nullable Object[] whereArgs) {
        Query query = getEntityManager().createNativeQuery("DELETE FROM " + getTableName() + " WHERE " + where);

        for (int i = 0; whereArgs != null && i < whereArgs.length; i++) {
            query.setParameter(i, whereArgs[i]);
        }

        return query.executeUpdate();
    }

    @Nonnull
    public List<T> findAll() {
        TypedQuery<T> q = getEntityManager().createQuery("SELECT o FROM " + getTableClassName() + " o", getRecordClass());
        return q.getResultList();
    }

    @Nonnull
    public List<T> findAllOrderBy(@Nullable String orderBy) {
        return findAllBySelection(null, null, orderBy);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nullable String selection, @Nonnull Object[] selectionArgs) {
        return findAllBySelection(selection, selectionArgs, null);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nullable String selection, @Nullable Object[] selectionArgs, @Nullable String orderBy) {
        JPAQueryBuilder builder = new JPAQueryBuilder();

        builder.object(getTableClassName());

        if (selection != null) {
            builder.filter(selection);
        }

        if (orderBy != null) {
            builder.orderBy(orderBy);
        }

        TypedQuery<T> query = getEntityManager().createQuery(builder.buildQuery(), getRecordClass());

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        return query.getResultList();
    }

    @Nullable
    public T findByRowId(long id) {
        TypedQuery<T> query = getEntityManager().createQuery("SELECT o FROM " + getTableClassName() + " o WHERE o." + getPrimaryKeyProperty() + " = ?0 ", getRecordClass());
        query.setParameter(0, id);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Nullable
    public T findBySelection(@Nullable String selection, @Nullable Object[] selectionArgs) {
        return findBySelection(selection, selectionArgs, null);
    }

    @Nullable
    public T findBySelection(@Nullable String selection, @Nullable Object[] selectionArgs, @Nullable String orderBy) {
        JPAQueryBuilder builder = new JPAQueryBuilder();

        builder.object(getTableClassName());
        builder.filter(selection);

        if (orderBy != null) {
            builder.orderBy(orderBy);
        }

        TypedQuery<T> query = getEntityManager().createQuery("SELECT o FROM " + getTableClassName() + " o WHERE " + selection, getRecordClass());

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Populate of List from a JPA jpaQuery.
     *
     * @param jpaQuery      Custom JPA query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @Nonnull
    public List<T> findAllByRawQuery(@Nonnull String jpaQuery, @Nullable Object[] selectionArgs) {
        TypedQuery<T> query = getEntityManager().createQuery(jpaQuery, getRecordClass());

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        return query.getResultList();
    }

    /**
     * Return T from a JPA rawQuery.
     *
     * @param jpaQuery      Custom JPA query
     * @param selectionArgs query arguments
     * @return object T
     */
    @Nullable
    public T findByRawQuery(@Nullable String jpaQuery, @Nullable Object[] selectionArgs) {
        TypedQuery<T> query = getEntityManager().createQuery(jpaQuery, getRecordClass());

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public long findCount() {
        Query q = getEntityManager().createNativeQuery("SELECT count(0) FROM " + getTableName());
        return ((Number) q.getSingleResult()).longValue();
    }

    public long findCountBySelection(@Nullable String selection, @Nullable Object[] selectionArgs) {
        Query query = getEntityManager().createNativeQuery("SELECT count(0) FROM " + getTableClassName() + " WHERE " + selection);

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        return ((Number) query.getSingleResult()).longValue();
    }

    /**
     * Return the first column and first row value as a Date for given rawQuery and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param rawQuery      Query contain first column which is the needed value
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueByRawQuery(@Nonnull Class<I> valueType, @Nonnull String rawQuery, @Nullable Object[] selectionArgs, I defaultValue) {
        JPAQueryBuilder builder = new JPAQueryBuilder();

        builder.object(getTableClassName());

        Query query = getEntityManager().createNativeQuery(rawQuery);

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        try {
            return (I) query.getSingleResult();
        } catch (NoResultException e) {
            return defaultValue;
        }
    }

    /**
     * Return the value for the specified column and first row value as given type for given selection and selectionArgs.
     *
     * @param valueType     Type to be used when getting data from database and what type is used on return (Integer.class, Boolean.class, etc)
     * @param column        Column which contains value
     * @param selection     Query selection
     * @param selectionArgs Query parameters
     * @param defaultValue  Value returned if nothing is found
     * @return query results value or defaultValue if no data was returned
     */
    public <I> I findValueBySelection(@Nonnull Class<I> valueType, @Nonnull String column, @Nonnull String selection, @Nullable Object[] selectionArgs, I defaultValue) {
        Query query = getEntityManager().createNativeQuery("SELECT " + column + " FROM " + getTableName() + " WHERE " + selection);

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        try {
            return (I) query.getSingleResult();
        } catch (NoResultException e) {
            return defaultValue;
        }
    }


}
