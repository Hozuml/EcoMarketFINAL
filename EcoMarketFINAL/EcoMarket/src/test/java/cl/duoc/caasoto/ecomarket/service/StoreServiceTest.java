package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.Store;
import cl.duoc.caasoto.ecomarket.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private Store store1;
    private Store store2;

    @BeforeEach
    void setUp() {
        store1 = new Store(1, "Tienda Central", "Av. Principal 123", "Santiago", "987654321");
        store2 = new Store(2, "Tienda Norte", "Calle Norte 456", "Valparaíso", "123456789");
    }

    @Test
    void testListarTiendas_DeberiaRetornarListaDeTiendas() {
        // Arrange
        List<Store> expectedStores = Arrays.asList(store1, store2);
        when(storeRepository.findAll()).thenReturn(expectedStores);

        // Act
        List<Store> actualStores = storeService.listarTiendas();

        // Assert
        assertEquals(2, actualStores.size());
        assertEquals(expectedStores, actualStores);
        verify(storeRepository, times(1)).findAll();
    }

    @Test
    void testListarTiendas_DeberiaRetornarListaVacia() {
        // Arrange
        when(storeRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Store> actualStores = storeService.listarTiendas();

        // Assert
        assertTrue(actualStores.isEmpty());
        verify(storeRepository, times(1)).findAll();
    }

    @Test
    void testObtenerTienda_ConIdExistente_DeberiaRetornarStore() {
        // Arrange
        int storeId = 1;
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store1));

        // Act
        Optional<Store> result = storeService.obtenerTienda(storeId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(store1, result.get());
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    void testObtenerTienda_ConIdInexistente_DeberiaRetornarEmpty() {
        // Arrange
        int storeId = 999;
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // Act
        Optional<Store> result = storeService.obtenerTienda(storeId);

        // Assert
        assertFalse(result.isPresent());
        verify(storeRepository, times(1)).findById(storeId);
    }

    @Test
    void testCrearTienda_DeberiaGuardarYRetornarTienda() {
        // Arrange
        Store newStore = new Store(0, "Nueva Tienda", "Calle Nueva 789", "Concepción", "555666777");
        Store savedStore = new Store(3, "Nueva Tienda", "Calle Nueva 789", "Concepción", "555666777");
        when(storeRepository.save(newStore)).thenReturn(savedStore);

        // Act
        Store result = storeService.crearTienda(newStore);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.getId());
        assertEquals("Nueva Tienda", result.getNombre());
        assertEquals("Calle Nueva 789", result.getDireccion());
        assertEquals("Concepción", result.getCiudad());
        assertEquals("555666777", result.getTelefono());
        verify(storeRepository, times(1)).save(newStore);
    }

    @Test
    void testEliminarTienda_ConIdExistente_DeberiaRetornarTrue() {
        // Arrange
        int storeId = 1;
        when(storeRepository.existsById(storeId)).thenReturn(true);

        // Act
        boolean result = storeService.eliminarTienda(storeId);

        // Assert
        assertTrue(result);
        verify(storeRepository, times(1)).existsById(storeId);
        verify(storeRepository, times(1)).deleteById(storeId);
    }

    @Test
    void testEliminarTienda_ConIdInexistente_DeberiaRetornarFalse() {
        // Arrange
        int storeId = 999;
        when(storeRepository.existsById(storeId)).thenReturn(false);

        // Act
        boolean result = storeService.eliminarTienda(storeId);

        // Assert
        assertFalse(result);
        verify(storeRepository, times(1)).existsById(storeId);
        verify(storeRepository, never()).deleteById(anyInt());
    }

    @Test
    void testActualizarTienda_ConIdExistente_DeberiaActualizarYRetornarTienda() {
        // Arrange
        int storeId = 1;
        Store updatedData = new Store(0, "Tienda Actualizada", "Nueva Dirección 999", "Nueva Ciudad", "999888777");
        Store existingStore = new Store(1, "Tienda Vieja", "Dirección Vieja", "Ciudad Vieja", "111222333");
        Store savedStore = new Store(1, "Tienda Actualizada", "Nueva Dirección 999", "Nueva Ciudad", "999888777");

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existingStore));
        when(storeRepository.save(any(Store.class))).thenReturn(savedStore);

        // Act
        Store result = storeService.actualizarTienda(storeId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Tienda Actualizada", result.getNombre());
        assertEquals("Nueva Dirección 999", result.getDireccion());
        assertEquals("Nueva Ciudad", result.getCiudad());
        assertEquals("999888777", result.getTelefono());
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void testActualizarTienda_ConIdInexistente_DeberiaRetornarNull() {
        // Arrange
        int storeId = 999;
        Store updatedData = new Store(0, "Tienda Actualizada", "Nueva Dirección 999", "Nueva Ciudad", "999888777");
        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // Act
        Store result = storeService.actualizarTienda(storeId, updatedData);

        // Assert
        assertNull(result);
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, never()).save(any(Store.class));
    }

    @Test
    void testActualizarTienda_ConDatosNulos_DeberiaActualizarSoloConValoresNoNulos() {
        // Arrange
        int storeId = 1;
        Store updatedData = new Store(0, null, "Nueva Dirección", null, "999888777");
        Store existingStore = new Store(1, "Tienda Original", "Dirección Original", "Ciudad Original", "111222333");

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(existingStore));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Store result = storeService.actualizarTienda(storeId, updatedData);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertNull(result.getNombre()); // Se actualiza con null
        assertEquals("Nueva Dirección", result.getDireccion());
        assertNull(result.getCiudad()); // Se actualiza con null
        assertEquals("999888777", result.getTelefono());
        verify(storeRepository, times(1)).findById(storeId);
        verify(storeRepository, times(1)).save(any(Store.class));
    }
}