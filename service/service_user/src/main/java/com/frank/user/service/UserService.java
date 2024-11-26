package com.frank.user.service;

import com.frank.common.JwtHelper;
import com.frank.model.User;
import com.frank.user.dto.LoginRequest;
import com.frank.user.dto.UserAuthRequest;
import com.frank.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setName("");
                    newUser.setNickName("");
                    newUser.setEmail(request.getEmail());
                    return userRepository.save(newUser);
                });
        map.put("name", user.getName());
        map.put("token", JwtHelper.createToken(user.getId(), user.getName()));
        return map;
    }

    public void userAuth(Long userId, UserAuthRequest userAuthRequest) {
        userRepository.findById(userId).ifPresent(u -> {
            u.setName(userAuthRequest.getName());
            u.setAuthNo(userAuthRequest.getAuthNo());
            userRepository.save(u);
        });
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
