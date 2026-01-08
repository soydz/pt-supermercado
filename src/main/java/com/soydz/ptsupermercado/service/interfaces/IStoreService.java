package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;

public interface IStoreService {

  StoreResDTO save(StoreReqDTO storeReqDTO);
}
