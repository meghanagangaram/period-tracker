package com.periodtracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.periodtracker.model.PeriodData;

@Repository
public interface PeriodRepository extends JpaRepository<PeriodData, Long> {

    // ✅ THIS METHOD MUST EXIST
    List<PeriodData> findTop5ByOrderByStartDateDesc();
}