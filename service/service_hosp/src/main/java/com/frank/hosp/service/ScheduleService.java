package com.frank.hosp.service;

import com.frank.hosp.dto.ScheduleDeleteRequest;
import com.frank.hosp.dto.ScheduleDetail;
import com.frank.hosp.dto.ScheduleQuery;
import com.frank.hosp.dto.ScheduleRule;
import com.frank.hosp.repository.HospitalRepository;
import com.frank.hosp.repository.ScheduleRepository;
import com.frank.model.Hospital;
import com.frank.model.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final HospitalRepository hospitalRepository;
    private final MongoTemplate mongoTemplate;

    public void save(List<Schedule> schedules) {
        schedules.forEach(s -> {
            Schedule scheduleInDB = scheduleRepository.findByHosCodeAndHosScheduleId(s.getHosCode(), s.getHosScheduleId());
            if (scheduleInDB != null) {
                scheduleInDB.setUpdateTime(new Date());
                scheduleInDB.setStatus(1);
                scheduleRepository.save(scheduleInDB);
            } else {
                s.setCreateTime(new Date());
                s.setUpdateTime(new Date());
                s.setStatus(1);
                s.setIsDeleted(0);
                scheduleRepository.save(s);
            }
        });
    }

    public List<Schedule> findSchedule(ScheduleQuery query) {
        PageRequest pageRequest = PageRequest.of(query.getPage() - 1, query.getLimit());
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(query, schedule);
        schedule.setIsDeleted(0);
        schedule.setStatus(1);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Example<Schedule> example = Example.of(schedule, matcher);
        return scheduleRepository.findAll(example, pageRequest).getContent();
    }

    public void removeSchedule(ScheduleDeleteRequest request) {
        Schedule scheduleInDB = scheduleRepository.findByHosCodeAndHosScheduleId(request.getHosCode(), request.getHosScheduleId());
        if (scheduleInDB != null) {
            scheduleRepository.delete(scheduleInDB);
        }
    }

    public Map<String, Object> getScheduleRule(long page, long limit, String hosCode) {
        Criteria criteria = Criteria.where("hosCode").is(hosCode);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria), // filter
                Aggregation.group("workDate") // grouping and counting
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("maxCount").as("maxCount")
                        .sum("availableCount").as("availableCount"),
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<ScheduleRule> results = mongoTemplate.aggregate(aggregation, Schedule.class, ScheduleRule.class);
        List<ScheduleRule> scheduleRuleList = results.getMappedResults();
        // calculate total count
        Aggregation totalAgg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<ScheduleRule> totalAggResults = mongoTemplate.aggregate(totalAgg, Schedule.class, ScheduleRule.class);
        int total = totalAggResults.getMappedResults().size();

        scheduleRuleList.forEach(scheduleRule -> {
            LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(scheduleRule.getWorkDate().getTime() / 1000), ZoneId.systemDefault());
            scheduleRule.setDayOfWeek(localDate.getDayOfWeek().name());
        });

        Hospital hospital = hospitalRepository.getHospitalByHosCode(hosCode);

        Map<String, Object> result = new HashMap<>();
        result.put("scheduleRuleList", scheduleRuleList);
        result.put("total", total);
        result.put("hosName", hospital.getHosName());
        return result;
    }

    public List<ScheduleDetail> getScheduleDetail(String hosCode, String workDate) {
        List<ScheduleDetail> res;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parse = format.parse(workDate);
            List<Schedule> scheduleList = scheduleRepository.findByHosCodeAndWorkDate(hosCode, parse);
            res = scheduleList.stream().map(this::transToScheduleDetail).toList();
        } catch (ParseException e) {
            log.error("workDate parse error.", e);
            res = new ArrayList<>();
        }
        return res;
    }

    private ScheduleDetail transToScheduleDetail(Schedule schedule) {
        ScheduleDetail detail = new ScheduleDetail();
        BeanUtils.copyProperties(schedule, detail);
        detail.setHosName(hospitalRepository.getHospitalByHosCode(schedule.getHosCode()).getHosName());
        LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(schedule.getWorkDate().getTime() / 1000), ZoneId.systemDefault());
        detail.setDayOfWeek(localDate.getDayOfWeek().name());
        return detail;
    }
}
