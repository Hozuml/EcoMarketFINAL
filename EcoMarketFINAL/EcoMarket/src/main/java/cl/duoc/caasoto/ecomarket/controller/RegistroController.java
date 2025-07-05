package cl.duoc.caasoto.ecomarket.controller;

import cl.duoc.caasoto.ecomarket.model.User;
import cl.duoc.caasoto.ecomarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistroController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String mostrarRegistro(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String procesarRegistro(@RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam(required = false) String direccion,
                                   RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setEmail(email);
            user.setPassword(password);
            user.setTelefono(telefono);
            user.setDireccion(direccion);
            user.setRol("Cliente");

            User nuevoUsuario = userService.crearUsuario(user);

            if (nuevoUsuario != null) {
                redirectAttributes.addFlashAttribute("mensaje",
                        "Registro exitoso. Por favor, inicia sesión.");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "No se pudo completar el registro.");
                return "redirect:/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error en el registro: " + e.getMessage());
            return "redirect:/register";
        }
    }

}
