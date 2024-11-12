package com.frank.hosp.controller.api;

import com.frank.common.Result;
import com.frank.hosp.dto.ScheduleDeleteRequest;
import com.frank.hosp.dto.ScheduleQuery;
import com.frank.hosp.service.HospitalService;
import com.frank.hosp.service.ScheduleService;
import com.frank.model.Hospital;
import com.frank.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hosp")
public class ApiController {

    private final HospitalService hospitalService;
    private final ScheduleService scheduleService;

    @PostMapping("/saveHospital")
    public Result<?> saveHosp(@RequestBody Hospital request) {
        hospitalService.save(request);
        return Result.ok();
    }

    @PostMapping("/saveSchedule")
    public Result<?> saveSchedule(@RequestBody List<Schedule> requests) {
        scheduleService.save(requests);
        return Result.ok();
    }

    @PostMapping("/schedule/list")
    public Result<List<Schedule>> findSchedule(@RequestBody ScheduleQuery query) {
        return Result.ok(scheduleService.findSchedule(query));
    }

    @PostMapping("/schedule/remove")
    public Result<?> removeSchedule(@RequestBody ScheduleDeleteRequest request) {
        scheduleService.removeSchedule(request);
        return Result.ok();
    }
}
