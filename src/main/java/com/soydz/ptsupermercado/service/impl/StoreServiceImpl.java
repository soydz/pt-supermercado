package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.repository.IStoreRepository;
import com.soydz.ptsupermercado.service.exception.StoreDuplicateNameException;
import com.soydz.ptsupermercado.service.interfaces.IStoreService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements IStoreService {

  private final IStoreRepository storeRepository;

  public StoreServiceImpl(IStoreRepository storeRepository) {
    this.storeRepository = storeRepository;
  }

  @Override
  public StoreResDTO save(StoreReqDTO storeReqDTO) {
    if (storeRepository.existsByName(storeReqDTO.name())) {
      throw new StoreDuplicateNameException(storeReqDTO.name());
    }

    return StoreResDTO.fromEntity(storeRepository.save(StoreReqDTO.toEntity(storeReqDTO)));
  }

  @Override
  public List<StoreResDTO> findAll() {
    return storeRepository.findAll().stream().map(StoreResDTO::fromEntity).toList();
  }
}
