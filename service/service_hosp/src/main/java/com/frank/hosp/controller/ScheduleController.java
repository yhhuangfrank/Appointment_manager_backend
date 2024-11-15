package com.frank.hosp.controller;

import com.frank.common.Result;
import com.frank.hosp.dto.ScheduleDetail;
import com.frank.hosp.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hosp/schedule")
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

    @GetMapping("/getScheduleDetail/{hosCode}/{workDate}")
    public Result<List<ScheduleDetail>> getScheduleDetail(
            @PathVariable("hosCode") String hosCode,
            @PathVariable("workDate") String workDate
    ) {
        List<ScheduleDetail> list = scheduleService.getScheduleDetail(hosCode, workDate);
        return Result.ok(list);
    }
}
