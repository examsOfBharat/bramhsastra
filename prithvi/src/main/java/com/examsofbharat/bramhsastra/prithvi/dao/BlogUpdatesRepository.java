package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.BlogUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogUpdatesRepository extends JpaRepository<BlogUpdates, String> {

    List<BlogUpdates> findAllByRefIdIsOrderBySequenceAsc(String refId);
}
