package com.frank.hosp.repository;

import com.frank.model.HospitalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalSettingRepository extends JpaRepository<HospitalSetting, Long> {
}