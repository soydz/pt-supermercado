package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Supplier;
import com.soydz.ptsupermercado.repository.IProductRepository;
import com.soydz.ptsupermercado.service.exception.ProductNotFoundException;
import com.soydz.ptsupermercado.service.interfaces.IProductService;
import com.soydz.ptsupermercado.service.interfaces.ISupplierService;
import java.util.List;
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

  @Override
  public List<ProductResDTO> findAll() {
    return productRepository.findAll().stream().map(ProductResDTO::fromEntity).toList();
  }

  @Override
  public Product findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
  }

  @Override
  public ProductResDTO update(ProductReqDTO productReqDTO, Long id) {
    Product product = findById(id);
    Supplier supplier = supplierService.findById(productReqDTO.supplierId());

    product.setName(productReqDTO.name());
    product.setCategory(productReqDTO.category());
    product.setPrice(productReqDTO.price());
    product.setSupplier(supplier);

    return ProductResDTO.fromEntity(productRepository.save(product));
  }
}
