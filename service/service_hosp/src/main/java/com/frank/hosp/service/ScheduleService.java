package com.frank.hosp.service;

import com.frank.hosp.dto.ScheduleDeleteRequest;
import com.frank.hosp.dto.ScheduleQuery;
import com.frank.hosp.repository.ScheduleRepository;
import com.frank.model.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

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
        schedule.setHosCode(query.getHosCode());
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
}
