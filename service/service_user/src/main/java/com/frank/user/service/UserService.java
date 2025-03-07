package com.frank.user.service;

import com.frank.common.JwtHelper;
import com.frank.model.User;
import com.frank.model.enums.AuthStatus;
import com.frank.user.dto.LoginRequest;
import com.frank.user.dto.UserAuthRequest;
import com.frank.user.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Page<User> findByPage(Integer page, Integer limit, User userRequest) {
        Specification<User> spec = filterByCriteria(userRequest);
        PageRequest pageRequest = PageRequest.of(page - 1, limit);
        Page<User> userPage = userRepository.findAll(spec, pageRequest);
        userPage.getContent().forEach(user -> {
            AuthStatus.getStatusNameByStatusCode(user.getAuthStatus());
        });
        return userPage;
    }

    private Specification<User> filterByCriteria(User userRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate>  predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(userRequest.getName())) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + userRequest.getName() +"%"));
            }
            if (StringUtils.isNotBlank(String.valueOf(userRequest.getAuthStatus()))) {
                predicates.add(criteriaBuilder.equal(root.get("authStatus"), userRequest.getAuthStatus()));
            }
            if (StringUtils.isNotBlank(userRequest.getCreateTime().toString())) {
                predicates.add(criteriaBuilder.ge(root.get("createTime"), userRequest.getCreateTime().toEpochMilli()));
            }
            if (StringUtils.isNotBlank(userRequest.getUpdateTime().toString())) {
                predicates.add(criteriaBuilder.le(root.get("updateTime"), userRequest.getUpdateTime().toEpochMilli()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
