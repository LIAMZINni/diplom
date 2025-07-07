package com.example.diplom.model;

import com.example.diplom.Violations.Violation;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class Inspector extends User{
    private String badgeNumber; // Табельный номер

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;


    private String fullName;
    private String rang;/////&&&mb ddelete

    @OneToMany(mappedBy = "inspector")
    private List<Violation> violations;


}
