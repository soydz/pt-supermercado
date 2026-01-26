package com.soydz.ptsupermercado.dto;

import com.soydz.ptsupermercado.entity.Product;
import java.util.Map;

public record BestSellingProductResDTO(ProductResDTO product, Integer totalSold) {

  public static BestSellingProductResDTO fromMapEntry(
      Map.Entry<Product, Integer> bestSellingProduct) {

    return new BestSellingProductResDTO(
        ProductResDTO.fromEntity(bestSellingProduct.getKey()), bestSellingProduct.getValue());
  }
}
