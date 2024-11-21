package com.frank.hosp.service;

import com.frank.config.DictionaryFeignClient;
import com.frank.hosp.dto.HospitalQuery;
import com.frank.hosp.repository.HospitalRepository;
import com.frank.model.Hospital;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final DictionaryFeignClient dictionaryFeignClient;
    private final MongoTemplate mongoTemplate;

    public void save(Hospital request) {
        hospitalRepository.findById(request.getId())
                .ifPresentOrElse(h -> {
                    h.setUpdateTime(new Date());
                    h.setDictCode(request.getDictCode());
                    h.setHosName(request.getHosName());
                    hospitalRepository.save(h);
                }, () -> {
                    request.setStatus(0);
                    request.setCreateTime(new Date());
                    request.setUpdateTime(new Date());
                    request.setIsDeleted(0);
                    hospitalRepository.save(request);
                });
    }

    public Page<Hospital> findHosByPage(int page, int limit, HospitalQuery query) {
        Page<Hospital> hospitals;
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (query == null) {
            hospitals = hospitalRepository.findAll(pageable);
        } else {
            Hospital hospital = new Hospital();
            BeanUtils.copyProperties(query, hospital);
            ExampleMatcher matcher = ExampleMatcher.matching()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                    .withIgnoreCase(true);
            Example<Hospital> example = Example.of(hospital, matcher);
            hospitals = hospitalRepository.findAll(example, pageable);
        }
        hospitals.getContent().forEach(this::setLevel);
        return hospitals;
    }

    private void setLevel(Hospital h) {
        String level;
        try {
            level = dictionaryFeignClient.findName(h.getDictCode()).getData();
        } catch (Exception e) {
            log.error("dictionaryFeignClient error.", e);
            level = "";
        }
        h.setLevel(level);
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
        Hospital hospital = hospitalRepository.findById(id).orElse(null);
        if (hospital != null) {
            setLevel(hospital);
        }
        return hospital;
    }

    public List<Map<String, String>> findAllName() {
        Query query = new Query();
        query.fields().include("hosCode", "hosName").exclude("_id");
        return mongoTemplate.find(query, Hospital.class).stream().map(h -> {
            Map<String, String> map = new HashMap<>();
            map.put("hosCode", h.getHosCode());
            map.put("hosName", h.getHosName());
            return map;
        }).toList();
    }

    public List<Hospital> findByHosName(String hosName) {
        return hospitalRepository.findByHosNameLike(hosName);
    }
}
