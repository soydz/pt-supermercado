package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.dto.SaleReqDTO;
import com.soydz.ptsupermercado.dto.SaleResDTO;
import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.Sale;
import com.soydz.ptsupermercado.entity.SalesDetails;
import com.soydz.ptsupermercado.entity.Store;
import com.soydz.ptsupermercado.repository.ISaleRepository;
import com.soydz.ptsupermercado.service.interfaces.IProductService;
import com.soydz.ptsupermercado.service.interfaces.ISaleService;
import com.soydz.ptsupermercado.service.interfaces.IStoreService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements ISaleService {

  private final ISaleRepository saleRepository;
  private final IStoreService storeService;
  private final IProductService productService;

  public SaleServiceImpl(
      ISaleRepository saleRepository, IStoreService storeService, IProductService productService) {
    this.saleRepository = saleRepository;
    this.storeService = storeService;
    this.productService = productService;
  }

  @Override
  public SaleResDTO save(SaleReqDTO saleReqDTO) {
    Store store = storeService.findById(saleReqDTO.storeId());

    Sale sale = new Sale();
    sale.setStore(store);

    saleReqDTO
        .detail()
        .forEach(
            detail -> {
              Product product = productService.findById(detail.productId());

              SalesDetails sd = new SalesDetails();
              sd.setProduct(product);
              sd.setQuantity(detail.quantity());

              sale.addDetail(sd);
            });

    return SaleResDTO.fromEntity(saleRepository.save(sale));
  }

  @Override
  public List<SaleResDTO> findByStoreIdAndCreationDate(Long storeId, LocalDate creationDate) {
    return saleRepository
        .findByStoreIdAndCreationDate(storeService.findById(storeId), creationDate)
        .stream()
        .map(SaleResDTO::fromEntity)
        .toList();
  }

  @Override
  public void delete(Long saleId) {
    saleRepository
        .findById(saleId)
        .ifPresent(
            sale -> {
              sale.setDeletedAt();
              saleRepository.save(sale);
            });
  }
}
