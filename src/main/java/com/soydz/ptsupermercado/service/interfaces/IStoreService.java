package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.entity.Store;
import java.util.List;

public interface IStoreService {

  StoreResDTO save(StoreReqDTO storeReqDTO);

  List<StoreResDTO> findAll();

  Store findById(Long id);

  StoreResDTO update(StoreReqDTO storeReqDTO, Long id);

  void delete(Long id);
}
