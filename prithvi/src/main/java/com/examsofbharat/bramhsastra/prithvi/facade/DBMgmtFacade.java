package com.examsofbharat.bramhsastra.prithvi.facade;

import com.examsofbharat.bramhsastra.prithvi.entity.SystemProperty;
import com.examsofbharat.bramhsastra.prithvi.entity.UserDetails;
import com.examsofbharat.bramhsastra.prithvi.manager.SystemRepositoryManagerImp;
import com.examsofbharat.bramhsastra.prithvi.manager.UserDetailsManagerImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class DBMgmtFacade {

    @Autowired
    UserDetailsManagerImp userDetailsManagerImp;
    @Autowired
    private SystemRepositoryManagerImp systemPropertyManager;

    public UserDetails getUserDetails(String userId){
        Optional<UserDetails> userDetails = userDetailsManagerImp.findUserByUserId(userId);
        return userDetails.orElse(null);
    }

    public List<UserDetails> getUserDetailsByUserStatus(UserDetails.UserStatus userStatus){
        return userDetailsManagerImp.findUserByUserStatus(userStatus);
    }

    public UserDetails findUserByUserId(String userId){
        return userDetailsManagerImp.findUserById(userId);
    }

    public UserDetails saveUserDetails(UserDetails userDetails){
        return userDetailsManagerImp.save(userDetails);
    }

    public SystemProperty getSystemProperty(String propertyName){
        return systemPropertyManager.getSystemProperty(propertyName);
    }

    public void updateUserDetails(UserDetails userDetails){
        userDetailsManagerImp.save(userDetails);
    }

    public void deleteUserDetails(UserDetails userDetails){
        userDetailsManagerImp.delete(userDetails);
    }
}
