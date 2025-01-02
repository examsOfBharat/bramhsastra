package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.response.HomeBlogDataDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogHeader;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HomeBlogDataDTOMapper {
    HomeBlogDataDTOMapper INSTANCE = Mappers.getMapper(HomeBlogDataDTOMapper.class);

    HomeBlogDataDTO toBlogHeaderDTO(BlogHeader entity);
}
