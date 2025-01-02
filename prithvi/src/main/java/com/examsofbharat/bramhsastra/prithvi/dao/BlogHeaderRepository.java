package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.BlogHeader;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface BlogHeaderRepository extends CrudRepository<BlogHeader, String> {

    List<BlogHeader> findAllOrderByDateCreatedAfter(Date date);

}
