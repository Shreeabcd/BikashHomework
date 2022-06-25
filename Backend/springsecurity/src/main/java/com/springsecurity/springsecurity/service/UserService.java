package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.entity.UserDetails;
import com.springsecurity.springsecurity.exception.ResourceUnAvailableException;
import com.springsecurity.springsecurity.provider.PasswordCryptographyProvider;
import com.springsecurity.springsecurity.repository.UserDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDetailsRepository userDetailsRepository;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;


    public UserDetails register(UserDetails userDetails) {


        userDetails.setCreatedDate(LocalDate.now().toString());
        encryptPassword(userDetails);
        userDetailsRepository.save(userDetails);
        return userDetails;
    }

    public UserDetails getUser(String id) {
        return Optional.ofNullable(userDetailsRepository.findById(id))
                .get()
                .orElseThrow(ResourceUnAvailableException::new);
    }




    private void encryptPassword(final UserDetails newUser) {

        String password = newUser.getPassword();
        final String[] encryptedData = passwordCryptographyProvider.encrypt(password);
        newUser.setSalt(encryptedData[0]);
        newUser.setPassword(encryptedData[1]);
    }
}

