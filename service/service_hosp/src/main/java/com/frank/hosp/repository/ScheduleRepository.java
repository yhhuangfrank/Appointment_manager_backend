package com.frank.hosp.repository;

import com.frank.model.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Schedule findByHosCodeAndHosScheduleId(String hosCode, String hosScheduleId);

    List<Schedule> findByHosCodeAndWorkDate(String hosCode, Date workDate);
}
