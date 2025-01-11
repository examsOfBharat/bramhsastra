package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.BlogHeaderRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogHeader;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BlogHeaderManagerImpl extends GenericManager<BlogHeader, String> {

    BlogHeaderRepository blogHeaderRepository;

    @Autowired
    public BlogHeaderManagerImpl(BlogHeaderRepository blogHeaderRepository) {
        this.blogHeaderRepository = blogHeaderRepository;
        setCrudRepository(blogHeaderRepository);
    }

    public List<BlogHeader> getBlogHeadersList(Date dateCriteria){
        return blogHeaderRepository.findAllOrderByDateCreatedAfterOrderByDateCreatedDesc(dateCriteria);
    }

    public BlogHeader getBlogHeader(String headerId){
        return blogHeaderRepository.findById(headerId).get();
    }
}
