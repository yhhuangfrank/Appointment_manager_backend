package com.frank.hosp.repository;

import com.frank.model.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    Hospital getHospitalByHosCode(String hosCode);

    List<Hospital> findByHosNameLike(String hosName);
}
