package com.lookingdev.github.Repositories;

import com.lookingdev.github.Domain.Entities.DeveloperProfile;
import com.lookingdev.github.Domain.Models.DeveloperDTOModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeveloperRepository {

    private final SessionFactory factory = new Configuration()
            .configure("hibernateconfig.cfg.xml")
            .addAnnotatedClass(DeveloperProfile.class)
            .buildSessionFactory();

    public void add(DeveloperDTOModel model){

        try(Session session = factory.openSession()){

            session.beginTransaction();
            DeveloperProfile entity = new DeveloperProfile();{
                entity.setUsername(model.getUsername());
                entity.setPlatform(model.getPlatform());
                entity.setLocation(model.getLocation());
                entity.setProfileUrl(model.getProfileUrl());
                entity.setLastActivityDate(model.getLastActivityDate());
                entity.setReputation(model.getReputation());
                entity.setSkills(model.getSkills().toArray(new String[0]));
            }

            session.persist(entity);
            session.getTransaction().commit();
        }
    }
    public List<DeveloperProfile> getDevelopers(int limit, int lastId){

        try(Session session = factory.openSession()){

            String sql = "SELECT * FROM developers WHERE id > :lastid LIMIT " + limit;

            NativeQuery<DeveloperProfile> query = session.createNativeQuery(sql, DeveloperProfile.class);
            query.setParameter("lastid", lastId);

            return query.list();

        } catch (Exception ex){
            System.out.println("Error with getting users from db " + ex);
            return null;
        }

    }


}
