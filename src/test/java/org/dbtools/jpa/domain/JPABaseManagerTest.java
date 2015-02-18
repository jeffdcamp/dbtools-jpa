package org.dbtools.jpa.domain;

import org.dbtools.jpa.domain.individual.Individual;
import org.dbtools.jpa.domain.individual.IndividualManager;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JPABaseManagerTest {

    private EntityManager entityManager;
    private IndividualManager individualManager;

    @Before
    public void setup() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence");
        entityManager = entityManagerFactory.createEntityManager();

        individualManager = new IndividualManager();
        individualManager.setEntityManager(entityManager);

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        individualManager.save(createIndividual("Jeff"));
        individualManager.save(createIndividual("Tanner"));
        tx.commit();
    }

    private Individual createIndividual(String name) {
        Individual individual = new Individual();
        individual.setName(name);
        individual.setBirthDate(DateTime.now());
        return individual;
    }


    @Test
    public void testCount() {
        assertEquals(2L, individualManager.findCount());
        assertEquals(1L, individualManager.findCountBySelectionByRowId(1));
    }


    @Test
    public void testUpdate() {
        String testName = "Bobby";

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Individual ind1 = individualManager.findByRowId(1L);
        String origName = ind1.getName();
        ind1.setName(testName);
        individualManager.save(ind1);
        tx.commit();

        // change name
        Individual ind2 = individualManager.findByRowId(1L);
        assertEquals(testName, ind2.getName());

        // restore name
        ContentValues contentValues = new ContentValues();
        contentValues.put(Individual.C_NAME, origName);

        EntityTransaction tx2 = entityManager.getTransaction();
        tx2.begin();
        int updatedCount = individualManager.update(contentValues, 1L);
        tx2.commit();
        assertEquals("Update count", 1, updatedCount);

        String updatedName = individualManager.findNameByIdRaw(1L);
        assertEquals(origName, updatedName);
    }

    @Test
    public void testUpdate2() {
        String testName = "Bobby";
        String testEmail = "bob@bob.com";

        ContentValues contentValues = new ContentValues();
        contentValues.put(Individual.P_NAME, testName);
        contentValues.put(Individual.P_EMAIL, testEmail);

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        int updatedCount = individualManager.update(contentValues, 1L);
        tx.commit();
        assertEquals("Update count", 1, updatedCount);

        Individual individual = individualManager.findByRowId(1L);
        assertEquals(testName, individual.getName());
        assertEquals(testEmail, individual.getEmail());
    }

    @Test
    public void testDelete() {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Individual ind1 = new Individual();
        ind1.setName("Betty");
        ind1.setBirthDate(DateTime.now());
        individualManager.save(ind1);
        tx.commit();

        assertEquals(3L, individualManager.findCount());

        EntityTransaction tx2 = entityManager.getTransaction();
        tx2.begin();
        int count = individualManager.delete(ind1.getId());
        assertEquals("delete count", 1, count);
        tx2.commit();

        // make sure the record is gone
        assertNull(individualManager.findByRowId(ind1.getId()));

        // double-check the table count
        assertEquals(2L, individualManager.findCount());
    }

    @Test
    public void testDelete2() {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        Individual ind1 = new Individual();
        ind1.setName("Buddy");
        ind1.setBirthDate(DateTime.now());
        individualManager.save(ind1);
        tx.commit();

        assertEquals(3L, individualManager.findCount());

        EntityTransaction tx2 = entityManager.getTransaction();
        tx2.begin();
        individualManager.delete(ind1);
        tx2.commit();

        assertEquals(2L, individualManager.findCount());
    }

    @Test
    public void testDeleteAll() {
        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        individualManager.deleteAll();
        tx.commit();

        assertEquals(0, individualManager.findCount());
    }

    @Test
    public void testFindById() {
        // RAW
        assertEquals("Jeff", individualManager.findByRowId(1).getName());
    }

    @Test
    public void testFindValueBy() {
        // RAW
        assertEquals("Jeff", individualManager.findNameByIdRaw(1));
        assertEquals("None", individualManager.findNameByIdRaw(10)); // FAIL

        // Selection
        assertEquals("Jeff", individualManager.findNameByIdRaw(1));
        assertEquals("None", individualManager.findNameByIdRaw(10)); // FAIL
    }

    @Test
    public void testFindValueBySelection() {
        // RAW
        assertEquals("Jeff", individualManager.findNameByIdSelection(1));
        assertEquals("None", individualManager.findNameByIdSelection(10)); // FAIL

        // Selection
        assertEquals("Jeff", individualManager.findNameByIdSelection(1));
        assertEquals("None", individualManager.findNameByIdSelection(10)); // FAIL
    }

    @Test
    public void testFindBySelection() {
        // RAW
        assertEquals("Jeff", individualManager.findIndividualByName("Jeff").getName());
        assertNull("None", individualManager.findIndividualByName("Bubba")); // FAIL

        // RAW
        assertEquals("Jeff", individualManager.findIndividualByNameRaw("Jeff").getName());
        assertNull("None", individualManager.findIndividualByNameRaw("Bubba")); // FAIL
    }

    @Test
    public void testFindAllBy() {
        assertEquals(2, individualManager.findAll().size());
        assertEquals(2, individualManager.findAllOrderBy(Individual.P_NAME).size());

        assertEquals(1, individualManager.findAllById(1).size());
        assertEquals(1, individualManager.findAllByIdRaw(1).size());
    }
}