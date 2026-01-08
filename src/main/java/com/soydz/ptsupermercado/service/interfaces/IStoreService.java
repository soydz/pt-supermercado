package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import java.util.List;

public interface IStoreService {

  StoreResDTO save(StoreReqDTO storeReqDTO);

  List<StoreResDTO> findAll();
}
