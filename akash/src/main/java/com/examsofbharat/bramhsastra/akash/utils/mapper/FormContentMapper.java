package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationContentManager;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FormContentMapper {

    FormContentMapper INSTANCE = Mappers.getMapper(FormContentMapper.class);

    ApplicationContentManagerDTO toFormContentDTO(ApplicationContentManager entity);

    ApplicationContentManager toContentEntity(ApplicationContentManagerDTO dto);
}
