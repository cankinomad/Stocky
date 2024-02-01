import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.request.LoginRequestDto;
import org.berka.dto.request.RegisterRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.dto.response.TokenResponseDto;
import org.berka.exception.AuthManagerException;
import org.berka.rabbitmq.produce.RegisterUserProducer;
import org.berka.repository.IAuthRepository;
import org.berka.repository.entity.Auth;
import org.berka.service.AuthService;
import org.berka.utility.JwtTokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthServiceTest {

    @Mock
    private IAuthRepository repository;

    @InjectMocks
    private AuthService authService;

    @Mock
    private JwtTokenManager jwtTokenManager;
    @Mock
    private RegisterUserProducer registerUserProducer;



    @BeforeEach
    public void Init() {
        MockitoAnnotations.openMocks(this);
    }

    //========================================================= Register Tests =======================================================

    @Test
    public void register_ShouldBeSuccessful(){
        RegisterRequestDto dto = RegisterRequestDto.builder().username("username").rePassword("password").password("password").build();

        when(repository.existsByUsername(dto.getUsername())).thenReturn(false);

        MessageResponseDto messageResponseDto = authService.registerUser(dto);

        assertEquals("Kaydiniz basariyla olusturulmustur!",messageResponseDto.getSuccessMessage());
    }

    @Test
    public void register_ShouldThrowMissMatchedException(){
        RegisterRequestDto dto = RegisterRequestDto.builder().username("username").rePassword("password").password("newpassword").build();
        assertThrows(AuthManagerException.class,()->authService.registerUser(dto));
    }

    @Test
    public void register_ShouldThrowUsernameExistException(){
        RegisterRequestDto dto = RegisterRequestDto.builder().username("username").rePassword("password").password("password").build();
        when(repository.existsByUsername(dto.getUsername())).thenReturn(true);
        assertThrows(AuthManagerException.class,()->authService.registerUser(dto));
    }

    //========================================================= Login Tests =======================================================
    @Test
    public void login_shouldLoginSuccessfully() {

        LoginRequestDto loginRequestDto = LoginRequestDto.builder().username("username").password("password").build();
        Auth mockAuth = Auth.builder().username("username").password("password").build();


        when(repository.findByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword()))
                .thenReturn(Optional.of(mockAuth));


        when(jwtTokenManager.createToken(mockAuth.getId())).thenReturn(Optional.of("token"));


        TokenResponseDto response = authService.login(loginRequestDto);

        assertEquals("token", response.getToken());
    }

    @Test
    public void login_shouldThrowExceptionWhenUserNotFound() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("nonexistentUser", "password");

        when(repository.findByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword()))
                .thenReturn(Optional.empty());


        assertThrows(AuthManagerException.class, () -> authService.login(loginRequestDto));
    }

    @Test
    public void login_shouldThrowExceptionWhenTokenNotCreated() {

        LoginRequestDto loginRequestDto = new LoginRequestDto("username", "password");
        Auth mockAuth = Auth.builder().username("username").password("password").build();


        when(repository.findByUsernameAndPassword(loginRequestDto.getUsername(), loginRequestDto.getPassword()))
                .thenReturn(Optional.of(mockAuth));


        when(jwtTokenManager.createToken(mockAuth.getId())).thenReturn(Optional.empty());


        assertThrows(AuthManagerException.class, () -> authService.login(loginRequestDto));
    }



    //========================================================= Delete Tests =======================================================

    @Test
    public void delete_shouldDeleteSuccessfully(){
        String token = "token";
        Auth mockAuth = Auth.builder().id(1L).build();
        when(jwtTokenManager.getIdFromToken("token")).thenReturn(Optional.of(mockAuth.getId()));
        when(repository.findById(mockAuth.getId())).thenReturn(Optional.of(mockAuth));

        Boolean result = authService.deleteUser(token);

        assertTrue(result);
    }


    @Test
    public void delete_shouldThrowExceptionWhenTokenInvalid() {

        String token = "token";

        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.empty());

        assertThrows(AuthManagerException.class, () -> authService.deleteUser(token));

    }

    @Test
    public void delete_shouldThrowExceptionWhenUserNotFound() {
        String token = "token";
        Long authId = 1L;

        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(authId));
        when(repository.findById(authId)).thenReturn(Optional.empty());


        assertThrows(AuthManagerException.class, () -> authService.deleteUser(token));
    }


    //========================================================= changePassword Tests =======================================================


    @Test
    public void changePassword_ShouldBeSuccessful(){
        ChangePasswordRequestDto dto = ChangePasswordRequestDto.builder().oldPassword("oldPassword").password("newPassword").token("token").build();
        Auth auth = Auth.builder().username("username").password("oldPassword").id(1L).build();

        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(auth.getId()));

        when(repository.findById(auth.getId())).thenReturn(Optional.of(auth));

        MessageResponseDto messageResponseDto= authService.changePassword(dto);

        assertEquals("Sifre basariyla guncellendi:s",messageResponseDto.getSuccessMessage());
    }

    @Test
    public void changePassword_ShouldFail(){
        ChangePasswordRequestDto dto = ChangePasswordRequestDto.builder().oldPassword("notOldPassword").password("newPassword").token("token").build();
        Auth auth = Auth.builder().username("username").password("oldPassword").id(1L).build();

        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(auth.getId()));

        when(repository.findById(auth.getId())).thenReturn(Optional.of(auth));

        MessageResponseDto messageResponseDto= authService.changePassword(dto);

        assertEquals("Yanlis bir sifre girdiniz:e",messageResponseDto.getSuccessMessage());
    }

    @Test
    public void changePassword_ShouldNotExecute(){
        ChangePasswordRequestDto dto = ChangePasswordRequestDto.builder().oldPassword("stable").password("stable").token("token").build();
        Auth auth = Auth.builder().username("username").password("stable").id(1L).build();

        when(jwtTokenManager.getIdFromToken(dto.getToken())).thenReturn(Optional.of(auth.getId()));

        when(repository.findById(auth.getId())).thenReturn(Optional.of(auth));

        MessageResponseDto messageResponseDto= authService.changePassword(dto);

        assertEquals("Herhangi bir degisiklik yapilmadi:w",messageResponseDto.getSuccessMessage());
    }


    //========================================================= tokenValidation Tests =======================================================

    @Test
    public void tokenValidation_ShouldBeSuccessful(){
        String token = "token";
        Auth auth=Auth.builder().id(1L).build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(auth.getId()));

        when(repository.existsById(auth.getId())).thenReturn(true);

        Boolean result = authService.tokenValidation(token);

        assertTrue(result);
    }

    @Test
    public void tokenValidation_ShouldIncludeEmptyAuth(){
        String token = "token";
        Auth auth=Auth.builder().id(1L).build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.of(auth.getId()));

        when(repository.existsById(auth.getId())).thenReturn(false);

        Boolean result = authService.tokenValidation(token);

        assertFalse(result);
    }

    @Test
    public void tokenValidation_ShouldIncludeEmptyId(){
        String token = "token";
        Auth auth=Auth.builder().id(1L).build();
        when(jwtTokenManager.getIdFromToken(token)).thenReturn(Optional.empty());

        Boolean result = authService.tokenValidation(token);

        assertFalse(result);
    }


}


