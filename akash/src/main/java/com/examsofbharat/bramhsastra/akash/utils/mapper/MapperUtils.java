package com.examsofbharat.bramhsastra.akash.utils.mapper;

import com.examsofbharat.bramhsastra.jal.dto.response.GenericResponseV1DTO;
import com.examsofbharat.bramhsastra.prithvi.entity.GenericResponseV1;
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
}
