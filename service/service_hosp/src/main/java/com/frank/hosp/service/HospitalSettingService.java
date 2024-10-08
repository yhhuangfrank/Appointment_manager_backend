package com.frank.hosp.service;

import com.frank.hosp.repository.HospitalSettingRepository;
import com.frank.model.HospitalSetting;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalSettingService {

    private final HospitalSettingRepository hospitalSettingRepository;

    public List<HospitalSetting> findAll() {
        return hospitalSettingRepository.findAll();
    }

    // soft delete
    public void deleteById(Long id) {
        HospitalSetting hospitalSetting = hospitalSettingRepository.findById(id).orElse(null);
        if (hospitalSetting != null) {
            hospitalSetting.setIsDeleted(1);
            hospitalSettingRepository.save(hospitalSetting);
        }
    }

    public List<HospitalSetting> findAllByPage(Integer pageNum, Integer pageSize) {
        int page = pageNum == null || pageNum - 1 <= 0 ? 0 : pageNum - 1;
        int size = pageSize == null ? 10 : pageSize;
        PageRequest pageRequest = PageRequest.of(page, size);
        return hospitalSettingRepository.findAll(pageRequest).getContent();
    }

    public void save(HospitalSetting hospitalSetting) {
        hospitalSettingRepository.save(hospitalSetting);
    }

    public HospitalSetting findById(Long id) {
        return hospitalSettingRepository.findById(id).orElse(null);
    }

    public void update(HospitalSetting hospitalSetting) {
        hospitalSettingRepository
                .findById(hospitalSetting.getId())
                .ifPresent(
                        h -> {
                            h.setHosName(hospitalSetting.getHosName());
                            h.setHosCode(hospitalSetting.getHosCode());
                            hospitalSettingRepository.save(h);
                        }
                );
    }
}
