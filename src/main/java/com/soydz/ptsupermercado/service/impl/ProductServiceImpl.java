package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.entity.Supplier;
import com.soydz.ptsupermercado.repository.IProductRepository;
import com.soydz.ptsupermercado.service.interfaces.IProductService;
import com.soydz.ptsupermercado.service.interfaces.ISupplierService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

  private final IProductRepository productRepository;
  private final ISupplierService supplierService;

  public ProductServiceImpl(
      IProductRepository productRepository, ISupplierService supplierService) {
    this.productRepository = productRepository;
    this.supplierService = supplierService;
  }

  @Override
  public ProductResDTO save(ProductReqDTO productReqDTO) {

    Supplier supplier = supplierService.findById(productReqDTO.supplierId());

    return ProductResDTO.fromEntity(
        productRepository.save(ProductReqDTO.toEntity(productReqDTO, supplier)));
  }
}
