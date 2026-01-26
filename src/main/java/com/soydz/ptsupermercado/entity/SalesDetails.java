package com.soydz.ptsupermercado.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(
    name = "sales_details",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"sale_id", "product_id"})})
public class SalesDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "sale_id")
  @NotNull private Sale sale;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id")
  @NotNull private Product product;

  @NotNull @Positive private Integer quantity;

  public SalesDetails() {}

  public SalesDetails(Sale sale, Product product, Integer quantity) {
    this.sale = sale;
    this.product = product;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public Sale getSale() {
    return sale;
  }

  public void setSale(Sale sale) {
    this.sale = sale;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    SalesDetails that = (SalesDetails) o;
    return Objects.equals(id, that.id)
        && Objects.equals(sale, that.sale)
        && Objects.equals(product, that.product)
        && Objects.equals(quantity, that.quantity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sale, product, quantity);
  }

  @Override
  public String toString() {
    return "SalesDetails{"
        + "id="
        + id
        + ", sale="
        + sale
        + ", product="
        + product
        + ", quantity="
        + quantity
        + '}';
  }
}
