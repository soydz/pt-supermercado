package com.soydz.ptsupermercado.service.interfaces;

import com.soydz.ptsupermercado.dto.BestSellingProductResDTO;
import java.util.List;

public interface IStatisticsService {
  List<BestSellingProductResDTO> bestSellingProduct();
}
