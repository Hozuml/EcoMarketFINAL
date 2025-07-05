package cl.duoc.caasoto.ecomarket.repository;

import cl.duoc.caasoto.ecomarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNombreContainingIgnoreCase(String nombre);
}
