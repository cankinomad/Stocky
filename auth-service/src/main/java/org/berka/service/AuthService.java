package org.berka.service;

import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.request.LoginRequestDto;
import org.berka.dto.request.RegisterRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.dto.response.TokenResponseDto;
import org.berka.exception.AuthManagerException;
import org.berka.exception.ErrorType;
import org.berka.mapper.IAuthMapper;
import org.berka.rabbitmq.model.RegisterUserModel;
import org.berka.rabbitmq.produce.RegisterUserProducer;
import org.berka.repository.IAuthRepository;
import org.berka.repository.entity.Auth;
import org.berka.utility.JwtTokenManager;
import org.berka.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService extends ServiceManager<Auth, Long> {

    private final IAuthRepository repository;

    private final JwtTokenManager jwtTokenManager;

    private final RegisterUserProducer registerUserProducer;

    public AuthService(IAuthRepository repository, JwtTokenManager jwtTokenManager, RegisterUserProducer registerUserProducer) {
        super(repository);
        this.repository = repository;
        this.jwtTokenManager = jwtTokenManager;
        this.registerUserProducer = registerUserProducer;
    }

    public MessageResponseDto registerUser(RegisterRequestDto dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) {
            throw new AuthManagerException(ErrorType.PASSWORD_MISMATCHED);
        }

        if(repository.existsByUsername(dto.getUsername())){
            throw new AuthManagerException(ErrorType.USERNAME_EXIST);
        }
        Auth auth = IAuthMapper.INSTANCE.toAuth(dto);
        save(auth);

        registerUserProducer.registerUser(new RegisterUserModel(auth.getId()));

        return new MessageResponseDto("Kaydiniz basariyla olusturulmustur!");
    }

    public TokenResponseDto login(LoginRequestDto dto) {
        Optional<Auth> byUsernameAndPassword = repository.findByUsernameAndPassword(dto.getUsername(), dto.getPassword());
        if (byUsernameAndPassword.isEmpty()) {
            throw new AuthManagerException(ErrorType.USER_NOT_FOUND);
        }
        Optional<String> token = jwtTokenManager.createToken(byUsernameAndPassword.get().getId());
        if (token.isEmpty()) {
            throw new AuthManagerException(ErrorType.TOKEN_NOT_CREATED);
        }

        return new TokenResponseDto(token.get());
    }

    public Boolean deleteUser(String token) {
        Long authId = jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));
        Auth auth = repository.findById(authId).orElseThrow(() -> new AuthManagerException(ErrorType.USER_NOT_FOUND));
        delete(auth);
        return true;
    }

    public MessageResponseDto changePassword(ChangePasswordRequestDto dto) {
        Long id = jwtTokenManager.getIdFromToken(dto.getToken()).orElseThrow(() -> new AuthManagerException(ErrorType.INVALID_TOKEN));
        Auth auth = repository.findById(id).orElseThrow(() -> new AuthManagerException(ErrorType.USER_NOT_FOUND));

        if (!auth.getPassword().equals(dto.getOldPassword())) {
            return new MessageResponseDto("Yanlis bir sifre girdiniz:e");
        }
        if (!auth.getPassword().equals(dto.getPassword())) {
            auth.setPassword(dto.getPassword());
            update(auth);
            return new MessageResponseDto("Sifre basariyla guncellendi:s") ;
        }

        return new MessageResponseDto("Herhangi bir degisiklik yapilmadi:w");


    }

    public Boolean tokenValidation(String token) {
        Optional<Long> optionalAuthId = jwtTokenManager.getIdFromToken(token);
        if (optionalAuthId.isEmpty()) {
            return false;
        }
        return repository.existsById(optionalAuthId.get());
    }
}
