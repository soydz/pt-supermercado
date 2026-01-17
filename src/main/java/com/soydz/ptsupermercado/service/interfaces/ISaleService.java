package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.SaleReqDTO;
import com.soydz.ptsupermercado.dto.SaleResDTO;

public interface ISaleService {

  SaleResDTO save(SaleReqDTO saleReqDTO);
}
