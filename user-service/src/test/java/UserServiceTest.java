import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.dto.response.UserResponseDto;
import org.berka.exception.UserManagerException;
import org.berka.manager.IAuthManager;
import org.berka.rabbitmq.producer.DeleteUserProducer;
import org.berka.repository.IUserRepository;
import org.berka.repository.entity.User;
import org.berka.service.UserService;
import org.berka.utility.JwtTokenManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService service;

    @Mock
    private JwtTokenManager jwtTokenManager;

    @Mock
    private DeleteUserProducer deleteUserProducer;

    @Mock
    IAuthManager authManager;


    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }


    //========================================================= Update Tests =======================================================

    @Test
    public void update_ShouldBeSuccessful(){
        UpdateRequestDto dto = UpdateRequestDto.builder().token("token").name("name").build();

        User user = User.builder().id(1L).authId(1L).build();
        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(user.getAuthId()));

        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.of(user));

        MessageResponseDto messageResponseDto = service.updateUser(dto);

        assertEquals("bilgileriniz guncellendi",messageResponseDto.getSuccessMessage());
 }

    @Test
    public void update_NothingChanged(){
        UpdateRequestDto dto = UpdateRequestDto.builder().token("token").name("name").surname("surname").title("title").build();

        User user = User.builder().id(1L).name("name").surname("surname").title("title").authId(1L).build();
        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(user.getAuthId()));

        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.of(user));

        MessageResponseDto messageResponseDto = service.updateUser(dto);

        assertEquals("Herhangi bir değişiklik yapilmadi",messageResponseDto.getSuccessMessage());
    }

    @Test
    public void update_InvalidTokenException(){
        UpdateRequestDto dto = UpdateRequestDto.builder().token("token").name("name").surname("surname").title("title").build();

        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserManagerException.class,()->service.updateUser(dto));
    }

    @Test
    public void update_UserNotFoundException(){
        UpdateRequestDto dto = UpdateRequestDto.builder().token("token").name("name").surname("surname").title("title").build();
        User user = User.builder().authId(1L).build();
        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(user.getAuthId()));

        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserManagerException.class,()->service.updateUser(dto));
    }

    //========================================================= Delete Tests =======================================================

    @Test
    public void delete_ShouldBeSuccessful(){
        String token = "token";
        User user = User.builder().authId(1L).id(1L).build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(user.getAuthId()));
        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.of(user));

        MessageResponseDto messageResponseDto = service.deleteUser(token);

        assertEquals("Hesabiniz basariyla silinmistir",messageResponseDto.getSuccessMessage());
    }
    @Test
    public void delete_ShouldBeInvalidToken(){
        String token = "token";
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.empty());

        assertThrows(UserManagerException.class,()-> service.deleteUser(token));
    }

    @Test
    public void delete_ShouldNotHaveUser(){
        String token = "token";
        User user = User.builder().authId(1L).id(1L).build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(user.getAuthId()));
        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.empty());

        assertThrows(UserManagerException.class,()-> service.deleteUser(token));
    }


    //========================================================= changePassword Tests =======================================================

//    @Test
//    public void changePassword_ShouldBeSuccessful(){
//        ChangePasswordRequestDto dto= ChangePasswordRequestDto.builder().password("password").oldPassword("oldPassword").token("token").build();
//        User user = User.builder().authId(1L).id(1L).build();
//        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(user.getAuthId()));
//        when(userRepository.existsByAuthId(user.getAuthId())).thenReturn(true);
//
//        MessageResponseDto messageResponseDto = service.changePassword(dto);
//
//        assertEquals("Hesabiniz basariyla silinmistir",messageResponseDto.getSuccessMessage());
//    }
    @Test
    public void changePassword_ShouldThrowInvalidToken(){
        ChangePasswordRequestDto dto= ChangePasswordRequestDto.builder().password("password").oldPassword("oldPassword").token("token").build();
        User user = User.builder().authId(1L).id(1L).build();
        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.empty());

        assertThrows(UserManagerException.class,()-> service.changePassword(dto));
    }

    @Test
    public void changePassword_ShouldThrowUserNotFound(){
        ChangePasswordRequestDto dto= ChangePasswordRequestDto.builder().password("password").oldPassword("oldPassword").token("token").build();
        User user = User.builder().authId(1L).id(1L).build();
        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(user.getAuthId()));
        when(userRepository.existsByAuthId(user.getAuthId())).thenReturn(false);

        assertThrows(UserManagerException.class,()-> service.changePassword(dto));
    }

    //========================================================= userInformation Tests =======================================================
    @Test
    public void userInformation_ShouldBeSuccessful(){
        String token = "token";
        User user = User.builder().authId(1L).id(1L).name("success").build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(user.getAuthId()));
        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.of(user));

        UserResponseDto dto = service.userInformation(token);

        assertEquals("success",dto.getName());
    }

    @Test
    public void userInformation_ShouldThrowInvalidToken(){
        String token = "token";
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.empty());

        assertThrows(UserManagerException.class,()-> service.userInformation(token));
    }

    @Test
    public void userInformation_ShouldThrowUserNotFound(){
        String token = "token";
        User user = User.builder().authId(1L).id(1L).build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(user.getAuthId()));
        when(userRepository.findByAuthId(user.getAuthId())).thenReturn(Optional.empty());

        assertThrows(UserManagerException.class,()-> service.userInformation(token));
    }
}
