package cl.duoc.caasoto.ecomarket.controller;

import cl.duoc.caasoto.ecomarket.model.Product;
import cl.duoc.caasoto.ecomarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Listar productos, con opción filtro por nombre
    @GetMapping
    public String listarProductos(@RequestParam(value = "filtro", required = false) String filtro, Model model) {
        List<Product> productos = (filtro != null && !filtro.isEmpty())
                ? productService.buscarPorNombre(filtro)
                : productService.getAllProducts();
        model.addAttribute("productos", productos);
        model.addAttribute("filtro", filtro);
        return "productos"; // Vista Thymeleaf lista productos
    }

    // Mostrar formulario para crear un producto nuevo
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("producto", new Product());
        return "producto_form"; // Vista Thymeleaf formulario producto
    }

    // Mostrar formulario para editar un producto existente
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable int id, Model model) {
        Product producto = productService.obtenerProducto(id).orElse(null);
        if (producto == null) {
            return "redirect:/productos"; // Si no existe, redirige a lista
        }
        model.addAttribute("producto", producto);
        return "producto_form";
    }

    // Guardar producto nuevo o editado (mismo método para ambos casos)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute Product producto) {
        productService.guardarProducto(producto);
        return "redirect:/productos"; // Redirige a lista tras guardar
    }

    // Eliminar producto por id
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable int id) {
        productService.eliminarProducto(id);
        return "redirect:/productos"; // Redirige a lista tras eliminar
    }

}
