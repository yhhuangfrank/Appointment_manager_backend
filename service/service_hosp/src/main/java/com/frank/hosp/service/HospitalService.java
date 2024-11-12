package com.frank.hosp.service;

import com.frank.hosp.dto.HospitalQuery;
import com.frank.hosp.repository.HospitalRepository;
import com.frank.model.Hospital;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public void save(Hospital request) {
        Hospital hospital = hospitalRepository.getHospitalByHosCode(request.getHosCode());

        if (hospital != null) {
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        } else {
            request.setStatus(0);
            request.setCreateTime(new Date());
            request.setUpdateTime(new Date());
            request.setIsDeleted(0);
            hospitalRepository.save(request);
        }
    }

    public Page<Hospital> findHosByPage(int page, int limit, HospitalQuery query) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (query == null) {
            return hospitalRepository.findAll(pageable);
        }
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(query, hospital);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Hospital> example = Example.of(hospital, matcher);
        return hospitalRepository.findAll(example, pageable);
    }

    public void updateStatusById(String id, Integer status) {
        hospitalRepository.findById(id).ifPresent(
                h -> {
                    h.setStatus(status);
                    h.setUpdateTime(new Date());
                    hospitalRepository.save(h);
                }
        );
    }

    public void deleteById(String id) {
        hospitalRepository.deleteById(id);
    }

    public Hospital findById(String id) {
        return hospitalRepository.findById(id).orElse(null);
    }
}
