package org.berka.service;

import org.berka.dto.request.ChangePasswordRequestDto;
import org.berka.dto.request.UpdateRequestDto;
import org.berka.dto.response.MessageResponseDto;
import org.berka.dto.response.UserResponseDto;
import org.berka.exception.ErrorType;
import org.berka.exception.UserManagerException;
import org.berka.manager.IAuthManager;
import org.berka.mapper.IUserMapper;
import org.berka.rabbitmq.model.RegisterUserModel;
import org.berka.rabbitmq.producer.DeleteUserProducer;
import org.berka.repository.IUserRepository;
import org.berka.repository.entity.User;
import org.berka.utility.JwtTokenManager;
import org.berka.utility.ServiceManager;
import org.springframework.stereotype.Service;


@Service
public class UserService extends ServiceManager<User,Long> {

    private final IUserRepository repository;

    private final JwtTokenManager jwtTokenManager;

    private final DeleteUserProducer deleteUserProducer;
    private final IAuthManager authManager;


    public UserService(IUserRepository repository, JwtTokenManager jwtTokenManager, DeleteUserProducer deleteUserProducer, IAuthManager authManager) {
        super(repository);
        this.repository = repository;
        this.jwtTokenManager = jwtTokenManager;
        this.deleteUserProducer = deleteUserProducer;
        this.authManager = authManager;
    }


    public User registerUser(RegisterUserModel model) {
        User user = IUserMapper.INSTANCE.toUser(model);
        return save(user);
    }

    public MessageResponseDto updateUser(UpdateRequestDto dto) {
        Long authId = jwtTokenManager.getIdFromToken(dto.getToken()).orElseThrow(()->new UserManagerException(ErrorType.INVALID_TOKEN));

        User user = repository.findByAuthId(authId).orElseThrow(()->new UserManagerException(ErrorType.USER_NOT_FOUND));


        if (user.getName() != null && user.getSurname() != null && user.getTitle() != null
                && user.getName().equals(dto.getName()) && user.getSurname().equals(dto.getSurname())
                && user.getTitle().equals(dto.getTitle())) {
            return new MessageResponseDto("Herhangi bir değişiklik yapilmadi");
        }


        user.setTitle(dto.getTitle());
        user.setSurname(dto.getSurname());
        user.setName(dto.getName());

        update(user);

        return new MessageResponseDto("bilgileriniz guncellendi");
    }

    public MessageResponseDto deleteUser(String token) {
        Long authId = jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new UserManagerException(ErrorType.INVALID_TOKEN));

        User user = repository.findByAuthId(authId).orElseThrow(() -> new UserManagerException(ErrorType.USER_NOT_FOUND));

        delete(user);

        deleteUserProducer.deleteUser(token);
        return new MessageResponseDto("Hesabiniz basariyla silinmistir");
    }

    public MessageResponseDto changePassword(ChangePasswordRequestDto dto) {

        Long authId = jwtTokenManager.getIdFromToken(dto.getToken()).orElseThrow(() -> new UserManagerException(ErrorType.INVALID_TOKEN));
        boolean existsById = repository.existsByAuthId(authId);
        if (!existsById) {
            throw new UserManagerException(ErrorType.USER_NOT_FOUND);
        }

        return authManager.changePassword(dto).getBody();

    }

    public UserResponseDto userInformation(String token) {
        Long authId = jwtTokenManager.getIdFromToken(token).orElseThrow(() -> new UserManagerException(ErrorType.INVALID_TOKEN));

        User user = repository.findByAuthId(authId).orElseThrow(() -> new UserManagerException(ErrorType.USER_NOT_FOUND));

        return IUserMapper.INSTANCE.toUserResponseDto(user);
    }
}
