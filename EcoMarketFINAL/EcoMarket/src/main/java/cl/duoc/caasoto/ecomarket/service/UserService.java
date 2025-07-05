package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.User;
import cl.duoc.caasoto.ecomarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    public Optional<User> obtenerUsuario(int id) {
        return userRepository.findById(id);
    }

    public User crearUsuario(User user) {
        return userRepository.save(user);
    }

    public User obtenerUsuarioPorId(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public boolean eliminarUsuario(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public User actualizarUsuario(int id, User user) {
        return userRepository.findById(id).map(usuario -> {
            usuario.setNombre(user.getNombre());
            usuario.setApellido(user.getApellido());
            usuario.setEmail(user.getEmail());
            usuario.setTelefono(user.getTelefono());
            usuario.setDireccion(user.getDireccion());
            usuario.setRol(user.getRol());
            return userRepository.save(usuario);
        }).orElse(null);
    }


    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userRepository.findByEmail(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
