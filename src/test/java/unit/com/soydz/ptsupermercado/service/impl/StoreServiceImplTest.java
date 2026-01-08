package unit.com.soydz.ptsupermercado.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.soydz.ptsupermercado.dto.StoreReqDTO;
import com.soydz.ptsupermercado.dto.StoreResDTO;
import com.soydz.ptsupermercado.entity.Store;
import com.soydz.ptsupermercado.repository.IStoreRepository;
import com.soydz.ptsupermercado.service.exception.StoreDuplicateNameException;
import com.soydz.ptsupermercado.service.impl.StoreServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreServiceImplTest {

  @Mock private IStoreRepository storeRepository;

  @InjectMocks private StoreServiceImpl storeService;

  private StoreReqDTO storeReqDTO;

  @BeforeEach
  void setup() {
    storeReqDTO = new StoreReqDTO("Torreon", "calle 12 # 42-11", "Barranquilla");
  }

  @Test
  void shouldReturnStoreWhenSaveIsSuccessful() {
    // Given
    Store store = StoreReqDTO.toEntity(storeReqDTO);

    // When
    when(storeRepository.save(any(Store.class))).thenReturn(store);

    StoreResDTO result = storeService.save(storeReqDTO);

    // Then
    verify(storeRepository)
        .save(
            argThat(
                storeArg ->
                    storeArg.getName().equals(storeReqDTO.name())
                        && storeArg.getAddress().equals(storeReqDTO.address())
                        && storeArg.getCity().equals(storeReqDTO.city())));

    assertNotNull(result);
    assertEquals(storeReqDTO.name(), result.name());
    assertEquals(storeReqDTO.address(), result.address());
    assertEquals(storeReqDTO.city(), result.city());
  }

  @Test
  void shouldReturnStoreWhenExistByNameIsSuccessful() {

    when(storeRepository.existsByName(any(String.class))).thenReturn(true);

    assertThrows(StoreDuplicateNameException.class, () -> storeService.save(storeReqDTO));
  }

  @Test
  void shouldReturnStoreResDTOListWhenFindAllIsSuccessful() {
    // Given
    Store store = StoreReqDTO.toEntity(storeReqDTO);

    // When
    when(storeRepository.findAll()).thenReturn(List.of(store));

    List<StoreResDTO> result = storeService.findAll();

    // Then
    verify(storeRepository).findAll();

    assertNotNull(result);
    assertEquals(1, result.size());
    assertEquals(store.getId(), result.getFirst().id());
    assertEquals(store.getName(), result.getFirst().name());
    assertEquals(store.getAddress(), result.getFirst().address());
    assertEquals(store.getCity(), result.getFirst().city());
  }
}
