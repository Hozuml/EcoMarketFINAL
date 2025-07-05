package cl.duoc.caasoto.ecomarket.controller;

import cl.duoc.caasoto.ecomarket.model.User;
import cl.duoc.caasoto.ecomarket.repository.UserRepository;
import cl.duoc.caasoto.ecomarket.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // Constructor con inyección de dependencias (recomendado)
    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        crearAdminInicial();
    }
    @GetMapping
    public String listar(Model model) {
        List<User> usuarios = userService.listarUsuarios();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", new User());
        return "usuarios";
    }

    @PostMapping
    public String crear(@ModelAttribute("usuario") User user,
                        BindingResult bindingResult,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarios", userService.listarUsuarios());
            return "usuarios";
        }

        try {
            userService.crearUsuario(user);
            model.addAttribute("exito", "Usuario creado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el usuario: " + e.getMessage());
        }

        model.addAttribute("usuarios", userService.listarUsuarios());
        model.addAttribute("usuario", new User());
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new User());
        return "usuario_form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable int id, Model model) {
        boolean eliminado = userService.eliminarUsuario(id);
        if (eliminado) {
            model.addAttribute("mensaje", "Usuario eliminado exitosamente");
        } else {
            model.addAttribute("mensaje", "Error: El usuario no pudo ser eliminado");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable int id, Model model) {
        try {
            User usuario = userService.obtenerUsuarioPorId(id);
            model.addAttribute("usuario", usuario);
            return "usuario_form";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping("/editar/{id}")
    public String actualizarUsuario(@PathVariable int id,
                                    @ModelAttribute("usuario") User user,
                                    BindingResult bindingResult,
                                    Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarios", userService.listarUsuarios());
            return "usuario_form";
        }

        try {
            userService.actualizarUsuario(id, user);
            model.addAttribute("exito", "Usuario actualizado correctamente.");
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
        }

        return "redirect:/usuarios";
    }

    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registrarUsuario(@ModelAttribute("user") User user,
                                   BindingResult bindingResult,
                                   HttpSession session,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            user.setRol("CLIENTE");
            User usuarioRegistrado = userService.crearUsuario(user);
            session.setAttribute("usuario", usuarioRegistrado);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "register";
        }
    }

    private void crearAdminInicial() {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setEmail("admin@ecomarket.com");
            admin.setPassword("admin123"); // Contraseña en texto plano
            admin.setTelefono("+56912345678");
            admin.setDireccion("Oficina Central");
            admin.setRol("ADMIN");

            userRepository.save(admin);
            System.out.println("Usuario admin creado: admin@ecomarket.com / admin123");
        }
    }
}