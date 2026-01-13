package com.soydz.ptsupermercado.controller;

import com.soydz.ptsupermercado.dto.ApiErrorResDTO;
import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.service.interfaces.IStoreService;
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
    name = "Store",
    description = "Endpoints for managing Store (create, retrieve, update and delete)")
@RestController
@RequestMapping("/api/v1/sucursales")
public class StoreController {

  private final IStoreService storeService;

  public StoreController(IStoreService storeService) {
    this.storeService = storeService;
  }

  @Operation(
      summary = "Create a store",
      description = "Creates a new store and return the created resource")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "store successfully created",
        content = @Content(schema = @Schema(implementation = StoreResDTO.class))),
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
                              "path": "/api/v1/sucursales",
                              "message": "store with name Olaya cali already exists",
                              "errors": []
                            }
                          """)
                }))
  })
  @PostMapping
  public ResponseEntity<StoreResDTO> save(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
              required = true,
              description = "store data to create",
              content =
                  @Content(
                      schema = @Schema(implementation = StoreReqDTO.class),
                      examples = {
                        @ExampleObject(
                            name = "Valid Request",
                            value =
                                """
                                  {
                                    "name": "Olaya cali",
                                    "address": "Carrera 12 # 26-03",
                                    "city": "Cali"
                                  }
                                """)
                      }))
          @Valid @RequestBody
          StoreReqDTO storeReqDTO) {
    StoreResDTO res = storeService.save(storeReqDTO);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(res.id())
            .toUri();

    return ResponseEntity.created(location).body(res);
  }

  @Operation(summary = "Get all stores", description = "Returns a list of all available stores")
  @ApiResponse(
      responseCode = "200",
      description = "Stores found",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = StoreResDTO.class),
              examples = {
                @ExampleObject(
                    name = "Stores found",
                    value =
                        """
                            [
                              {
                                "id": 1,
                                "name": "Olaya cali",
                                "address": "Carrera 12 # 26-03",
                                "city": "Cali"
                              }
                            ]
                        """)
              }))
  @GetMapping
  public ResponseEntity<List<StoreResDTO>> findByAll() {
    return ResponseEntity.ok(storeService.findAll());
  }

  @Operation(summary = "Update a store", description = "Updates an existing store by its ID ")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Store successfully update",
        content = @Content(schema = @Schema(implementation = StoreResDTO.class))),
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
                              "timestamp": "2026-01-08T23:58:36.077570Z",
                              "path": "/api/v1/sucursales/1",
                              "message": "Validation failed",
                              "errors": [
                                {
                                  "field": "name",
                                  "message": "no debe estar vacío"
                                }
                              ]
                            }
                          """)
                })),
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
                              "timestamp": "2026-01-08T23:57:20.787467Z",
                              "path": "/api/v1/sucursales/7",
                              "message": "Store with id 7 not found",
                              "errors": []
                            }
                          """)
                }))
  })
  @PutMapping("/{id}")
  public ResponseEntity<StoreResDTO> update(
      @Parameter(description = "Store id", example = "7", required = true) @PathVariable("id")
          Long id,
      @Valid @RequestBody StoreReqDTO storeReqDTO) {
    return ResponseEntity.ok(storeService.update(storeReqDTO, id));
  }
}
