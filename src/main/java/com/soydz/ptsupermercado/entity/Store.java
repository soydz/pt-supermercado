package com.soydz.ptsupermercado.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "store")
public class Store {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotNull @NotBlank private String name;

  @NotNull @NotBlank private String address;

  @NotNull @NotBlank private String city;

  @OneToMany(mappedBy = "store")
  private List<Sale> sales;

  public Store() {}

  public Store(String name, String address, String city) {
    this.name = name;
    this.address = address;
    this.city = city;
  }

  public Store(String name, String address, String city, List<Sale> sales) {
    this.name = name;
    this.address = address;
    this.city = city;
    this.sales = sales;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<Sale> getSales() {
    return sales;
  }

  public void setSales(List<Sale> sales) {
    this.sales = sales;
  }
}
