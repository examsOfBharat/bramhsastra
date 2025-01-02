package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.BlogUpdatesDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogUpdates;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BlogUpdatesMapper {

    BlogUpdatesMapper INSTANCE = Mappers.getMapper(BlogUpdatesMapper.class);

    BlogUpdatesDTO toBlogHeaderDTO(BlogUpdates entity);

    BlogUpdates toBlogDTO(BlogUpdatesDTO dto);
}
