package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.Product;
import cl.duoc.caasoto.ecomarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    //obtener TODOS los productos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> listarProductos() {
        return productRepository.findAll();
    }

    public void guardarProducto(Product product) {
        productRepository.save(product);
    }

    //Obtener UN producto por ID
    public Optional<Product> obtenerProducto(int id) {
        return productRepository.findById(id);
    }
    //Buscar producto
    public List<Product> buscarPorNombre(String filtro) {
        return productRepository.findByNombreContainingIgnoreCase(filtro);
    }

    public Product crearProducto(Product product) {
        return productRepository.save(product);
    }

    public boolean eliminarProducto(int id) {
        if (productRepository.existsById(id)){
            productRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Product actualizarProducto(int id, Product product) {
        return productRepository.findById(id).map(p -> {
            p.setNombre(product.getNombre());
            p.setDescripcion(product.getDescripcion());
            p.setPrecio(product.getPrecio());
            p.setStock(product.getStock());
            p.setCategoria(product.getCategoria());
            return productRepository.save(p);
        }).orElse(null);
    }

    /*

    public Product actualizarProducto(Product product){
        return productRepository.save(product);
    }

     */

}
