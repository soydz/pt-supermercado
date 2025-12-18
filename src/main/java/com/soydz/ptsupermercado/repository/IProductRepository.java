package com.soydz.ptsupermercado.repository;

import com.soydz.ptsupermercado.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {}
