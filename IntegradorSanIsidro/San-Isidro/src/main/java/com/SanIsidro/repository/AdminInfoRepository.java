package com.SanIsidro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SanIsidro.model.AdminInfo;

@Repository
public interface AdminInfoRepository extends JpaRepository<AdminInfo, Long> {

}
