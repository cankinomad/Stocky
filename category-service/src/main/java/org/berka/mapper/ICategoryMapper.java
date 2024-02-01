package org.berka.mapper;

import org.berka.dto.request.CreateRequestDto;
import org.berka.repository.ICategoryRepository;
import org.berka.repository.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ICategoryMapper {

    ICategoryMapper INSTANCE = Mappers.getMapper(ICategoryMapper.class);


    Category toCategory(final CreateRequestDto dto);
}
