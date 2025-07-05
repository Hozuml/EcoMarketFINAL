package cl.duoc.caasoto.ecomarket.controller;

import cl.duoc.caasoto.ecomarket.model.Order;
import cl.duoc.caasoto.ecomarket.service.OrderService;
import cl.duoc.caasoto.ecomarket.service.ProductService;
import cl.duoc.caasoto.ecomarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    // -------------------- VISTA WEB --------------------

    // Listado de pedidos (HTML)
    @GetMapping
    public String listarPedidosVista(Model model) {
        List<Order> pedidos = orderService.listarPedidos();
        model.addAttribute("pedidos", pedidos);
        return "pedidos"; // Debe existir pedidos.html
    }

    // Formulario para crear un nuevo pedido (HTML)
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoPedido(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("usuarios", userService.listarUsuarios());
        model.addAttribute("productos", productService.listarProductos());
        return "pedido_form";
    }

    // Guardar nuevo pedido desde formulario (HTML)
    @PostMapping
    public String guardarPedidoDesdeFormulario(@ModelAttribute("order") Order order) {
        orderService.crearPedido(order);
        return "redirect:/pedidos";
    }

    // -------------------- API REST --------------------

    // Obtener un pedido por ID (JSON)
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Order> obtener(@PathVariable int id) {
        return orderService.obtenerPedido(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear pedido por API (JSON)
    @PostMapping(path = "/api")
    @ResponseBody
    public ResponseEntity<Order> crear(@RequestBody Order order) {
        Order creado = orderService.crearPedido(order);
        return ResponseEntity.status(201).body(creado);
    }

    // Actualizar pedido por API (JSON)
    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Order> actualizar(@PathVariable int id, @RequestBody Order order) {
        Order actualizado = orderService.actualizarPedido(id, order);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar pedido por API
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = orderService.eliminarPedido(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
