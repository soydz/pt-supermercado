package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.entity.Store;
import com.soydz.ptsupermercado.repository.IStoreRepository;
import com.soydz.ptsupermercado.service.exception.StoreDuplicateNameException;
import com.soydz.ptsupermercado.service.exception.StoreNotFoundException;
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

  @Override
  public Store findById(Long id) {
    return storeRepository.findById(id).orElseThrow(() -> new StoreNotFoundException(id));
  }

  @Override
  public StoreResDTO update(StoreReqDTO storeReqDTO, Long id) {
    Store store = findById(id);

    store.setName(storeReqDTO.name());
    store.setAddress(storeReqDTO.name());
    store.setCity(storeReqDTO.city());

    return StoreResDTO.fromEntity(storeRepository.save(store));
  }

  @Override
  public void delete(Long id) {
    storeRepository.deleteById(id);
  }
}
