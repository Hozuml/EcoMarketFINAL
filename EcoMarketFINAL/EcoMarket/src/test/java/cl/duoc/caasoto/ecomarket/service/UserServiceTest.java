package cl.duoc.caasoto.ecomarket.service;

import cl.duoc.caasoto.ecomarket.model.User;
import cl.duoc.caasoto.ecomarket.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        User user = new User(1, "Camila", "Soto", "camila@ecomarket.cl", "123456789", "Calle 123", "Cliente","123");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user));
    }

    @Test
    public void testListarUsuarios() {
        User user = new User (1, "Camila", "Soto", "camila@ecomarket.cl", "123456789", "Calle 123", "Cliente", "123");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        var usuarios = userService.listarUsuarios();

        assertNotNull(usuarios);
        assertEquals(1,usuarios.size());
        assertEquals("", usuarios.get(0).getNombre());
    }

    @Test
    public void testCrearUsuario(){
        User user = new User(1,"Camila","Soto","camila@ecomarket.cl","123456789","Calle 123","Cliente","123");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User createdUser = userService.crearUsuario(user);

        assertNotNull(createdUser);
        assertEquals("", createdUser.getNombre());
    }

    @Test
    public void testObtenerUsuario(){
        User user = new User ();

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<User> retrievedUser = userService.obtenerUsuario(1);

        assertTrue(retrievedUser.isPresent());
        assertEquals(1, retrievedUser.get().getNombre());
    }

    @Test
    public void testEliminarUsuario(){
        Mockito.when(userRepository.existsById(1)).thenReturn(true);

        boolean deleted = userService.eliminarUsuario(1);

        assertTrue(deleted);
    }

    @Test
    public void testActualizarUsuario(){
        User user = new User (1,"Camila","Soto","camila@ecomarket.cl","123456789","Calle 123","Cliente","123");
        User updatedUser = new User(1,"Camila","Soto","NUEVOcamila@ecomarket.cl","987654321","Calle 123","Cliente","123");

        Mockito.when(userRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(updatedUser);

        User result = userService.actualizarUsuario(1, updatedUser);

        assertNotNull(result);
        assertEquals("NUEVOcamila@ecomarket.cl", result.getEmail());
    }
}