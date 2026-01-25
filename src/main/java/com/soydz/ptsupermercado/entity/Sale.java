package com.soydz.ptsupermercado.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "sale")
public class Sale {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @CreationTimestamp
  @Column(name = "creation_date", nullable = false, updatable = false)
  private LocalDateTime creationDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "store_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sale_store"))
  @NotNull private Store store;

  @OneToMany(
      mappedBy = "sale",
      fetch = FetchType.LAZY,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  @NotEmpty @Valid private Set<SalesDetails> salesDetails = new HashSet<>();

  @JoinColumn(name = "deleted_at")
  private LocalDateTime deletedAt = null;

  public Sale() {}

  public Sale(
      LocalDateTime creationDate,
      Store store,
      Set<SalesDetails> salesDetails,
      LocalDateTime deletedAt) {
    this.creationDate = creationDate;
    this.store = store;
    this.salesDetails = salesDetails;
    this.deletedAt = deletedAt;
  }

  public void addDetail(SalesDetails sd) {

    salesDetails.stream()
        .filter(detail -> detail.getProduct().getId().equals(sd.getProduct().getId()))
        .findFirst()
        .ifPresentOrElse(
            detail -> detail.setQuantity(detail.getQuantity() + sd.getQuantity()),
            () -> {
              sd.setSale(this);
              salesDetails.add(sd);
            });
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt() {
    this.deletedAt = LocalDateTime.now();
  }

  public Set<SalesDetails> getSalesDetails() {
    return salesDetails;
  }

  public void setSalesDetails(Set<SalesDetails> salesDetails) {
    this.salesDetails = salesDetails;
  }

  public Store getStore() {
    return store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
