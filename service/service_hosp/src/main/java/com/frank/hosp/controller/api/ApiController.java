package com.frank.hosp.controller.api;

import com.frank.common.Result;
import com.frank.hosp.dto.HospitalQuery;
import com.frank.hosp.dto.ScheduleDeleteRequest;
import com.frank.hosp.dto.ScheduleQuery;
import com.frank.hosp.service.HospitalService;
import com.frank.hosp.service.ScheduleService;
import com.frank.model.Hospital;
import com.frank.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/findHospList/{page}/{limit}")
    public Result<Page<Hospital>> findHospList(@PathVariable("page") int page,
                                               @PathVariable("limit") int limit,
                                               @RequestBody(required = false) HospitalQuery query
    ) {
        Page<Hospital> pageObj = hospitalService.findHosByPage(page, limit, query);
        return Result.ok(pageObj);
    }

    @GetMapping("/{id}")
    public Result<Hospital> findById(@PathVariable("id") String id) {
        return Result.ok(hospitalService.findById(id));
    }

    @GetMapping("/findHospByHosName/{hosName}")
    public Result<List<Hospital>> findHospByHosName(@PathVariable("hosName") String hosName) {
        return Result.ok(hospitalService.findByHosName(hosName));
    }
}
