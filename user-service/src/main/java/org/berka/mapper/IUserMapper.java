package org.berka.mapper;

import org.berka.dto.response.UserResponseDto;
import org.berka.rabbitmq.model.RegisterUserModel;
import org.berka.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserMapper {

    IUserMapper INSTANCE = Mappers.getMapper(IUserMapper.class);

    User toUser(final RegisterUserModel model);

    UserResponseDto toUserResponseDto(final User user);
}
