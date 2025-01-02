package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationSeoDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogHeaderDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogUpdatesDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.GenericResponseV1DTO;
import com.examsofbharat.bramhsastra.jal.dto.response.HomeBlogDataDTO;
import com.examsofbharat.bramhsastra.prithvi.entity.*;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Random;


@Component
public class MapperUtils {
    //ENTITY TO DTO
    public static GenericResponseV1DTO toUpdateSectionDTO(GenericResponseV1 genericResponseV1){
        GenericResponseV1DTO genericResponseV1DTO = genericResponseV1 == null ? null :
                GenericResV1DTOMapper.INSTANCE.toGenericResV1DTO(genericResponseV1);

        if(Objects.isNull(genericResponseV1DTO)){
            return null;
        }
        Random random = new Random();
        genericResponseV1DTO.setRating(2 + random.nextInt(4));
        genericResponseV1DTO.setSeenValue(2000 + random.nextInt(8001));

        return genericResponseV1DTO;
    }

    public static BlogHeaderDTO toBlogHeaderDTO(BlogHeader blogHeader) {
        return blogHeader == null ? null :
                BlogHeaderMapper.INSTANCE.toBlogHeaderDTO(blogHeader);
    }

    public static BlogUpdatesDTO toBlogUpdatesDTO(BlogUpdates blogUpdates) {
        return blogUpdates == null ? null :
                BlogUpdatesMapper.INSTANCE.toBlogHeaderDTO(blogUpdates);
    }

    public static HomeBlogDataDTO toHomeBlogDTO(BlogHeader entity) {
        return entity == null ? null :
                HomeBlogDataDTOMapper.INSTANCE.toBlogHeaderDTO(entity);
    }

    public static ApplicationContentManagerDTO toApplicationContentDTO(ApplicationContentManager entity) {
        return entity == null ? null :
                FormContentMapper.INSTANCE.toFormContentDTO(entity);
    }

    public static ApplicationSeoDetailsDTO toApplicationSeoDetailsDTO(ApplicationSeoDetails entity) {
        return entity == null ? null :
                SeoDataMapper.INSTANCE.toFormContentDTO(entity);
    }

}
