package com.soydz.ptsupermercado.repository;

import com.soydz.ptsupermercado.entity.Sale;
import com.soydz.ptsupermercado.entity.Store;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ISaleRepository extends JpaRepository<Sale, Long> {

  @Query(
      "SELECT s FROM Sale s WHERE s.deletedAt IS NULL AND s.store = :store AND FUNCTION('DATE', s.creationDate) = :date")
  List<Sale> findByStoreIdAndCreationDate(
      @Param("store") Store store, @Param("date") LocalDate date);
}
