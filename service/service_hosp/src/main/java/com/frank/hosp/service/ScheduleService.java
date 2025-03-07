package com.frank.hosp.service;

import com.frank.hosp.dto.ScheduleDeleteRequest;
import com.frank.hosp.dto.ScheduleDetail;
import com.frank.hosp.dto.ScheduleQuery;
import com.frank.hosp.dto.ScheduleRule;
import com.frank.hosp.repository.HospitalRepository;
import com.frank.hosp.repository.ScheduleRepository;
import com.frank.model.Hospital;
import com.frank.model.Schedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Map<String, Object> getBookingScheduleRule(Integer page, Integer limit, String hosCode) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = hospitalRepository.getHospitalByHosCode(hosCode);
        PagedDate pagedDateList = getListDate(page, limit);
        Criteria criteria = Criteria.where("hosCode").is(hosCode).and("workDate").in(pagedDateList.getContent());
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate").first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("maxCount").as("maxCount")
                        .sum("availableCount").as("availableCount")
        );
        AggregationResults<ScheduleRule> aggregationResults = mongoTemplate.aggregate(aggregation, Schedule.class, ScheduleRule.class);
        List<ScheduleRule> mappedResults = aggregationResults.getMappedResults();
        Map<Date, ScheduleRule> scheduleRuleMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(mappedResults)) {
            scheduleRuleMap = mappedResults.stream().collect(Collectors.toMap(ScheduleRule::getWorkDate, Function.identity()));
        }
        List<ScheduleRule> scheduleRuleList = new ArrayList<>();
        List<Date> dateList = pagedDateList.getContent();
        for (int i = 0; i < dateList.size(); i++) {
            Date date = dateList.get(i);
            ScheduleRule scheduleRule = scheduleRuleMap.get(date);
            if (scheduleRule == null) {
                scheduleRule = new ScheduleRule();
                scheduleRule.setAvailableCount(0);
                scheduleRule.setDocCount(0);
            }
            scheduleRule.setWorkDate(date);
            LocalDate localDate = LocalDate.ofInstant(Instant.ofEpochSecond(date.getTime() / 1000), ZoneId.systemDefault());
            scheduleRule.setDayOfWeek(localDate.getDayOfWeek().name());

            if (i == dateList.size() - 1 && page == pagedDateList.getTotalPages()) {
                scheduleRule.setStatus(0);
            } else {
                scheduleRule.setStatus(1);
            }
            scheduleRuleList.add(scheduleRule);
        }
        result.put("scheduleRuleList", scheduleRuleList);
        result.put("total", pagedDateList.getTotalElements());
        result.put("hosName", hospital.getHosName());
        return result;
    }

    private PagedDate getListDate(int page, int limit) {
        String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime releaseTime = LocalDateTime.parse(dateString.concat(" 08:30"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        int period = 10;
        if (releaseTime.isBefore(LocalDateTime.now())) {
            period += 1;
        }
        List<Date> dateList = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < period; i++) {
            String curDateTime = releaseTime.plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            try {
                dateList.add(dateFormat.parse(curDateTime));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        List<Date> pageDateList = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = (page - 1) * limit + limit;
        if (end > dateList.size()) {
            end = dateList.size();
        }
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        int totalPages = (int) Math.ceil((double) dateList.size() / (double) limit);
        return new PagedDate(pageDateList, dateList.size(), totalPages);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class PagedDate {
        private List<Date> content;
        private int totalElements;
        private int totalPages;
    }
}
