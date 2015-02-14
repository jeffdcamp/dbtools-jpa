package org.dbtools.jpa.domain;


import org.dbtools.query.jpa.JPAQueryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public abstract class JPABaseManager<T extends JPABaseRecord> {

    public abstract Class getRecordClass();
    public abstract String getTableName();
    public abstract String getTableClassName();
    public abstract String getPrimaryKey();

    @javax.persistence.PersistenceContext
    private EntityManager entityManager;

//    @javax.transaction.Transactional
    public void create(T record) {
        entityManager.persist(record);
    }

//    @javax.transaction.Transactional
    public void update(T record) {
        T mergedRecord = entityManager.merge(record);
        mergedRecord.cleanupOrphans(entityManager);  // work-around till CascadeType.DELETE-ORPHAN is supported
    }

//    @javax.transaction.Transactional
    public void delete(T record) {
        T mergedRecord = entityManager.merge(record);
        mergedRecord.cleanupOrphans(entityManager);  // work-around till CascadeType.DELETE-ORPHAN is supported
        entityManager.remove(mergedRecord);
    }

//    @javax.transaction.Transactional
    public void save(T record) {
        if (record.isNewRecord()) {
            create(record);
        } else {
            update(record);
        }
    }

    public T findByRowId(Object pk) {
        return (T) entityManager.find(getRecordClass(), pk);
    }

    public List<T> findAll() {
        Query q = entityManager.createQuery("SELECT o FROM " + getTableClassName() + " o");
        return q.getResultList();
    }

    public long findCount() {
        Query q = entityManager.createNativeQuery("SELECT count(0) FROM " + getTableName());
        return ((Number) q.getSingleResult()).longValue();
    }

    public void deleteAll() {
        Query q = entityManager.createNativeQuery("DELETE FROM " + getTableName());
        q.executeUpdate();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * If Depedendency Injection is not supported, supply an EntityManager here
     * @param entityManager JPA EntityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public int update(@Nonnull ContentValues values, long rowId) {
        return update(values, getPrimaryKey() + " = ?0", new String[]{String.valueOf(rowId)});
    }

    public int update(@Nonnull ContentValues values, @Nullable String where, @Nullable String[] whereArgs) {
        JPAQueryBuilder builder = new JPAQueryBuilder(entityManager);

        builder.object(getTableClassName());
        builder.filter(where);

        Query query = entityManager.createQuery("UPDATE c FROM " + getTableClassName() + " c WHERE " + where);

        for (int i = 0; i < whereArgs.length; i++) {
            query.setParameter(i, whereArgs[i]);
        }

        return query.executeUpdate();
    }

    public int delete(long rowId) {
        return delete(getPrimaryKey() + " = ?", new String[]{String.valueOf(rowId)});
    }

    public int delete(@Nullable String where, @Nullable String[] whereArgs) {
        JPAQueryBuilder builder = new JPAQueryBuilder(entityManager);

        builder.object(getTableClassName());
        builder.filter(where);

        Query query = entityManager.createQuery("DELETE FROM " + getTableClassName() + " c WHERE " + where);

        for (int i = 0; i < whereArgs.length; i++) {
            query.setParameter(i, whereArgs[i]);
        }

        return query.executeUpdate();
    }

    @Nonnull
    public List<T> findAllOrderBy(@Nullable String orderBy) {
        return findAllBySelection(null, null, orderBy);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nullable String selection, @Nonnull String[] selectionArgs) {
        return findAllBySelection(selection, selectionArgs, null);
    }

    @Nonnull
    public List<T> findAllBySelection(@Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        JPAQueryBuilder builder = new JPAQueryBuilder(entityManager);

        builder.object(getTableClassName());
        builder.filter(selection);

        if (orderBy != null) {
            builder.orderBy(orderBy);
        }

        Query query = entityManager.createQuery("SELECT c FROM " + getTableClassName() + " c WHERE " + selection);

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        return query.getResultList();
    }

    /**
     * Populate of List from a rawQuery.  The raw query must contain all of the columns names for the object
     *
     * @param rawQuery      Custom query
     * @param selectionArgs query arguments
     * @return List of object T
     */
    @Nonnull
    public List<T> findAllByRawQuery(@Nonnull String rawQuery, @Nullable String[] selectionArgs) {
        Query query = entityManager.createNativeQuery(rawQuery);

        for (int i = 0; selectionArgs != null && i < selectionArgs.length; i++) {
            query.setParameter(i, selectionArgs[i]);
        }

        return query.getResultList();
    }
}
