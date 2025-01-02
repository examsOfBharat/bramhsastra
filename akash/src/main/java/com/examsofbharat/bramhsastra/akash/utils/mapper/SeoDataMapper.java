package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationSeoDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SeoDataMapper {

    SeoDataMapper INSTANCE = Mappers.getMapper(SeoDataMapper.class);

    ApplicationSeoDetailsDTO toFormContentDTO(ApplicationSeoDetails entity);
}
