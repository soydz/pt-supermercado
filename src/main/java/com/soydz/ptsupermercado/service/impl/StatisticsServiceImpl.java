package com.soydz.ptsupermercado.service.impl;

import com.soydz.ptsupermercado.dto.BestSellingProductResDTO;
import com.soydz.ptsupermercado.entity.Product;
import com.soydz.ptsupermercado.entity.SalesDetails;
import com.soydz.ptsupermercado.repository.ISalesDetailsRepository;
import com.soydz.ptsupermercado.service.exception.NoProductsSoldException;
import com.soydz.ptsupermercado.service.interfaces.IStatisticsService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

  private final ISalesDetailsRepository salesDetailsRepository;

  public StatisticsServiceImpl(ISalesDetailsRepository salesDetailsRepository) {
    this.salesDetailsRepository = salesDetailsRepository;
  }

  @Override
  public List<BestSellingProductResDTO> bestSellingProduct() {
    Map<Product, Integer> totals =
        salesDetailsRepository.findAll().stream()
            .collect(
                Collectors.groupingBy(
                    SalesDetails::getProduct, Collectors.summingInt(SalesDetails::getQuantity)));

    int maxSold =
        totals.values().stream().max(Integer::compareTo).orElseThrow(NoProductsSoldException::new);

    return totals.entrySet().stream()
        .filter(entry -> entry.getValue() == maxSold)
        .map(BestSellingProductResDTO::fromMapEntry)
        .toList();
  }
}
