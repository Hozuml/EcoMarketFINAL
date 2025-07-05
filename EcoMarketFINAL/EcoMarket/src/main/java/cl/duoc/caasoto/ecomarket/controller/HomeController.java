package cl.duoc.caasoto.ecomarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import cl.duoc.caasoto.ecomarket.model.Product;
import cl.duoc.caasoto.ecomarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        List<Product> productos = productService.listarProductos();

        if (productos == null || productos.isEmpty()) {
            System.out.println("No se encontraron productos.");
        } else {
            System.out.println("Productos encontrados: " + productos.size());
        }

        model.addAttribute("productos", productos);
        return "home";
    }

}
