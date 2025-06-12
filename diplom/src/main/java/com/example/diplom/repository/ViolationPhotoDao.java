package com.example.diplom.repository;

import com.example.diplom.model.CameraReport;
import com.example.diplom.model.ViolationPhoto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ViolationPhotoDao {
    @Autowired
    private SessionFactory sessionFactory;

    public Long save(CameraReport photo) {
        Session session = sessionFactory.getCurrentSession();
        return (Long) session.save(photo);
    }

    public CameraReport findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(CameraReport.class, id);
    }
}
