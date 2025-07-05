package cl.duoc.caasoto.ecomarket.controller;

import cl.duoc.caasoto.ecomarket.model.User;
import cl.duoc.caasoto.ecomarket.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserService userService;

    // Inyección por constructor (recomendado)
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session) {
        Optional<User> usuarioOpt = userService.login(email, password);

        if (usuarioOpt.isPresent()) {
            User usuario = usuarioOpt.get();
            session.setAttribute("usuario", usuario);
            return "redirect:/";
        }
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            session.invalidate();
            redirectAttributes.addFlashAttribute("mensaje", "Sesión cerrada exitosamente");
        } catch (Exception e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
        }
        return "redirect:/";
    }
}