package com.example.java5_petshop.repository;

import com.example.java5_petshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OderRepository extends JpaRepository<Order, Long> {

}
