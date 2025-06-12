package com.example.diplom.repository;

import com.example.diplom.model.ViolationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolRep extends JpaRepository<ViolationPhoto,Long> {

}
