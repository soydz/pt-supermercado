package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.entity.Supplier;
import com.soydz.ptsupermercado.repository.ISupplierRepository;
import com.soydz.ptsupermercado.service.exception.SupplierNotFoundException;
import com.soydz.ptsupermercado.service.interfaces.ISupplierService;
import org.springframework.stereotype.Service;

@Service
public class SupplierServiceImpl implements ISupplierService {

  private final ISupplierRepository supplierRepository;

  public SupplierServiceImpl(ISupplierRepository supplierRepository) {
    this.supplierRepository = supplierRepository;
  }

  @Override
  public Supplier findById(Long id) {
    return supplierRepository.findById(id).orElseThrow(() -> new SupplierNotFoundException(id));
  }
}
