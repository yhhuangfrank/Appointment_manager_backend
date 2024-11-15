package com.frank.hosp.controller;

import com.frank.common.Result;
import com.frank.hosp.dto.HospitalQuery;
import com.frank.hosp.service.HospitalService;
import com.frank.model.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hosp")
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping("/search/{page}/{limit}")
    public Result<Page<Hospital>> findHosByPage(@PathVariable("page") int page,
                                                @PathVariable("limit") int limit,
                                                @RequestBody(required = false) HospitalQuery query) {
        return Result.ok(hospitalService.findHosByPage(page, limit, query));
    }

    @PostMapping("/")
    public Result<?> add(@RequestBody Hospital hospital) {
        hospitalService.save(hospital);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") String id) {
        hospitalService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<Hospital> findById(@PathVariable("id") String id) {
        return Result.ok(hospitalService.findById(id));
    }

    @PutMapping("/update/{id}/{status}")
    public Result<?> updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        hospitalService.updateStatusById(id, status);
        return Result.ok();
    }

    @GetMapping("/all/names")
    public Result<List<Map<String, String>>> getAllNames() {
        return Result.ok(hospitalService.findAllName());
    }
}
