package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.Order;
import cl.duoc.caasoto.ecomarket.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> listarPedidos() {
        return orderRepository.findAll();
    }

    public Optional<Order> obtenerPedido(int id) {
        return orderRepository.findById(id);
    }

    public Order crearPedido(Order order) {
        return orderRepository.save(order);
    }

    public boolean eliminarPedido(int id) {
        if (orderRepository.existsById(id)){
            orderRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public Order actualizarPedido(int id, Order order) {
        return orderRepository.findById(id).map(pedido -> {
            pedido.setUser(order.getUser());
            pedido.setProducts(order.getProducts());
            pedido.setEstado(order.getEstado());
            pedido.setDireccionEnvio(order.getDireccionEnvio());
            return orderRepository.save(pedido);
        }).orElse(null);
    }
}
