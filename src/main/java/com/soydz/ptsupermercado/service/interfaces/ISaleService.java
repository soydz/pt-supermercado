package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.SaleReqDTO;
import com.soydz.ptsupermercado.dto.SaleResDTO;
import java.time.LocalDate;
import java.util.List;

public interface ISaleService {

  SaleResDTO save(SaleReqDTO saleReqDTO);

  List<SaleResDTO> findByStoreIdAndCreationDate(Long storeId, LocalDate creationDate);

  void delete(Long saleId);
}
