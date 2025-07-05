package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.Order;
import cl.duoc.caasoto.ecomarket.model.User;
import cl.duoc.caasoto.ecomarket.model.Product;
import cl.duoc.caasoto.ecomarket.repository.OrderRepository;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order1;
    private Order order2;
    private Order orderActualizado;
    private User user1;
    private User user2;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    void setUp() {
        // Configuración de usuarios de prueba
        user1 = new User();
        user1.setId(1);
        user1.setNombre("Juan");
        user1.setApellido("Pérez");
        user1.setEmail("juan.perez@email.com");

        user2 = new User();
        user2.setId(2);
        user2.setNombre("María");
        user2.setApellido("González");
        user2.setEmail("maria.gonzalez@email.com");

        // Configuración de productos de prueba
        product1 = new Product();
        product1.setId(1);
        product1.setNombre("Producto A");
        product1.setPrecio(100.0);

        product2 = new Product();
        product2.setId(2);
        product2.setNombre("Producto B");
        product2.setPrecio(200.0);

        product3 = new Product();
        product3.setId(3);
        product3.setNombre("Producto C");
        product3.setPrecio(150.0);

        // Configuración de pedidos de prueba
        order1 = new Order();
        order1.setId(1);
        order1.setUser(user1);
        order1.setProducts(Arrays.asList(product1, product2));
        order1.setEstado("PENDIENTE");
        order1.setDireccionEnvio("Calle 123, Ciudad");

        order2 = new Order();
        order2.setId(2);
        order2.setUser(user2);
        order2.setProducts(Arrays.asList(product3));
        order2.setEstado("ENVIADO");
        order2.setDireccionEnvio("Avenida 456, Ciudad");

        orderActualizado = new Order();
        orderActualizado.setUser(user2);
        orderActualizado.setProducts(Arrays.asList(product2, product3));
        orderActualizado.setEstado("ENTREGADO");
        orderActualizado.setDireccionEnvio("Nueva Dirección 789");
    }

    @Test
    void testListarPedidos_DeberiaRetornarListaDePedidos() {
        // Arrange
        List<Order> expectedOrders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        // Act
        List<Order> actualOrders = orderService.listarPedidos();

        // Assert
        assertNotNull(actualOrders);
        assertEquals(2, actualOrders.size());
        assertEquals(expectedOrders, actualOrders);
        assertEquals("Juan", actualOrders.get(0).getUser().getNombre());
        assertEquals("María", actualOrders.get(1).getUser().getNombre());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testListarPedidos_DeberiaRetornarListaVacia() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Order> actualOrders = orderService.listarPedidos();

        // Assert
        assertNotNull(actualOrders);
        assertTrue(actualOrders.isEmpty());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPedido_DeberiaRetornarPedidoExistente() {
        // Arrange
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));

        // Act
        Optional<Order> actualOrder = orderService.obtenerPedido(1);

        // Assert
        assertTrue(actualOrder.isPresent());
        assertEquals(order1, actualOrder.get());
        assertEquals("Juan", actualOrder.get().getUser().getNombre());
        assertEquals(2, actualOrder.get().getProducts().size());
        assertEquals("PENDIENTE", actualOrder.get().getEstado());
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void testObtenerPedido_DeberiaRetornarVacioSiNoExiste() {
        // Arrange
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Order> actualOrder = orderService.obtenerPedido(999);

        // Assert
        assertFalse(actualOrder.isPresent());
        verify(orderRepository, times(1)).findById(999);
    }

    @Test
    void testCrearPedido_DeberiaGuardarYRetornarPedido() {
        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(order1);

        // Act
        Order actualOrder = orderService.crearPedido(order1);

        // Assert
        assertNotNull(actualOrder);
        assertEquals(order1, actualOrder);
        assertEquals("Juan", actualOrder.getUser().getNombre());
        assertEquals("PENDIENTE", actualOrder.getEstado());
        assertEquals(2, actualOrder.getProducts().size());
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void testCrearPedido_ConPedidoNulo_DeberiaLanzarExcepcion() {
        // Arrange
        when(orderRepository.save(null)).thenThrow(new IllegalArgumentException("Order cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.crearPedido(null);
        });
        verify(orderRepository, times(1)).save(null);
    }

    @Test
    void testEliminarPedido_DeberiaRetornarTrueSiExiste() {
        // Arrange
        when(orderRepository.existsById(1)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1);

        // Act
        boolean resultado = orderService.eliminarPedido(1);

        // Assert
        assertTrue(resultado);
        verify(orderRepository, times(1)).existsById(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarPedido_DeberiaRetornarFalseSiNoExiste() {
        // Arrange
        when(orderRepository.existsById(999)).thenReturn(false);

        // Act
        boolean resultado = orderService.eliminarPedido(999);

        // Assert
        assertFalse(resultado);
        verify(orderRepository, times(1)).existsById(999);
        verify(orderRepository, never()).deleteById(anyInt());
    }

    @Test
    void testActualizarPedido_DeberiaActualizarPedidoExistente() {
        // Arrange
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderRepository.save(any(Order.class))).thenReturn(order1);

        // Act
        Order resultado = orderService.actualizarPedido(1, orderActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(user2, order1.getUser());
        assertEquals("María", order1.getUser().getNombre());
        assertEquals(2, order1.getProducts().size());
        assertEquals("ENTREGADO", order1.getEstado());
        assertEquals("Nueva Dirección 789", order1.getDireccionEnvio());
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void testActualizarPedido_DeberiaRetornarNullSiNoExiste() {
        // Arrange
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Order resultado = orderService.actualizarPedido(999, orderActualizado);

        // Assert
        assertNull(resultado);
        verify(orderRepository, times(1)).findById(999);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testActualizarPedido_ConDatosNulos_DeberiaActualizarCorrectamente() {
        // Arrange
        Order orderConNulos = new Order();
        orderConNulos.setUser(null);
        orderConNulos.setProducts(null);
        orderConNulos.setEstado(null);
        orderConNulos.setDireccionEnvio(null);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderRepository.save(any(Order.class))).thenReturn(order1);

        // Act
        Order resultado = orderService.actualizarPedido(1, orderConNulos);

        // Assert
        assertNotNull(resultado);
        assertNull(order1.getUser());
        assertNull(order1.getProducts());
        assertNull(order1.getEstado());
        assertNull(order1.getDireccionEnvio());
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void testActualizarPedido_ConPedidoActualizadoNulo_DeberiaLanzarExcepcion() {
        // Arrange
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            orderService.actualizarPedido(1, null);
        });
        verify(orderRepository, times(1)).findById(1);
    }

    @Test
    void testActualizarPedido_ConListaProductosVacia() {
        // Arrange
        Order orderConListaVacia = new Order();
        orderConListaVacia.setUser(user1);
        orderConListaVacia.setProducts(Arrays.asList());
        orderConListaVacia.setEstado("CANCELADO");
        orderConListaVacia.setDireccionEnvio("Dirección Cancelación");

        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderRepository.save(any(Order.class))).thenReturn(order1);

        // Act
        Order resultado = orderService.actualizarPedido(1, orderConListaVacia);

        // Assert
        assertNotNull(resultado);
        assertEquals(user1, order1.getUser());
        assertTrue(order1.getProducts().isEmpty());
        assertEquals("CANCELADO", order1.getEstado());
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).save(order1);
    }

    @Test
    void testIntegracionCompleta_CrearActualizarEliminar() {
        // Arrange - Crear
        when(orderRepository.save(any(Order.class))).thenReturn(order1);

        // Act - Crear
        Order pedidoCreado = orderService.crearPedido(order1);

        // Assert - Crear
        assertNotNull(pedidoCreado);
        assertEquals("Juan", pedidoCreado.getUser().getNombre());
        assertEquals(2, pedidoCreado.getProducts().size());

        // Arrange - Actualizar
        when(orderRepository.findById(1)).thenReturn(Optional.of(order1));
        when(orderRepository.save(any(Order.class))).thenReturn(order1);

        // Act - Actualizar
        Order pedidoActualizado = orderService.actualizarPedido(1, orderActualizado);

        // Assert - Actualizar
        assertNotNull(pedidoActualizado);
        assertEquals("María", order1.getUser().getNombre());
        assertEquals("ENTREGADO", order1.getEstado());

        // Arrange - Eliminar
        when(orderRepository.existsById(1)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1);

        // Act - Eliminar
        boolean eliminado = orderService.eliminarPedido(1);

        // Assert - Eliminar
        assertTrue(eliminado);

        // Verificar todas las interacciones
        verify(orderRepository, times(2)).save(any(Order.class));
        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).existsById(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    void testCrearPedido_ConRelacionesCompletas() {
        // Arrange
        Order orderCompleto = new Order();
        orderCompleto.setUser(user1);
        orderCompleto.setProducts(Arrays.asList(product1, product2, product3));
        orderCompleto.setEstado("PROCESANDO");
        orderCompleto.setDireccionEnvio("Dirección Completa 123");

        when(orderRepository.save(any(Order.class))).thenReturn(orderCompleto);

        // Act
        Order resultado = orderService.crearPedido(orderCompleto);

        // Assert
        assertNotNull(resultado);
        assertEquals(user1, resultado.getUser());
        assertEquals(3, resultado.getProducts().size());
        assertEquals("Producto A", resultado.getProducts().get(0).getNombre());
        assertEquals("Producto B", resultado.getProducts().get(1).getNombre());
        assertEquals("Producto C", resultado.getProducts().get(2).getNombre());
        assertEquals("PROCESANDO", resultado.getEstado());
        verify(orderRepository, times(1)).save(orderCompleto);
    }
}