package com.soydz.ptsupermercado.controller;

import com.soydz.ptsupermercado.dto.*;
import com.soydz.ptsupermercado.service.interfaces.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Sale", description = "Endpoints for managing sales (create, retrieve and delete)")
@RestController
@RequestMapping("/api/v1/ventas")
public class SaleController {

  private final ISaleService saleService;

  public SaleController(ISaleService saleService) {
    this.saleService = saleService;
  }

  @Operation(
      summary = "Create a sale",
      description = "Creates a new sale and return the created resource")
  @ApiResponse(
      responseCode = "201",
      description = "Sale successfully created",
      content = @Content(schema = @Schema(implementation = SaleResDTO.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Validation failed",
      content =
          @Content(
              schema = @Schema(implementation = ApiErrorResDTO.class),
              examples = {
                @ExampleObject(
                    name = "Validation failed",
                    value =
                        """
                            {
                              "timestamp": "2026-01-15T16:27:26.046540Z",
                              "path": "/api/v1/ventas",
                              "message": "Validation failed",
                              "errors": [
                                {
                                  "field": "storeId",
                                  "message": "no debe ser nulo"
                                },
                                {
                                  "field": "detail[0].productId",
                                  "message": "debe ser mayor que 0"
                                },
                                {
                                  "field": "detail[0].quantity",
                                  "message": "debe ser mayor que o igual a 1"
                                }
                              ]
                            }
                        """)
              }))
  @ApiResponse(
      responseCode = "404",
      description = "Store not found",
      content =
          @Content(
              schema = @Schema(implementation = ApiErrorResDTO.class),
              examples = {
                @ExampleObject(
                    name = "StoreNotFound",
                    value =
                        """
                            {
                              "timestamp": "2026-01-15T15:52:31.438739Z",
                              "path": "/api/v1/ventas",
                              "message": "Store with id 7 not found",
                              "errors": []
                            }
                        """)
              }))
  @PostMapping
  public ResponseEntity<SaleResDTO> save(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              required = true,
              description = "Sale data to create",
              content =
                  @Content(
                      schema = @Schema(implementation = SaleReqDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Valid Request",
                            value =
                                """
                                  {
                                    "storeId": 7,
                                    "detail": [
                                      {
                                        "productId": 17,
                                        "quantity": 2
                                      }
                                    ]
                                  }
                                """)
                      }))
          @Valid @RequestBody
          SaleReqDTO saleReqDTO) {

    SaleResDTO saleResDTO = saleService.save(saleReqDTO);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/id")
            .buildAndExpand(saleResDTO.id())
            .toUri();

    return ResponseEntity.created(location).body(saleResDTO);
  }
}
