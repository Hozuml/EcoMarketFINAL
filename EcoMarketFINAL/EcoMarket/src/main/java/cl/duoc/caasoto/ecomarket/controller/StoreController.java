package cl.duoc.caasoto.ecomarket.controller;

import cl.duoc.caasoto.ecomarket.model.Store;
import cl.duoc.caasoto.ecomarket.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tiendas")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @GetMapping
    public String listarTiendas(Model model) {
        List<Store> tiendas = storeService.listarTiendas();
        model.addAttribute("tiendas", tiendas);
        model.addAttribute("tienda", new Store()); // para formulario creación
        return "tiendas";  // nombre de la vista Thymeleaf tiendas.html
    }

    @PostMapping
    public String crearTienda(@ModelAttribute Store store, Model model) {
        storeService.crearTienda(store);
        return "redirect:/tiendas"; // redirige para evitar reenvío del formulario
    }

    @GetMapping("/editar/{id}")
    public String editarTienda(@PathVariable int id, Model model) {
        Optional<Store> tiendaOpt = storeService.obtenerTienda(id);
        if (tiendaOpt.isPresent()) {
            model.addAttribute("tienda", tiendaOpt.get());
            return "editar_tienda";  // vista para editar tienda
        } else {
            return "redirect:/tiendas";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarTienda(@PathVariable int id, @ModelAttribute Store store) {
        storeService.actualizarTienda(id, store);
        return "redirect:/tiendas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarTienda(@PathVariable int id) {
        storeService.eliminarTienda(id);
        return "redirect:/tiendas";
    }
}
