package com.soydz.ptsupermercado.controller;

import com.soydz.ptsupermercado.dto.ApiErrorResDTO;
import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.service.interfaces.IProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(
    name = "Products",
    description = "Endpoints for managing products (create, retrieve, update and delete)")
@RestController
@RequestMapping("/api/v1/productos")
public class ProductController {

  private final IProductService productService;

  public ProductController(IProductService productService) {
    this.productService = productService;
  }

  @Operation(
      summary = "Create a product",
      description = "Creates a new product and return the created resource")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Product successfully created",
        content = @Content(schema = @Schema(implementation = ProductResDTO.class))),
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
                              "timestamp": "2026-01-06T17:07:58.954033Z",
                              "path": "/api/v1/productos",
                              "message": "Validation failed",
                              "errors": [
                                {
                                  "field": "price",
                                  "message": "debe ser mayor que 0"
                                }
                              ]
                            }
                          """)
                }))
  })
  @PostMapping
  public ResponseEntity<ProductResDTO> save(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              required = true,
              description = "Product data to create",
              content =
                  @Content(
                      schema = @Schema(implementation = ProductReqDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Valid Request",
                            value =
                                """
                                {
                                  "name": "Arroz",
                                  "category": "Granos",
                                  "price": 10,
                                  "supplierId": 1
                                }
                                """)
                      }))
          @Valid @RequestBody
          ProductReqDTO productReqDTO) {

    ProductResDTO res = productService.save(productReqDTO);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(res.id())
            .toUri();

    return ResponseEntity.created(location).body(res);
  }

  @Operation(summary = "Get all products", description = "Returns a list of all available products")
  @ApiResponse(
      responseCode = "200",
      description = "Products found",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ProductResDTO.class),
              examples = {
                @ExampleObject(
                    name = "Products found",
                    value =
                        """
                        [
                            {
                              "id": "13",
                              "name": "Arroz floral",
                              "category": "Granos",
                              "price": 10.5,
                              "supplierName": "Granos del sol"
                            }
                        ]
                        """)
              }))
  @GetMapping
  public ResponseEntity<List<ProductResDTO>> findAll() {
    return ResponseEntity.ok(productService.findAll());
  }

  @Operation(summary = "Update a product", description = "Updates an existing product by its ID")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Product successfully update",
        content = @Content(schema = @Schema(implementation = ProductResDTO.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Validation failed",
        content = @Content(schema = @Schema(implementation = ApiErrorResDTO.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Product not found",
        content =
            @Content(
                schema = @Schema(implementation = ApiErrorResDTO.class),
                examples = {
                  @ExampleObject(
                      name = "ProductNotFound",
                      value =
                          """
                            {
                              "timestamp": "2026-01-06T14:33:26.837426Z",
                              "path": "/api/v1/productos/10",
                              "message": "Product with id 10 not found",
                              "errors": []
                            }
                          """)
                }))
  })
  @PutMapping("/{id}")
  public ResponseEntity<ProductResDTO> update(
      @Parameter(description = "Product ID", example = "10", required = true) @PathVariable("id")
          Long id,
      @Valid @RequestBody ProductReqDTO productReqDTO) {
    return ResponseEntity.ok(productService.update(productReqDTO, id));
  }

  @Operation(summary = "Delete a product", description = "Delete a product by its ID")
  @ApiResponse(responseCode = "204", description = "Product successfully deleted")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @Parameter(description = "Product ID", example = "10", required = true) @PathVariable("id")
          Long id) {
    productService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
