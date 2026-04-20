package com.petshop.repository;

import com.petshop.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    @Query(value = "SELECT supplier_id, name, contact_person, phone_number, email FROM suppliers", nativeQuery = true)
    List<Object[]> findAllRaw();
}