package com.examsofbharat.bramhsastra.akash.processors.blogProcessor;

import com.examsofbharat.bramhsastra.akash.utils.DateUtils;
import com.examsofbharat.bramhsastra.akash.utils.EobInitilizer;
import com.examsofbharat.bramhsastra.akash.utils.FormUtil;
import com.examsofbharat.bramhsastra.akash.utils.WebUtils;
import com.examsofbharat.bramhsastra.akash.utils.mapper.MapperUtils;
import com.examsofbharat.bramhsastra.jal.dto.ApplicationContentManagerDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogHeaderDTO;
import com.examsofbharat.bramhsastra.jal.dto.BlogUpdatesDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.BlogHomeUpdateDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.BlogResponseDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.FormLandingPageDTO;
import com.examsofbharat.bramhsastra.jal.dto.response.HomeBlogDataDTO;
import com.examsofbharat.bramhsastra.jal.utils.StringUtil;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationContentManager;
import com.examsofbharat.bramhsastra.prithvi.entity.ApplicationSeoDetails;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogHeader;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogUpdates;
import com.examsofbharat.bramhsastra.prithvi.facade.DBMgmtFacade;
import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Component
@Slf4j
public class ProcessBlogResponse {

    @Autowired
    EobInitilizer eobInitilizer;

    @Autowired
    DBMgmtFacade dbMgmtFacade;

    @Autowired
    WebUtils webUtils;

    public void processHomeBlog(FormLandingPageDTO formLandingPageDTO){

        log.info("Loading blog Landing data.............");
        int blogCount = eobInitilizer.getBlogMaxCount();
        Date date = DateUtils.addDays(new Date(), -blogCount);

        List<HomeBlogDataDTO> homeBlogDataDTOS = new ArrayList<>();
        List<BlogHeader> blogHeaderList = dbMgmtFacade.fetchAllBlogHeaderAfterDate(date);

        for(BlogHeader blogHeader : blogHeaderList){
            HomeBlogDataDTO blogData = MapperUtils.toHomeBlogDTO(blogHeader);
            blogData.setShortTitle(FormUtil.getUrlTitle(blogHeader.getShortTitle()));
            blogData.setBlogId(blogHeader.getId());
            homeBlogDataDTOS.add(blogData);

            BlogHeaderDTO blogHeaderDTO = MapperUtils.toBlogHeaderDTO(blogHeader);
            blogHeaderDTO.setTagsList(getTagList(blogHeader));
            FormUtil.blogCache.put(blogHeader.getId(), blogHeaderDTO);
        }

        if(!CollectionUtils.isEmpty(homeBlogDataDTOS)){
            BlogHomeUpdateDTO blogHomeUpdateDTO = new BlogHomeUpdateDTO();
            blogHomeUpdateDTO.setBlogDetailsList(homeBlogDataDTOS);
            formLandingPageDTO.setHomeBlogUpdateDTO(blogHomeUpdateDTO);
        }

    }

    private List<String> getTagList(BlogHeader blogHeader){
        if(Objects.isNull(blogHeader) || StringUtil.isEmpty(blogHeader.getTags())){
            return Collections.emptyList();
        }
        return Arrays.asList(blogHeader.getTags().trim().split(","));
    }

    public Response processBlogResponse(String blogId){

        BlogHeaderDTO blogHeaderDto = FormUtil.blogCache.get(blogId);

        if(Objects.isNull(blogHeaderDto)){
            BlogHeader blogHeader = dbMgmtFacade.fetchBlogHeaderById(blogId);
            blogHeaderDto = MapperUtils.toBlogHeaderDTO(blogHeader);
            if( Objects.nonNull(blogHeaderDto) && StringUtil.notEmpty(blogHeaderDto.getShortTitle())) {
                blogHeaderDto.setShortTitle(FormUtil.getUrlTitle(blogHeaderDto.getShortTitle()));
            }
        }


        BlogResponseDTO blogResponseDTO  = new BlogResponseDTO();
        blogResponseDTO.setBlogHeader(blogHeaderDto);

        List<BlogUpdates> blogUpdates = dbMgmtFacade.fetchAllBlogUpdatesById(blogId);
        List<BlogUpdatesDTO> blogUpdatesDTOList = new ArrayList<>();

        for(BlogUpdates blogUpdate : blogUpdates){
            blogUpdatesDTOList.add(MapperUtils.toBlogUpdatesDTO(blogUpdate));
        }

        blogResponseDTO.setBlogUpdatesList(blogUpdatesDTOList);

        List<ApplicationContentManager> contentManagers = dbMgmtFacade.getApplicationContentDetails(blogId);
        List<ApplicationContentManagerDTO> contentManagerDTOList = new ArrayList<>();
        for(ApplicationContentManager contentManager : contentManagers){
            contentManagerDTOList.add(MapperUtils.toApplicationContentDTO(contentManager));
        }

        ApplicationSeoDetails applicationSeoDetails = dbMgmtFacade.getApplicationSeoDetails(blogId);
        blogResponseDTO.setSeoDetailsDTO(MapperUtils.toApplicationSeoDetailsDTO(applicationSeoDetails));
        blogResponseDTO.setContentManagerDTOList(contentManagerDTOList);
        String response = new Gson().toJson(blogResponseDTO);

        return Response.ok(response).build();
    }
}
