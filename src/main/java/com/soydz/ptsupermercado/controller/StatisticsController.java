package com.soydz.ptsupermercado.controller;

import com.soydz.ptsupermercado.dto.BestSellingProductResDTO;
import com.soydz.ptsupermercado.service.interfaces.IStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Statistics", description = "Endpoints for statistics")
@RestController
@RequestMapping("/api/v1/estadisticas")
public class StatisticsController {

  private final IStatisticsService statisticsService;

  public StatisticsController(IStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @Operation(
      summary = "Best selling product",
      description = "Obtains the product or products best selling")
  @ApiResponse(
      responseCode = "200",
      description = "Best selling product found",
      content = @Content(schema = @Schema(implementation = BestSellingProductResDTO.class)))
  @GetMapping("/producto-mas-vendido")
  public ResponseEntity<List<BestSellingProductResDTO>> bestSellingProduct() {
    return ResponseEntity.ok(statisticsService.bestSellingProduct());
  }
}
