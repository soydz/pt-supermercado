package com.soydz.ptsupermercado.repository;

import com.soydz.ptsupermercado.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Long> {

  boolean existsByName(String name);
}
