package com.vladimir.zubencko.service;

import com.vladimir.zubencko.domain.User;

public interface UserService {
    User getByLogin(String login);
    void registrationUser(String login, String password);
    Boolean isAuthenticate();
}
