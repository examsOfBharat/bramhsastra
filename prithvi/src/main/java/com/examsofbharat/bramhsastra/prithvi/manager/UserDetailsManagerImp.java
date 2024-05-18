package com.examsofbharat.bramhsastra.prithvi.manager;

import com.examsofbharat.bramhsastra.prithvi.dao.UserDetailsRepository;
import com.examsofbharat.bramhsastra.prithvi.entity.UserDetails;
import com.examsofbharat.bramhsastra.prithvi.sql.GenericManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDetailsManagerImp extends GenericManager<UserDetails, String> {
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    public UserDetailsManagerImp(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
        setCrudRepository(userDetailsRepository);
    }

    public Optional<UserDetails> findUserByUserId(String userId){
        return userDetailsRepository.findByEmailId(userId);
    }

    public List<UserDetails> findUserByUserStatus(UserDetails.UserStatus userStatus){
        return userDetailsRepository.findByUserStatusOrderByDateModifiedDesc(userStatus);
    }

    public UserDetails findUserById(String userId){
        return userDetailsRepository.findById(userId).orElse(null);
    }


}
