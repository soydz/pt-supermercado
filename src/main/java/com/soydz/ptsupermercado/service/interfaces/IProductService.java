package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.entity.Product;
import java.util.List;

public interface IProductService {

  ProductResDTO save(ProductReqDTO productReqDTO);

  List<ProductResDTO> findAll();

  Product findById(Long id);

  ProductResDTO update(ProductReqDTO productReqDTO, Long id);

  void delete(Long id);
}
