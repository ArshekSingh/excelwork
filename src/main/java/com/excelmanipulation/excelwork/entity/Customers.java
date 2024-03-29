package com.excelmanipulation.excelwork.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "customers")
public class Customers {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "country")
    private String country;

    @Column(name = "dob")
    private String dob;
}
