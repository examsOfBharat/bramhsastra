package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.BlogUpdatesRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.BlogUpdates;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlogUpdatesManagerImpl extends GenericManager<BlogUpdates, String> {

    BlogUpdatesRepository blogUpdatesRepository;
    public BlogUpdatesManagerImpl(BlogUpdatesRepository blogUpdatesRepository) {
        this.blogUpdatesRepository = blogUpdatesRepository;
        setCrudRepository(blogUpdatesRepository);
    }

    public List<BlogUpdates> fetchAllBlogUpdatesById(String id){
        return blogUpdatesRepository.findAllByRefIdIsOrderBySequenceAsc(id);
    }
}
