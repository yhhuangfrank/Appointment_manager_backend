package com.frank.hosp.controller;

import com.frank.common.Result;
import com.frank.hosp.dto.SearchRequest;
import com.frank.hosp.dto.SearchResponse;
import com.frank.hosp.service.HospitalSettingService;
import com.frank.model.HospitalSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/hosp/hospitalSettings")
@CrossOrigin
public class HospitalSettingController {

    private final HospitalSettingService hospitalSettingService;

    @GetMapping("/all")
    public Result<List<HospitalSetting>> findAll() {
        return Result.ok(hospitalSettingService.findAll());
    }

    @PostMapping("/search/{page}/{limit}")
    public Result<SearchResponse<HospitalSetting>> search(@PathVariable(value = "page", required = false) Integer pageNum,
                                         @PathVariable(value = "limit", required = false) Integer pageSize,
                                         @RequestBody(required = false) SearchRequest request) {
        if (request != null) {
            System.out.println(request.getHosName());
            System.out.println(request.getHosCode());
        }

        return Result.ok(hospitalSettingService.findAllByPage(pageNum, pageSize, request));
    }

    @PostMapping("/")
    public Result<?> add(@RequestBody HospitalSetting hospitalSetting) {
        hospitalSettingService.save(hospitalSetting);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") Long id) {
        hospitalSettingService.deleteById(id);
        return Result.ok();
    }

    @GetMapping("/{id}")
    public Result<HospitalSetting> findById(@PathVariable("id") Long id) {
        return Result.ok(hospitalSettingService.findById(id));
    }

    @PostMapping("/update")
    public Result<HospitalSetting> update(@RequestBody HospitalSetting hospitalSetting) {
        hospitalSettingService.update(hospitalSetting);
        return Result.ok();
    }
}
