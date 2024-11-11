package com.frank.hosp.service;

import com.frank.hosp.repository.HospitalRepository;
import com.frank.model.Hospital;
import lombok.RequiredArgsConstructor;
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
}
