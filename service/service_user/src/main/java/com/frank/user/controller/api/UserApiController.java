package com.frank.user.controller.api;

import com.frank.common.AuthHelper;
import com.frank.common.Result;
import com.frank.model.User;
import com.frank.user.dto.LoginRequest;
import com.frank.user.dto.UserAuthRequest;
import com.frank.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request) {
        Map<String, Object> map = userService.login(request);
        return Result.ok(map);
    }

    @PostMapping("/auth/userAuth")
    public Result<?> userAuth(@RequestBody UserAuthRequest userAuthRequest, HttpServletRequest request) {
        userService.userAuth(AuthHelper.getUserId(request), userAuthRequest);
        return Result.ok();
    }

    @GetMapping("/auth/userInfo")
    public Result<User> getUser(HttpServletRequest request) {
        return Result.ok(userService.getUserById(AuthHelper.getUserId(request)));
    }
}
