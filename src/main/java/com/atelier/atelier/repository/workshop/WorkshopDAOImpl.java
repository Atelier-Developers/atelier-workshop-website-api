package com.atelier.atelier.repository.workshop;

import com.atelier.atelier.entity.WorkshopManagment.Workshop;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class WorkshopDAOImpl implements WorkshopDAO {

    private EntityManager entityManager;

    @Autowired
    public WorkshopDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Workshop> findAll() {
        Session currentSession = entityManager.unwrap(Session.class);

        Query theQuery = currentSession.createQuery("select w FROM Workshop w");

        List<Workshop> workshops = theQuery.getResultList();
        return workshops;
    }

    @Override
    public Workshop findById(long id) {

        Session currentSession = entityManager.unwrap(Session.class);

        return currentSession.get(Workshop.class, id);
    }

    @Override
    public void save(Workshop workshop) {
        Session currentSession = entityManager.unwrap(Session.class);
        currentSession.saveOrUpdate(workshop);
    }

    @Override
    public void deleteById(long id) {
        System.out.println(id);
        Session currentSession = entityManager.unwrap(Session.class);
        Query theQuery =
                currentSession.createQuery(
                        "delete from Workshop where id=:wid");
        theQuery.setParameter("wid", id);
        theQuery.executeUpdate();
    }


}
