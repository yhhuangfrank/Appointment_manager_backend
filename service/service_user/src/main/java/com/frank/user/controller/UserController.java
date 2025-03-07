package com.frank.user.controller;

import com.frank.common.Result;
import com.frank.model.User;
import com.frank.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{page}/{limit}")
    public Result<?> list(@PathVariable("page") Integer page,
                          @PathVariable("limit") Integer limit,
                          @RequestBody User userRequest) {
        Page<User> userByPage = userService.findByPage(page, limit, userRequest);
        return Result.ok(userByPage);
    }
}
