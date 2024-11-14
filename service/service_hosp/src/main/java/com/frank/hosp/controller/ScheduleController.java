package com.frank.hosp.controller;

import com.frank.common.Result;
import com.frank.hosp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hosp/schedule")
@CrossOrigin
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping("/getScheduleRule/{page}/{limit}/{hosCode}")
    public Result<?> getScheduleRule(@PathVariable("page") long page,
                                     @PathVariable("limit") long limit,
                                     @PathVariable("hosCode") String hosCode
    ) {
        Map<String, Object> map = scheduleService.getScheduleRule(page, limit, hosCode);
        return Result.ok(map);
    }
}
