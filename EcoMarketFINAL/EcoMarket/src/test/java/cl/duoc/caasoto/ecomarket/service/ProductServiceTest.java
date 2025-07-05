package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.Product;
import cl.duoc.caasoto.ecomarket.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product productActualizado;

    @BeforeEach
    void setUp() {
        // Configuración de productos de prueba
        product1 = new Product();
        product1.setId(1);
        product1.setNombre("Producto Ecológico A");
        product1.setDescripcion("Producto biodegradable de alta calidad");
        product1.setPrecio(25.99);
        product1.setStock(100);
        product1.setCategoria("Limpieza");

        product2 = new Product();
        product2.setId(2);
        product2.setNombre("Producto Ecológico B");
        product2.setDescripcion("Producto orgánico certificado");
        product2.setPrecio(15.50);
        product2.setStock(50);
        product2.setCategoria("Alimentación");

        product3 = new Product();
        product3.setId(3);
        product3.setNombre("Champú Natural");
        product3.setDescripcion("Champú sin sulfatos ni parabenos");
        product3.setPrecio(12.75);
        product3.setStock(75);
        product3.setCategoria("Cuidado Personal");

        productActualizado = new Product();
        productActualizado.setNombre("Producto Actualizado");
        productActualizado.setDescripcion("Descripción actualizada");
        productActualizado.setPrecio(30.00);
        productActualizado.setStock(200);
        productActualizado.setCategoria("Hogar");
    }

    @Test
    void testGetAllProducts_DeberiaRetornarListaDeProductos() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product1, product2, product3);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.getAllProducts();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(3, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);
        assertEquals("Producto Ecológico A", actualProducts.get(0).getNombre());
        assertEquals("Limpieza", actualProducts.get(0).getCategoria());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetAllProducts_DeberiaRetornarListaVacia() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Product> actualProducts = productService.getAllProducts();

        // Assert
        assertNotNull(actualProducts);
        assertTrue(actualProducts.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testListarProductos_DeberiaRetornarListaDeProductos() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.listarProductos();

        // Assert
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertEquals(expectedProducts, actualProducts);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGuardarProducto_DeberiaGuardarProductoCorrectamente() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        productService.guardarProducto(product1);

        // Assert
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testGuardarProducto_ConProductoNulo_DeberiaLanzarExcepcion() {
        // Arrange
        when(productRepository.save(null)).thenThrow(new IllegalArgumentException("Product cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.guardarProducto(null);
        });
        verify(productRepository, times(1)).save(null);
    }

    @Test
    void testObtenerProducto_DeberiaRetornarProductoExistente() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));

        // Act
        Optional<Product> actualProduct = productService.obtenerProducto(1);

        // Assert
        assertTrue(actualProduct.isPresent());
        assertEquals(product1, actualProduct.get());
        assertEquals("Producto Ecológico A", actualProduct.get().getNombre());
        assertEquals(25.99, actualProduct.get().getPrecio());
        assertEquals(100, actualProduct.get().getStock());
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testObtenerProducto_DeberiaRetornarVacioSiNoExiste() {
        // Arrange
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Optional<Product> actualProduct = productService.obtenerProducto(999);

        // Assert
        assertFalse(actualProduct.isPresent());
        verify(productRepository, times(1)).findById(999);
    }

    @Test
    void testBuscarPorNombre_DeberiaRetornarProductosCoincidentes() {
        // Arrange
        List<Product> expectedProducts = Arrays.asList(product1, product2);
        when(productRepository.findByNombreContainingIgnoreCase("Ecológico")).thenReturn(expectedProducts);

        // Act
        List<Product> actualProducts = productService.buscarPorNombre("Ecológico");

        // Assert
        assertNotNull(actualProducts);
        assertEquals(2, actualProducts.size());
        assertTrue(actualProducts.get(0).getNombre().contains("Ecológico"));
        assertTrue(actualProducts.get(1).getNombre().contains("Ecológico"));
        verify(productRepository, times(1)).findByNombreContainingIgnoreCase("Ecológico");
    }

    @Test
    void testBuscarPorNombre_ConFiltroVacio_DeberiaRetornarListaVacia() {
        // Arrange
        when(productRepository.findByNombreContainingIgnoreCase("")).thenReturn(Collections.emptyList());

        // Act
        List<Product> actualProducts = productService.buscarPorNombre("");

        // Assert
        assertNotNull(actualProducts);
        assertTrue(actualProducts.isEmpty());
        verify(productRepository, times(1)).findByNombreContainingIgnoreCase("");
    }

    @Test
    void testBuscarPorNombre_ConFiltroNoCoincidente_DeberiaRetornarListaVacia() {
        // Arrange
        when(productRepository.findByNombreContainingIgnoreCase("NoExiste")).thenReturn(Collections.emptyList());

        // Act
        List<Product> actualProducts = productService.buscarPorNombre("NoExiste");

        // Assert
        assertNotNull(actualProducts);
        assertTrue(actualProducts.isEmpty());
        verify(productRepository, times(1)).findByNombreContainingIgnoreCase("NoExiste");
    }

    @Test
    void testBuscarPorNombre_ConFiltroMinusculasYMayusculas() {
        // Arrange
        when(productRepository.findByNombreContainingIgnoreCase("CHAMPÚ")).thenReturn(Arrays.asList(product3));

        // Act
        List<Product> actualProducts = productService.buscarPorNombre("CHAMPÚ");

        // Assert
        assertNotNull(actualProducts);
        assertEquals(1, actualProducts.size());
        assertEquals("Champú Natural", actualProducts.get(0).getNombre());
        verify(productRepository, times(1)).findByNombreContainingIgnoreCase("CHAMPÚ");
    }

    @Test
    void testCrearProducto_DeberiaGuardarYRetornarProducto() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product actualProduct = productService.crearProducto(product1);

        // Assert
        assertNotNull(actualProduct);
        assertEquals(product1, actualProduct);
        assertEquals("Producto Ecológico A", actualProduct.getNombre());
        assertEquals("Limpieza", actualProduct.getCategoria());
        assertEquals(25.99, actualProduct.getPrecio());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testCrearProducto_ConProductoNulo_DeberiaLanzarExcepcion() {
        // Arrange
        when(productRepository.save(null)).thenThrow(new IllegalArgumentException("Product cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            productService.crearProducto(null);
        });
        verify(productRepository, times(1)).save(null);
    }

    @Test
    void testEliminarProducto_DeberiaRetornarTrueSiExiste() {
        // Arrange
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        // Act
        boolean resultado = productService.eliminarProducto(1);

        // Assert
        assertTrue(resultado);
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void testEliminarProducto_DeberiaRetornarFalseSiNoExiste() {
        // Arrange
        when(productRepository.existsById(999)).thenReturn(false);

        // Act
        boolean resultado = productService.eliminarProducto(999);

        // Assert
        assertFalse(resultado);
        verify(productRepository, times(1)).existsById(999);
        verify(productRepository, never()).deleteById(anyInt());
    }

    @Test
    void testActualizarProducto_DeberiaActualizarProductoExistente() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product resultado = productService.actualizarProducto(1, productActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Producto Actualizado", product1.getNombre());
        assertEquals("Descripción actualizada", product1.getDescripcion());
        assertEquals(30.00, product1.getPrecio());
        assertEquals(200, product1.getStock());
        assertEquals("Hogar", product1.getCategoria());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testActualizarProducto_DeberiaRetornarNullSiNoExiste() {
        // Arrange
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Product resultado = productService.actualizarProducto(999, productActualizado);

        // Assert
        assertNull(resultado);
        verify(productRepository, times(1)).findById(999);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void testActualizarProducto_ConDatosNulos_DeberiaActualizarCorrectamente() {
        // Arrange
        Product productConNulos = new Product();
        productConNulos.setNombre(null);
        productConNulos.setDescripcion(null);
        productConNulos.setPrecio(0.0);
        productConNulos.setStock(0);
        productConNulos.setCategoria(null);

        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product resultado = productService.actualizarProducto(1, productConNulos);

        // Assert
        assertNotNull(resultado);
        assertNull(product1.getNombre());
        assertNull(product1.getDescripcion());
        assertEquals(0.0, product1.getPrecio());
        assertEquals(0, product1.getStock());
        assertNull(product1.getCategoria());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testActualizarProducto_ConProductoActualizadoNulo_DeberiaLanzarExcepcion() {
        // Arrange
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            productService.actualizarProducto(1, null);
        });
        verify(productRepository, times(1)).findById(1);
    }

    @Test
    void testActualizarProducto_ConStockNegativo_DeberiaActualizarCorrectamente() {
        // Arrange
        Product productConStockNegativo = new Product();
        productConStockNegativo.setNombre("Producto Agotado");
        productConStockNegativo.setDescripcion("Producto sin stock");
        productConStockNegativo.setPrecio(10.00);
        productConStockNegativo.setStock(-5);
        productConStockNegativo.setCategoria("Agotado");

        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act
        Product resultado = productService.actualizarProducto(1, productConStockNegativo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Producto Agotado", product1.getNombre());
        assertEquals(-5, product1.getStock());
        assertEquals("Agotado", product1.getCategoria());
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void testIntegracionCompleta_CrearBuscarActualizarEliminar() {
        // Arrange - Crear
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act - Crear
        Product productoCreado = productService.crearProducto(product1);

        // Assert - Crear
        assertNotNull(productoCreado);
        assertEquals("Producto Ecológico A", productoCreado.getNombre());
        assertEquals(100, productoCreado.getStock());

        // Arrange - Buscar
        when(productRepository.findByNombreContainingIgnoreCase("Ecológico")).thenReturn(Arrays.asList(product1));

        // Act - Buscar
        List<Product> productosEncontrados = productService.buscarPorNombre("Ecológico");

        // Assert - Buscar
        assertNotNull(productosEncontrados);
        assertEquals(1, productosEncontrados.size());
        assertEquals("Producto Ecológico A", productosEncontrados.get(0).getNombre());

        // Arrange - Actualizar
        when(productRepository.findById(1)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenReturn(product1);

        // Act - Actualizar
        Product productoActualizado = productService.actualizarProducto(1, productActualizado);

        // Assert - Actualizar
        assertNotNull(productoActualizado);
        assertEquals("Producto Actualizado", product1.getNombre());
        assertEquals(200, product1.getStock());

        // Arrange - Eliminar
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        // Act - Eliminar
        boolean eliminado = productService.eliminarProducto(1);

        // Assert - Eliminar
        assertTrue(eliminado);

        // Verificar todas las interacciones
        verify(productRepository, times(2)).save(any(Product.class));
        verify(productRepository, times(1)).findByNombreContainingIgnoreCase("Ecológico");
        verify(productRepository, times(1)).findById(1);
        verify(productRepository, times(1)).existsById(1);
        verify(productRepository, times(1)).deleteById(1);
    }

    @Test
    void testValidarProductosConDiferentesCategorias() {
        // Arrange
        Product productLimpieza = new Product(1, "Detergente Eco", "Limpieza ecológica", 8.99, 50, "Limpieza");
        Product productAlimentacion = new Product(2, "Miel Orgánica", "Miel pura de abeja", 12.50, 30, "Alimentación");
        Product productCuidado = new Product(3, "Crema Natural", "Crema hidratante natural", 15.99, 25, "Cuidado Personal");

        List<Product> productsPorCategoria = Arrays.asList(productLimpieza, productAlimentacion, productCuidado);
        when(productRepository.findAll()).thenReturn(productsPorCategoria);

        // Act
        List<Product> productos = productService.getAllProducts();

        // Assert
        assertNotNull(productos);
        assertEquals(3, productos.size());

        // Verificar diferentes categorías
        assertTrue(productos.stream().anyMatch(p -> "Limpieza".equals(p.getCategoria())));
        assertTrue(productos.stream().anyMatch(p -> "Alimentación".equals(p.getCategoria())));
        assertTrue(productos.stream().anyMatch(p -> "Cuidado Personal".equals(p.getCategoria())));

        // Verificar rangos de precios
        assertTrue(productos.stream().anyMatch(p -> p.getPrecio() < 10.00));
        assertTrue(productos.stream().anyMatch(p -> p.getPrecio() > 10.00 && p.getPrecio() < 15.00));
        assertTrue(productos.stream().anyMatch(p -> p.getPrecio() > 15.00));

        verify(productRepository, times(1)).findAll();
    }
}