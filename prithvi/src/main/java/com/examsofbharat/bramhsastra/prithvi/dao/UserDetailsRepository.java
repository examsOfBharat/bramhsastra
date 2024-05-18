package com.examsofbharat.bramhsastra.prithvi.dao;

import com.examsofbharat.bramhsastra.prithvi.entity.UserDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserDetailsRepository extends CrudRepository<UserDetails, String> {

    Optional<UserDetails> findByEmailId(String userName);

    List<UserDetails> findByUserStatusOrderByDateModifiedDesc(UserDetails.UserStatus userStatus);
}
