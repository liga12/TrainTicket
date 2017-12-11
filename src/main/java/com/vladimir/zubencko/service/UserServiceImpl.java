package com.vladimir.zubencko.service;

import com.vladimir.zubencko.ROLE;
import com.vladimir.zubencko.domain.User;
import com.vladimir.zubencko.domain.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@Log4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public User getByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void registrationUser(String login, String password) {
        User user = new User(login, passwordEncoder().encode(password), ROLE.ROLE_ADMIN);
        userRepository.saveAndFlush(user);
    }

    @Override
    public Boolean isAuthenticate() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof User) {
            log.debug("Authentication success");
            return true;
        } else {
            log.debug("Authentication failed");
            return false;
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final Md5PasswordEncoder md5 = new Md5PasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return md5.encodePassword(rawPassword.toString(), 1);
            }

            @Override
            @SuppressWarnings("PMD")
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                log.debug("Get encode password");
                return (md5.encodePassword(rawPassword.toString(), 1)).equals(encodedPassword);
            }
        };
    }

}
