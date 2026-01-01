package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import java.util.List;

public interface IProductService {

  ProductResDTO save(ProductReqDTO productReqDTO);

  List<ProductResDTO> findAll();
}
