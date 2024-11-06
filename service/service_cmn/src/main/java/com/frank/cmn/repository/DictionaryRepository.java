package com.frank.cmn.repository;

import com.frank.model.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {

    @Query("FROM Dictionary d WHERE d.parentId = :id")
    List<Dictionary> findAllByParentId(@Param("id") Long id);
}