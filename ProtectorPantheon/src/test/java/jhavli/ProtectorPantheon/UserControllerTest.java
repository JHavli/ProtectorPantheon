package jhavli.ProtectorPantheon;


import jhavli.ProtectorPantheon.controller.UserController;
import jhavli.ProtectorPantheon.mapper.UserRepository;
import jhavli.ProtectorPantheon.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUsers() {

        List<User> users = Collections.singletonList(new User());
        when(userRepository.findAll()).thenReturn(users);
        List<User> result = userController.getUsers();
        assertEquals(users, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateOwnPassword() {
        User currentUser = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(currentUser);
        when(userRepository.save(currentUser)).thenReturn(currentUser);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("john");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User result = userController.updateOwnPassword("newPassword");

        assertEquals("newPassword", result.getPassword());
        verify(userRepository, times(1)).findByUsername("john");
        verify(userRepository, times(1)).save(currentUser);
    }
}
