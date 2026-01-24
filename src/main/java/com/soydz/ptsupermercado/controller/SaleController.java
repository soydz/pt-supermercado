package com.soydz.ptsupermercado.controller;

import com.soydz.ptsupermercado.dto.*;
import com.soydz.ptsupermercado.service.interfaces.ISaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

  @Operation(
      summary = "Find sales by store and creation date",
      description =
          "Retrieve a list of sales based on the provided store id and creation date. The stores are the filtered by the store id and the date the sale was created",
      parameters = {
        @Parameter(
            name = "sucursalId",
            description = "The id of store to filter sales by",
            required = true,
            example = "1"),
        @Parameter(
            name = "fecha",
            description =
                "The creation date of the sales, in the format 'yyyy-MM-dd'. Only sales created on this date will be returned",
            required = true,
            example = "2026-01-17")
      })
  @ApiResponse(
      responseCode = "200",
      description = "Successfully retrieved the sales list",
      content =
          @Content(
              mediaType = "application/json",
              array = @ArraySchema(schema = @Schema(implementation = SaleResDTO.class))))
  @ApiResponse(
      responseCode = "400",
      description = "Bad request: Missing or invalid parameters",
      content =
          @Content(
              mediaType = "application/json",
              examples = {
                @ExampleObject(
                    name = "Bad Request",
                    value =
                        """
                            {
                              "timestamp": "2026-01-17T22:01:52.607688Z",
                              "path": "/api/v1/ventas",
                              "message": "Text '2026-01-13  e' could not be parsed, unparsed text found at index 10",
                              "errors": []
                            }
                        """)
              }))
  @ApiResponse(
      responseCode = "404",
      description = "No sales found for the given store ID and date",
      content =
          @Content(
              mediaType = "application/json",
              examples = {
                @ExampleObject(
                    name = "Not found",
                    value =
                        """
                            {
                              "timestamp": "2026-01-17T21:48:40.237671Z",
                              "path": "/api/v1/ventas",
                              "message": "Store with id 9 not found",
                              "errors": []
                            }
                        """)
              }))
  @GetMapping
  public ResponseEntity<List<SaleResDTO>> findByStoreIdAndDate(
      @RequestParam(name = "sucursalId") Long storeId,
      @RequestParam(name = "fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
    return ResponseEntity.ok(saleService.findByStoreIdAndCreationDate(storeId, date));
  }
}
