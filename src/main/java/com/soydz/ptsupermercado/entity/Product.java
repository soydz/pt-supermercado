package com.soydz.ptsupermercado.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull @NotBlank private String name;
  private String category;
  @NotNull @Positive private BigDecimal price;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "supplier_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_product_supplier"))
  private Supplier supplier;

  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  @NotNull private List<SalesDetails> salesDetails;

  public Product() {}

  public Product(
      String name,
      String category,
      BigDecimal price,
      Supplier supplier,
      List<SalesDetails> salesDetails) {
    this.name = name;
    this.category = category;
    this.price = price;
    this.supplier = supplier;
    this.salesDetails = salesDetails;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Supplier getSupplier() {
    return supplier;
  }

  public void setSupplier(Supplier supplier) {
    this.supplier = supplier;
  }

  public List<SalesDetails> getSalesDetails() {
    return salesDetails;
  }

  public void setSalesDetails(List<SalesDetails> salesDetails) {
    this.salesDetails = salesDetails;
  }
}
