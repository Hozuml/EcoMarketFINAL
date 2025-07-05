package cl.duoc.caasoto.ecomarket.repository;

import cl.duoc.caasoto.ecomarket.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
