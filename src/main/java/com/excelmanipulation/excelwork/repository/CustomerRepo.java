package com.excelmanipulation.excelwork.repository;

import com.excelmanipulation.excelwork.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customers, Integer> {

//    Optional<Customers> findById(Integer id);
}
