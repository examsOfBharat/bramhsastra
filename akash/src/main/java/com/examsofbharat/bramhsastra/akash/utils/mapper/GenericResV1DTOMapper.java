package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.response.GenericResponseV1DTO;
import com.examsofbharat.bramhsastra.prithvi.entity.GenericResponseV1;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GenericResV1DTOMapper {

    GenericResV1DTOMapper INSTANCE = Mappers.getMapper(GenericResV1DTOMapper.class);

    GenericResponseV1DTO toGenericResV1DTO(GenericResponseV1 entity);
}
