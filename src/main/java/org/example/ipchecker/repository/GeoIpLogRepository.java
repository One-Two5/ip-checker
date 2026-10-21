package org.example.ipchecker.repository;

import org.example.ipchecker.entity.GeoIpLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeoIpLogRepository extends JpaRepository<GeoIpLog, Long> {
}
