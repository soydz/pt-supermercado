package com.soydz.ptsupermercado.repository;

import com.soydz.ptsupermercado.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {}
