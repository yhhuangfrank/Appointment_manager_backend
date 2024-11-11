package com.frank.hosp.repository;

import com.frank.model.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    Hospital getHospitalByHosCode(String hosCode);
}
