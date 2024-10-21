package com.frank.hosp.service;

import com.frank.hosp.dto.SearchRequest;
import com.frank.hosp.dto.SearchResponse;
import com.frank.hosp.repository.HospitalSettingRepository;
import com.frank.model.HospitalSetting;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HospitalSettingService {

    private final HospitalSettingRepository hospitalSettingRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    public SearchResponse<HospitalSetting> findAllByPage(Integer pageNum, Integer pageSize, SearchRequest request) {
        int page = pageNum == null || pageNum - 1 <= 0 ? 0 : pageNum - 1;
        int size = pageSize == null ? 10 : pageSize;
        String baseSql = """
                    FROM HospitalSetting h
                    WHERE 1=1
                """;
        StringBuilder countSqlString =  new StringBuilder("SELECT COUNT(*) " + baseSql);
        StringBuilder sqlString = new StringBuilder(baseSql);

        if (request != null && StringUtils.isNotBlank(request.getHosName())) {
            sqlString.append(" AND h.hosName LIKE :hosName ");
            countSqlString.append(" AND h.hosName LIKE :hosName ");
        }
        if (request != null && StringUtils.isNotBlank(request.getHosCode())) {
            sqlString.append(" AND h.hosCode = :hosCode ");
            countSqlString.append(" AND h.hosCode = :hosCode ");
        }

        TypedQuery<HospitalSetting> query = entityManager.createQuery(sqlString.toString(), HospitalSetting.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSqlString.toString(), Long.class);

        if (request != null && StringUtils.isNotBlank(request.getHosName())) {
            query.setParameter("hosName", "%" + request.getHosName() + "%");
            countQuery.setParameter("hosName", "%" + request.getHosName() + "%");
        }
        if (request != null && StringUtils.isNotBlank(request.getHosCode())) {
            query.setParameter("hosCode", request.getHosCode());
            countQuery.setParameter("hosCode", request.getHosCode());
        }

        int start = page * size;
        query.setFirstResult(start).setMaxResults(size);

        List<HospitalSetting> content = query.getResultList();
        Long count = countQuery.getSingleResult();
        long totalPages = count % size == 0 ? count / size : count / size + 1;
        return new SearchResponse<>(content, pageNum, totalPages);
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
