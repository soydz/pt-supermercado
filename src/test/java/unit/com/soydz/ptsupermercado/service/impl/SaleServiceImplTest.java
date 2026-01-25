package unit.com.soydz.ptsupermercado.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.soydz.ptsupermercado.dto.ProductDetailsDTO;
import com.soydz.ptsupermercado.dto.SaleReqDTO;
import com.soydz.ptsupermercado.dto.SaleResDTO;
import com.soydz.ptsupermercado.entity.*;
import com.soydz.ptsupermercado.repository.ISaleRepository;
import com.soydz.ptsupermercado.service.exception.StoreNotFoundException;
import com.soydz.ptsupermercado.service.impl.SaleServiceImpl;
import com.soydz.ptsupermercado.service.interfaces.IProductService;
import com.soydz.ptsupermercado.service.interfaces.IStoreService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

  @Mock private ISaleRepository saleRepository;
  @Mock private IStoreService storeService;
  @Mock private IProductService productService;

  @InjectMocks private SaleServiceImpl saleService;

  @Test
  void shouldSaveSaleWithDetailsSuccessfully() {
    // Given
    Store store = new Store();
    store.setId(1L);

    Product product = new Product();
    product.setId(3L);

    SaleReqDTO saleReqDTO = new SaleReqDTO(1L, List.of(new ProductDetailsDTO(3L, 3)));

    // When
    when(storeService.findById(1L)).thenReturn(store);
    when(productService.findById(3L)).thenReturn(product);
    when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

    SaleResDTO result = saleService.save(saleReqDTO);

    // Then
    assertNotNull(result);

    verify(storeService).findById(1L);
    verify(productService).findById(3L);
    verify(saleRepository).save(any(Sale.class));
  }

  @Test
  void shouldSaveSaleOnlyOnce() {
    // Given
    SaleReqDTO saleReqDTO = new SaleReqDTO(5L, List.of(new ProductDetailsDTO(23L, 4)));

    // When
    when(storeService.findById(anyLong())).thenReturn(new Store());
    when(productService.findById(anyLong())).thenReturn(new Product());
    when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));

    saleService.save(saleReqDTO);

    // Then
    verify(saleRepository, times(1)).save(any(Sale.class));
  }

  @Test
  void shouldIncreaseQuantityWhenProductIsRepeated() {
    // Given
    Product product = new Product();
    product.setId(9L);

    SaleReqDTO saleReqDTO =
        new SaleReqDTO(1L, List.of(new ProductDetailsDTO(9L, 2), new ProductDetailsDTO(9L, 5)));

    // When
    when(storeService.findById(1L)).thenReturn(new Store());
    when(productService.findById(9L)).thenReturn(product);
    when(saleRepository.save(any(Sale.class))).thenAnswer(invocate -> invocate.getArgument(0));

    SaleResDTO result = saleService.save(saleReqDTO);

    // Then
    assertEquals(1, result.salesDetails().size());
    assertEquals(7, result.salesDetails().getFirst().quantity());
  }

  @Test
  void shouldTrowExceptionWhenStoreNotFound() {
    // Given
    Long storeId = 73L;

    SaleReqDTO saleReqDTO = new SaleReqDTO(storeId, List.of(new ProductDetailsDTO(5L, 2)));

    // When
    when(storeService.findById(storeId)).thenThrow(new StoreNotFoundException(storeId));

    // Then
    assertThrows(StoreNotFoundException.class, () -> saleService.save(saleReqDTO));

    verify(saleRepository, never()).save(any());
  }

  @Test
  void shouldReturnSalesWhenFilterByStoreIdAndCreationDate() {
    // Given
    Long storeId = 6L;
    LocalDate date = LocalDate.of(2026, 1, 15);

    Store store = new Store();
    store.setId(storeId);
    store.setName("Primavera");
    store.setAddress("Calle 12 # 19-11");
    store.setCity("Cartagena");

    Supplier supplier = new Supplier();
    supplier.setId(1L);
    supplier.setName("Granos el trigal");
    supplier.setEmail("clientes@granosdeltolima.com");

    Product product = new Product();
    product.setId(2L);
    product.setName("Arroz la abundancia");
    product.setCategory("Granos");
    product.setPrice(BigDecimal.valueOf(1850));
    product.setSupplier(supplier);

    Sale sale = new Sale();
    sale.setStore(store);

    SalesDetails salesDetails = new SalesDetails(sale, product, 5);

    sale.setSalesDetails(Set.of(salesDetails));

    // When
    when(storeService.findById(storeId)).thenReturn(store);
    when(saleRepository.findByStoreIdAndCreationDate(store, date)).thenReturn(List.of(sale));

    List<SaleResDTO> result = saleService.findByStoreIdAndCreationDate(storeId, date);

    // Then
    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(storeId, result.getFirst().storeId());
    assertEquals(store.getName(), result.getFirst().storeName());
    assertEquals(
        salesDetails.getProduct().getName(),
        result.getFirst().salesDetails().getFirst().productName());
    assertEquals(
        salesDetails.getQuantity(), result.getFirst().salesDetails().getFirst().quantity());
    assertEquals(
        salesDetails.getProduct().getPrice(),
        result.getFirst().salesDetails().getFirst().productPrice());

    verify(storeService).findById(storeId);
    verify(saleRepository).findByStoreIdAndCreationDate(store, date);
  }

  @Test
  void findByStoreIdAndCreationDateWhenNotSalesFoundShouldReturnEmptyList() {
    // Given
    Long storeId = 4L;
    LocalDate creationDate = LocalDate.of(2026, 1, 12);
    Store store = new Store();
    store.setId(storeId);

    // When
    when(storeService.findById(storeId)).thenReturn(store);
    when(saleRepository.findByStoreIdAndCreationDate(store, creationDate)).thenReturn(List.of());

    List<SaleResDTO> result = saleService.findByStoreIdAndCreationDate(storeId, creationDate);

    // Then
    assertTrue(result.isEmpty());
    verify(storeService).findById(storeId);
    verify(saleRepository).findByStoreIdAndCreationDate(store, creationDate);
    verify(saleRepository).findByStoreIdAndCreationDate(store, creationDate);
  }

  @Test
  void shouldMarkSaleAsDeletedWhenExists() {
    // Given
    Long saleId = 2L;
    Sale sale = new Sale();
    sale.setId(saleId);

    // When
    when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

    saleService.delete(saleId);

    // Then
    assertNotNull(sale.getDeletedAt());
    verify(saleRepository).save(sale);
  }

  @Test
  void shouldDoNothingWhenSaleDoesNotExist() {
    // Given
    Long saleId = 3L;

    // When
    when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

    saleService.delete(saleId);

    // Then
    verify(saleRepository, never()).save(any());
  }
}
