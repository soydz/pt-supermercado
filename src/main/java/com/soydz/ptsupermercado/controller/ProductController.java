package com.soydz.ptsupermercado.controller;

import com.soydz.ptsupermercado.dto.ProductReqDTO;
import com.soydz.ptsupermercado.dto.ProductResDTO;
import com.soydz.ptsupermercado.service.interfaces.IProductService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductController {

  private final IProductService productService;

  public ProductController(IProductService productService) {
    this.productService = productService;
  }

  @PostMapping
  public ResponseEntity<ProductResDTO> save(@Valid @RequestBody ProductReqDTO productReqDTO) {
    ProductResDTO res = productService.save(productReqDTO);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(res.id())
            .toUri();

    return ResponseEntity.created(location).body(res);
  }

  @GetMapping
  public ResponseEntity<List<ProductResDTO>> findAll() {
    return ResponseEntity.ok(productService.findAll());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResDTO> update(
      @PathVariable("id") Long id, @Valid @RequestBody ProductReqDTO productReqDTO) {
    return ResponseEntity.ok(productService.update(productReqDTO, id));
  }
}
