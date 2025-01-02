package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.BlogHeaderDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogHeader;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogHeaderMapper {

    BlogHeaderMapper INSTANCE = Mappers.getMapper(BlogHeaderMapper.class);

    BlogHeaderDTO toBlogHeaderDTO(BlogHeader entity);

    BlogHeader toBlogHeader(BlogHeaderDTO dto);
}
